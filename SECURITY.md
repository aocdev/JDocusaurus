# Security Policy

## Supported Versions

| Version | Supported |
|---------|-----------|
| 1.x     | Yes       |

## Scope

JDocusaurus is a **compile-time only** library (`RetentionPolicy.SOURCE`). It does not execute at runtime, does not process user input at runtime, and does not perform network operations. Its attack surface is limited to the annotation processing phase during compilation.

Potential security concerns include:

- **Path traversal** in `jdoc.outputDir` configuration (file write operations)
- **Code injection** in generated Markdown or Mermaid output
- **Dependency vulnerabilities** in JavaParser or other compile-time dependencies

## Reporting a Vulnerability

If you discover a security vulnerability, please report it responsibly:

1. **Do NOT open a public issue**
2. Email the maintainer at the address listed in the [pom.xml](pom.xml) developer section, or use [GitHub Security Advisories](https://github.com/aocdev/jdocusaurus/security/advisories/new)
3. Include:
   - Description of the vulnerability
   - Steps to reproduce
   - Potential impact
   - Suggested fix (if any)

You should receive an acknowledgment within **48 hours** and a resolution plan within **7 days**.

## Disclosure Policy

- Vulnerabilities will be fixed in a patch release
- A security advisory will be published after the fix is available
- Credit will be given to the reporter (unless anonymity is requested)
