# Contributing to JDocusaurus

First off, thank you for considering contributing to JDocusaurus! Every contribution helps make this project better for the Java community.

## Code of Conduct

This project adheres to the [Contributor Covenant v2.1](https://www.contributor-covenant.org/version/2/1/code_of_conduct/). By participating, you are expected to uphold this code. Please report unacceptable behavior via [GitHub Issues](https://github.com/aocdev/jdocusaurus/issues).

## How Can I Contribute?

### Reporting Bugs

Before creating a bug report, please check [existing issues](https://github.com/aocdev/jdocusaurus/issues) to avoid duplicates.

When filing a bug, include:

- **JDocusaurus version** and **Java version**
- **Minimal reproducible example** (annotation + class that triggers the issue)
- **Expected vs actual** generated Markdown output
- **Build log** with the relevant `JDocusaurus:` messages

### Suggesting Features

Open a [GitHub Discussion](https://github.com/aocdev/jdocusaurus/discussions) with:

- **Use case**: What problem does this solve?
- **Proposed annotation/option**: How would users interact with it?
- **Example output**: What Markdown/Mermaid should be generated?

### Submitting Code

We welcome pull requests for bug fixes, new annotations, generators, and documentation improvements.

## Development Setup

### Prerequisites

- **Java 21+** (LTS, compilation target is 21)
- **Maven 3.8+**
- **Git**

### Building the Project

```bash
git clone https://github.com/aocdev/jdocusaurus.git
cd jdocusaurus
mvn clean verify
```

The project is a multi-module Maven build:

```
jdocusaurus/
  jdocusaurus-annotations/   # @JDoc* annotations and enums
  jdocusaurus-processor/      # Annotation processor + generators
  jdocusaurus-test/           # Example module for verification
```

### Running Tests

```bash
# Full build with verification
mvn clean verify

# Only compile (runs annotation processing on test module)
mvn clean compile
```

A successful build shows:

```
JDocusaurus: generados N ficheros (...)
```

Check generated output in `jdocusaurus-test/target/classes/docs/`.

## Branching Strategy (GitHub Flow)

We use [GitHub Flow](https://docs.github.com/en/get-started/using-github/github-flow):

1. **`main`** is always deployable
2. Create a **feature branch** from `main`
3. Open a **Pull Request** when ready
4. After review and CI passing, merge to `main`

### Branch Naming

```
feat/short-description      # New feature
fix/short-description       # Bug fix
docs/short-description      # Documentation only
refactor/short-description  # Code refactoring
```

Examples:

```
feat/jdoc-scheduler-annotation
fix/er-diagram-missing-relations
docs/update-readme-examples
refactor/extract-markdown-utils
```

## Commit Convention

We follow [Conventional Commits v1.0.0](https://www.conventionalcommits.org/en/v1.0.0/).

### Format

```
<type>(<scope>): <description>

[optional body]

[optional footer(s)]
```

### Types

| Type | Description |
|------|-------------|
| `feat` | A new feature (new annotation, generator, option) |
| `fix` | A bug fix |
| `docs` | Documentation only changes |
| `style` | Code style (formatting, missing semicolons, no logic change) |
| `refactor` | Code change that neither fixes a bug nor adds a feature |
| `perf` | Performance improvement |
| `test` | Adding or correcting tests |
| `build` | Changes to build system or dependencies (Maven, pom.xml) |
| `ci` | CI configuration changes (GitHub Actions, workflows) |
| `chore` | Other changes that don't modify src or test files |
| `revert` | Reverts a previous commit |

### Scopes (optional)

- `annotations` - Changes in `jdocusaurus-annotations`
- `processor` - Changes in `jdocusaurus-processor`
- `generator` - Changes in generators (Markdown, Mermaid, Sidebar...)
- `scanner` - Changes in scanners (Annotation, JavaParser, JPA)
- `model` - Changes in model classes
- `config` - Changes in processor configuration
- `test` - Changes in `jdocusaurus-test`

### Examples

```
feat(annotations): add @JDocScheduler annotation for cron jobs

fix(generator): fix ER diagram missing nullable fields

docs: update README with absolute path examples

refactor(scanner): extract common mirror utils to helper class

build: upgrade JavaParser to 3.28.0

feat(annotations)!: rename @JDocResponse code to statusCode

BREAKING CHANGE: @JDocResponse.code() has been renamed to statusCode()
```

### Breaking Changes

Append `!` after the type/scope and add a `BREAKING CHANGE:` footer:

```
feat(annotations)!: rename severity levels in @JDocBusinessRule

BREAKING CHANGE: RuleSeverity.MANDATORY renamed to RuleSeverity.CRITICAL
```

## Versioning

We follow [Semantic Versioning 2.0.0](https://semver.org/):

```
MAJOR.MINOR.PATCH
```

| Increment | When |
|-----------|------|
| **MAJOR** | Breaking changes to annotations, processor options, or generated output structure |
| **MINOR** | New annotations, generators, options, or non-breaking enhancements |
| **PATCH** | Bug fixes, documentation, internal refactoring |

The version is derived from Conventional Commits:

- `feat` commits trigger a **MINOR** bump
- `fix` commits trigger a **PATCH** bump
- `BREAKING CHANGE` footer triggers a **MAJOR** bump

## Pull Request Process

### Before Submitting

1. **Create an issue first** for non-trivial changes (new annotations, generators, etc.)
2. **Branch from `main`**: `git checkout -b feat/my-feature main`
3. **Follow commit conventions** described above
4. **Ensure the build passes**: `mvn clean verify`
5. **Add/update tests** if applicable (annotated example classes in `jdocusaurus-test`)
6. **Update documentation** if you add new annotations or options

### PR Template

When opening a PR, include:

```markdown
## Summary
Brief description of changes.

## Related Issue
Closes #123

## Type of Change
- [ ] Bug fix (non-breaking)
- [ ] New feature (non-breaking)
- [ ] Breaking change
- [ ] Documentation update

## Checklist
- [ ] `mvn clean verify` passes
- [ ] New/changed annotations have test examples in `jdocusaurus-test`
- [ ] Generated Markdown output is correct
- [ ] Conventional Commits followed
- [ ] README updated (if new annotations or options)
```

### Review Criteria

PRs will be reviewed for:

- **Correctness**: Generated Markdown/Mermaid output is valid
- **Consistency**: Follows existing patterns (`@JDoc` prefix, Spanish output labels, etc.)
- **No runtime dependencies**: JDocusaurus is compile-time only (`RetentionPolicy.SOURCE`)
- **Backward compatibility**: Existing annotations must not break

## Architecture Guidelines

When contributing code, keep these principles in mind:

### Annotations (`jdocusaurus-annotations`)

- All annotations use the `@JDoc` prefix
- Use `RetentionPolicy.SOURCE` (compile-time only)
- Use `@Repeatable` with a container annotation when needed
- Group by domain: `api`, `flow`, `data`, `event`, `rule`, `integration`, `config`

### Generators (`jdocusaurus-processor`)

- Implement the `Generator` interface: `void generate(ProjectModel model, DocWriter writer)`
- Use `DocWriter.write(relativePath, content)` for output (supports absolute and relative paths)
- Generate Spanish-language labels in Markdown (`sidebar_label`, section titles)
- Include Docusaurus frontmatter (`---\nsidebar_label: "..."\n---`)

### Scanners

- `AnnotationScanner`: Reads `@JDoc*` annotations via `javax.annotation.processing` API
- `JpaScanner`: Reads JPA metadata via `AnnotationMirror` (no JPA dependency)
- `JavaParserScanner`: Static analysis for automatic flow detection

### Models

- Plain Java objects, no external dependencies
- One model per documented concept (`ClassModel`, `EndpointModel`, `EntityModel`, etc.)

## Getting Help

- **Questions**: Open a [GitHub Discussion](https://github.com/aocdev/jdocusaurus/discussions)
- **Bugs**: Open a [GitHub Issue](https://github.com/aocdev/jdocusaurus/issues)
- **Feature ideas**: Open a [GitHub Discussion](https://github.com/aocdev/jdocusaurus/discussions) first

Thank you for contributing!
