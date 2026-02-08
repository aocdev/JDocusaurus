package org.aocdev.jdocusaurus.processor.scanner;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.aocdev.jdocusaurus.processor.model.CallGraphModel;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class JavaParserScanner {

    private final ProcessingEnvironment processingEnv;
    private final JavaParser javaParser;
    private final Map<String, CompilationUnit> parsedFiles = new HashMap<>();
    private final int maxDepth;

    public JavaParserScanner(ProcessingEnvironment processingEnv) {
        this(processingEnv, 5);
    }

    public JavaParserScanner(ProcessingEnvironment processingEnv, int maxDepth) {
        this.processingEnv = processingEnv;
        this.javaParser = new JavaParser();
        this.maxDepth = maxDepth;
    }

    public CallGraphModel analyzeMethod(String qualifiedClassName, String methodName) {
        CallGraphModel graph = new CallGraphModel();
        String simpleClassName = extractSimpleName(qualifiedClassName);
        graph.setSourceClass(simpleClassName);
        graph.setSourceMethod(methodName);

        CompilationUnit cu = getCompilationUnit(qualifiedClassName);
        if (cu == null) return graph;

        Optional<ClassOrInterfaceDeclaration> classDecl = cu.findFirst(
                ClassOrInterfaceDeclaration.class,
                c -> c.getNameAsString().equals(simpleClassName)
        );
        if (classDecl.isEmpty()) return graph;

        Map<String, String> fieldTypeMap = buildFieldTypeMap(classDecl.get());

        Optional<MethodDeclaration> methodDecl = classDecl.get().getMethodsByName(methodName).stream().findFirst();
        if (methodDecl.isEmpty()) return graph;

        analyzeMethodBody(methodDecl.get(), simpleClassName, fieldTypeMap, graph, new HashSet<>(), 0);

        return graph;
    }

    private void analyzeMethodBody(MethodDeclaration method, String currentClass,
                                   Map<String, String> fieldTypeMap, CallGraphModel graph,
                                   Set<String> visited, int depth) {
        if (depth >= maxDepth) return;

        method.getBody().ifPresent(body -> {
            body.findAll(IfStmt.class).forEach(ifStmt -> {
                String condition = ifStmt.getCondition().toString();

                ifStmt.getThenStmt().findAll(MethodCallExpr.class).forEach(call -> {
                    CallGraphModel.CallEdge edge = buildEdge(call, currentClass, fieldTypeMap);
                    if (edge != null) {
                        edge.setCondition(condition);
                        if (!isDuplicate(graph, edge)) {
                            graph.addEdge(edge);
                            recurseIntoCall(edge, fieldTypeMap, visited, depth);
                        }
                    }
                });

                ifStmt.getElseStmt().ifPresent(elseStmt ->
                    elseStmt.findAll(MethodCallExpr.class).forEach(call -> {
                        CallGraphModel.CallEdge edge = buildEdge(call, currentClass, fieldTypeMap);
                        if (edge != null) {
                            edge.setCondition("else");
                            if (!isDuplicate(graph, edge)) {
                                graph.addEdge(edge);
                                recurseIntoCall(edge, fieldTypeMap, visited, depth);
                            }
                        }
                    })
                );
            });

            body.findAll(MethodCallExpr.class).forEach(call -> {
                if (isInsideIfBlock(call, body)) return;

                CallGraphModel.CallEdge edge = buildEdge(call, currentClass, fieldTypeMap);
                if (edge != null && !isDuplicate(graph, edge)) {
                    graph.addEdge(edge);
                    recurseIntoCall(edge, fieldTypeMap, visited, depth);
                }
            });
        });
    }

    private CallGraphModel.CallEdge buildEdge(MethodCallExpr call, String currentClass,
                                               Map<String, String> fieldTypeMap) {
        if (call.getScope().isEmpty()) return null;

        String scopeStr = call.getScope().get().toString();

        if (scopeStr.contains(".") || scopeStr.equals("System") || scopeStr.equals("Math")
                || scopeStr.equals("String") || scopeStr.equals("Objects")
                || scopeStr.equals("Collections") || scopeStr.equals("Arrays")) {
            return null;
        }

        String targetType = fieldTypeMap.get(scopeStr);
        if (targetType == null) return null;

        CallGraphModel.CallEdge edge = new CallGraphModel.CallEdge();
        edge.setSourceClass(currentClass);
        edge.setTargetField(scopeStr);
        edge.setTargetFieldType(targetType);
        edge.setTargetMethod(call.getNameAsString());

        StringBuilder args = new StringBuilder();
        call.getArguments().forEach(arg -> {
            if (!args.isEmpty()) args.append(", ");
            if (arg instanceof NameExpr) {
                args.append(((NameExpr) arg).getNameAsString());
            } else {
                String argStr = arg.toString();
                args.append(argStr.length() > 20 ? argStr.substring(0, 20) + "..." : argStr);
            }
        });
        edge.setArguments(args.toString());

        boolean isAsync = call.toString().contains("Async") ||
                call.toString().contains("CompletableFuture") ||
                call.toString().contains("supplyAsync");
        edge.setAsync(isAsync);

        return edge;
    }

    private void recurseIntoCall(CallGraphModel.CallEdge edge, Map<String, String> parentFieldMap,
                                  Set<String> visited, int depth) {
        String callKey = edge.getTargetFieldType() + "." + edge.getTargetMethod();
        if (visited.contains(callKey)) return;
        visited.add(callKey);

        String targetQualified = findQualifiedName(edge.getTargetFieldType());
        if (targetQualified == null) return;

        CompilationUnit cu = getCompilationUnit(targetQualified);
        if (cu == null) return;

        Optional<ClassOrInterfaceDeclaration> classDecl = cu.findFirst(
                ClassOrInterfaceDeclaration.class,
                c -> c.getNameAsString().equals(edge.getTargetFieldType())
        );
        if (classDecl.isEmpty()) return;

        Map<String, String> targetFieldMap = buildFieldTypeMap(classDecl.get());
        Optional<MethodDeclaration> method = classDecl.get().getMethodsByName(edge.getTargetMethod()).stream().findFirst();
        if (method.isEmpty()) return;

        CallGraphModel subGraph = new CallGraphModel();
        subGraph.setSourceClass(edge.getTargetFieldType());
        subGraph.setSourceMethod(edge.getTargetMethod());
        analyzeMethodBody(method.get(), edge.getTargetFieldType(), targetFieldMap, subGraph, visited, depth + 1);

        if (!subGraph.isEmpty()) {
            edge.setSubCalls(subGraph);
        }
    }

    private Map<String, String> buildFieldTypeMap(ClassOrInterfaceDeclaration classDecl) {
        Map<String, String> map = new HashMap<>();
        for (FieldDeclaration field : classDecl.getFields()) {
            for (VariableDeclarator var : field.getVariables()) {
                String type = var.getTypeAsString();
                int genericStart = type.indexOf('<');
                if (genericStart > 0) type = type.substring(0, genericStart);
                map.put(var.getNameAsString(), type);
            }
        }
        return map;
    }

    private CompilationUnit getCompilationUnit(String qualifiedClassName) {
        if (parsedFiles.containsKey(qualifiedClassName)) {
            return parsedFiles.get(qualifiedClassName);
        }

        CompilationUnit cu = tryParseFromSourcePath(qualifiedClassName);
        parsedFiles.put(qualifiedClassName, cu);
        return cu;
    }

    private CompilationUnit tryParseFromSourcePath(String qualifiedClassName) {
        String relativePath = qualifiedClassName.replace('.', '/') + ".java";

        String sourcePath = processingEnv.getOptions().get("jdoc.sourcePath");
        if (sourcePath != null) {
            for (String dir : sourcePath.split(";")) {
                Path file = Paths.get(dir.trim(), relativePath);
                if (Files.exists(file)) {
                    return parseFile(file);
                }
            }
        }

        try {
            FileObject fileObject = processingEnv.getFiler().getResource(
                    StandardLocation.SOURCE_PATH, "", relativePath);
            ParseResult<CompilationUnit> result = javaParser.parse(fileObject.openReader(true));
            if (result.isSuccessful() && result.getResult().isPresent()) {
                return result.getResult().get();
            }
        } catch (Exception ignored) {}

        String[] commonPaths = {"src/main/java", "src/java", "src"};
        for (String base : commonPaths) {
            Path file = Paths.get(base, relativePath);
            if (Files.exists(file)) {
                return parseFile(file);
            }
        }

        return null;
    }

    private CompilationUnit parseFile(Path file) {
        try (Reader reader = Files.newBufferedReader(file)) {
            ParseResult<CompilationUnit> result = javaParser.parse(reader);
            if (result.isSuccessful() && result.getResult().isPresent()) {
                return result.getResult().get();
            }
        } catch (IOException ignored) {}
        return null;
    }

    private String findQualifiedName(String simpleClassName) {
        for (Map.Entry<String, CompilationUnit> entry : parsedFiles.entrySet()) {
            if (entry.getKey().endsWith("." + simpleClassName)) {
                return entry.getKey();
            }
        }

        String sourcePath = processingEnv.getOptions().get("jdoc.sourcePath");
        if (sourcePath != null) {
            for (String dir : sourcePath.split(";")) {
                Path found = findJavaFile(Paths.get(dir.trim()), simpleClassName);
                if (found != null) {
                    CompilationUnit cu = parseFile(found);
                    if (cu != null) {
                        String pkg = cu.getPackageDeclaration()
                                .map(pd -> pd.getNameAsString() + ".")
                                .orElse("");
                        String qualified = pkg + simpleClassName;
                        parsedFiles.put(qualified, cu);
                        return qualified;
                    }
                }
            }
        }

        String[] commonPaths = {"src/main/java", "src/java", "src"};
        for (String base : commonPaths) {
            Path found = findJavaFile(Paths.get(base), simpleClassName);
            if (found != null) {
                CompilationUnit cu = parseFile(found);
                if (cu != null) {
                    String pkg = cu.getPackageDeclaration()
                            .map(pd -> pd.getNameAsString() + ".")
                            .orElse("");
                    String qualified = pkg + simpleClassName;
                    parsedFiles.put(qualified, cu);
                    return qualified;
                }
            }
        }

        return null;
    }

    private Path findJavaFile(Path baseDir, String simpleClassName) {
        String fileName = simpleClassName + ".java";
        try (var walk = Files.walk(baseDir)) {
            return walk.filter(p -> p.getFileName().toString().equals(fileName))
                    .findFirst()
                    .orElse(null);
        } catch (IOException e) {
            return null;
        }
    }

    private boolean isInsideIfBlock(MethodCallExpr call, com.github.javaparser.ast.stmt.BlockStmt body) {
        return call.findAncestor(IfStmt.class)
                .filter(ifStmt -> ifStmt.findAncestor(com.github.javaparser.ast.stmt.BlockStmt.class)
                        .map(b -> b == body).orElse(false))
                .isPresent();
    }

    private boolean isDuplicate(CallGraphModel graph, CallGraphModel.CallEdge edge) {
        return graph.getEdges().stream().anyMatch(e ->
                e.getTargetFieldType().equals(edge.getTargetFieldType()) &&
                e.getTargetMethod().equals(edge.getTargetMethod()) &&
                Objects.equals(e.getCondition(), edge.getCondition())
        );
    }

    private String extractSimpleName(String qualifiedName) {
        int lastDot = qualifiedName.lastIndexOf('.');
        return lastDot >= 0 ? qualifiedName.substring(lastDot + 1) : qualifiedName;
    }
}
