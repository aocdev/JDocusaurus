package org.aocdev.jdocusaurus.processor.model;

import java.util.ArrayList;
import java.util.List;

public class EndpointModel {
    private String methodName;
    private String httpMethod;
    private String path;
    private String description;
    private String summary;
    private String produces;
    private String consumes;
    private boolean deprecated;
    private String deprecatedMessage;
    private String auth;
    private List<ParamModel> params = new ArrayList<>();
    private List<ResponseModel> responses = new ArrayList<>();
    private List<HeaderModel> headers = new ArrayList<>();
    private List<FlowStepModel> manualSteps = new ArrayList<>();
    private CallGraphModel callGraph;

    public String getMethodName() { return methodName; }
    public void setMethodName(String methodName) { this.methodName = methodName; }

    public String getHttpMethod() { return httpMethod; }
    public void setHttpMethod(String httpMethod) { this.httpMethod = httpMethod; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public String getProduces() { return produces; }
    public void setProduces(String produces) { this.produces = produces; }

    public String getConsumes() { return consumes; }
    public void setConsumes(String consumes) { this.consumes = consumes; }

    public boolean isDeprecated() { return deprecated; }
    public void setDeprecated(boolean deprecated) { this.deprecated = deprecated; }

    public String getDeprecatedMessage() { return deprecatedMessage; }
    public void setDeprecatedMessage(String deprecatedMessage) { this.deprecatedMessage = deprecatedMessage; }

    public String getAuth() { return auth; }
    public void setAuth(String auth) { this.auth = auth; }

    public List<ParamModel> getParams() { return params; }
    public void setParams(List<ParamModel> params) { this.params = params; }

    public List<ResponseModel> getResponses() { return responses; }
    public void setResponses(List<ResponseModel> responses) { this.responses = responses; }

    public List<HeaderModel> getHeaders() { return headers; }
    public void setHeaders(List<HeaderModel> headers) { this.headers = headers; }

    public List<FlowStepModel> getManualSteps() { return manualSteps; }
    public void setManualSteps(List<FlowStepModel> manualSteps) { this.manualSteps = manualSteps; }

    public CallGraphModel getCallGraph() { return callGraph; }
    public void setCallGraph(CallGraphModel callGraph) { this.callGraph = callGraph; }

    public boolean hasFlowDiagram() {
        return !manualSteps.isEmpty() || (callGraph != null && !callGraph.isEmpty());
    }
}
