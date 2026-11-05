
package org.example;

import java.util.ArrayList;
import java.util.List;

public class SecurityAuditVisitor implements ProjectVisitor {

    private final List<String> issues = new ArrayList<>();
    private int criticalIssues = 0;
    private int warningIssues = 0;

    @Override
    public void visit(SourceCodeFile file) {
        if (file.hasHardcodedCredentials()) {
            addCritical(file.getName()
                    + ": Hardcoded credentials detected");
        }

        if (file.getComplexity() > 15) {
            addWarning(file.getName()
                    + ": High-risk source file (complexity > 15)");
        }
    }

    @Override
    public void visit(TestFile file) {
        if (file.getTestCount() == 0) {
            addWarning(file.getName()
                    + ": No test cases found");
        }
    }

    @Override
    public void visit(ConfigurationFile file) {
        if (file.hasExposedCredentials()) {
            addCritical(file.getName()
                    + ": Exposed credentials detected");
        }

        if (file.isDebugModeEnabled()) {
            addWarning(file.getName()
                    + ": Debug mode is enabled");
        }

        if (!file.isSecureConnectionEnabled()) {
            addCritical(file.getName()
                    + ": Secure connection is disabled");
        }
    }

    @Override
    public void visit(DependencyFile file) {
        if (file.hasVulnerableDependencies()) {
            addCritical(file.getName()
                    + ": Vulnerable dependencies detected");
        }
    }

    @Override
    public void visit(Directory directory) {
        // Directory contents are processed by Directory.accept().
    }

    private void addCritical(String message) {
        criticalIssues++;
        issues.add("[CRITICAL] " + message);
    }

    private void addWarning(String message) {
        warningIssues++;
        issues.add("[WARNING] " + message);
    }

    public int getCriticalIssues() {
        return criticalIssues;
    }

    public int getWarningIssues() {
        return warningIssues;
    }

    public List<String> getIssues() {
        return List.copyOf(issues);
    }

    public int getSecurityScore() {
        return Math.max(
                0,
                100 - criticalIssues * 20 - warningIssues * 5
        );
    }

    public void printReport() {
        System.out.println("\n=== SECURITY AUDIT REPORT ===");

        if (issues.isEmpty()) {
            System.out.println("No security issues found.");
        } else {
            for (String issue : issues) {
                System.out.println(issue);
            }
        }

        System.out.println("Critical issues: " + criticalIssues);
        System.out.println("Warnings: " + warningIssues);
        System.out.println("Security score: "
                + getSecurityScore() + "/100");
    }
}
