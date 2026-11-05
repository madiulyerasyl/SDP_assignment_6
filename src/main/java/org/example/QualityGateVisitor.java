
package org.example;

public class QualityGateVisitor implements ProjectVisitor {

    private int sourceFiles = 0;
    private int testFiles = 0;
    private int totalComplexity = 0;
    private int criticalIssues = 0;

    @Override
    public void visit(SourceCodeFile file) {
        sourceFiles++;
        totalComplexity += file.getComplexity();

        if (file.hasHardcodedCredentials()) {
            criticalIssues++;
        }
    }

    @Override
    public void visit(TestFile file) {
        testFiles++;
    }

    @Override
    public void visit(ConfigurationFile file) {
        if (file.hasExposedCredentials()) {
            criticalIssues++;
        }

        if (!file.isSecureConnectionEnabled()) {
            criticalIssues++;
        }
    }

    @Override
    public void visit(DependencyFile file) {
        if (file.hasVulnerableDependencies()) {
            criticalIssues++;
        }
    }

    @Override
    public void visit(Directory directory) {
        // Child elements are processed by Directory.accept().
    }

    public double getAverageComplexity() {
        if (sourceFiles == 0) {
            return 0.0;
        }

        return (double) totalComplexity / sourceFiles;
    }

    public double getTestSourceRatio() {
        if (sourceFiles == 0) {
            return 0.0;
        }

        return (double) testFiles / sourceFiles;
    }

    public int getCriticalIssues() {
        return criticalIssues;
    }

    public boolean isSecurityPassed() {
        return criticalIssues == 0;
    }

    public boolean isComplexityPassed() {
        return getAverageComplexity() <= 10.0;
    }

    public boolean isTestsPassed() {
        return sourceFiles > 0
                && getTestSourceRatio() >= 0.40;
    }

    public boolean isReadyForRelease() {
        return isSecurityPassed()
                && isComplexityPassed()
                && isTestsPassed();
    }

    private String status(boolean passed) {
        return passed ? "PASS" : "FAIL";
    }

    public void printReport() {
        System.out.println("\n=== QUALITY GATE REPORT ===");

        System.out.println("Security: "
                + status(isSecurityPassed()));

        System.out.println("Complexity: "
                + status(isComplexityPassed()));

        System.out.println("Tests: "
                + status(isTestsPassed()));

        System.out.printf(
                "Average complexity: %.2f%n",
                getAverageComplexity()
        );

        System.out.printf(
                "Test/source ratio: %.2f%n",
                getTestSourceRatio()
        );

        System.out.println("Critical issues: " + criticalIssues);

        System.out.println("\nFINAL RESULT: "
                + (isReadyForRelease() ? "PASSED" : "FAILED"));
    }
}
