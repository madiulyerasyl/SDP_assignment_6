
package org.example;

public class ProjectStatisticsVisitor implements ProjectVisitor {

    private int totalFiles = 0;
    private int totalLinesOfCode = 0;
    private int sourceFiles = 0;
    private int testFiles = 0;
    private int totalTestCases = 0;
    private int totalDependencies = 0;
    private int totalComplexity = 0;
    private int directoryCount = 0;

    @Override
    public void visit(SourceCodeFile file) {
        totalFiles++;
        sourceFiles++;
        totalLinesOfCode += file.getLinesOfCode();
        totalComplexity += file.getComplexity();
    }

    @Override
    public void visit(TestFile file) {
        totalFiles++;
        testFiles++;
        totalTestCases += file.getTestCount();
        totalLinesOfCode += file.getLinesOfCode();
    }

    @Override
    public void visit(ConfigurationFile file) {
        totalFiles++;
    }

    @Override
    public void visit(DependencyFile file) {
        totalFiles++;
        totalDependencies += file.getDependencyCount();
    }

    @Override
    public void visit(Directory directory) {
        directoryCount++;
    }

    public int getTotalFiles() {
        return totalFiles;
    }

    public int getTotalLinesOfCode() {
        return totalLinesOfCode;
    }

    public int getSourceFiles() {
        return sourceFiles;
    }

    public int getTestFiles() {
        return testFiles;
    }

    public int getTotalTestCases() {
        return totalTestCases;
    }

    public int getTotalDependencies() {
        return totalDependencies;
    }

    public int getDirectoryCount() {
        return directoryCount;
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

    public void printReport() {
        System.out.println("\n=== PROJECT STATISTICS REPORT ===");
        System.out.println("Total files: " + totalFiles);
        System.out.println("Directories: " + directoryCount);
        System.out.println("Source files: " + sourceFiles);
        System.out.println("Test files: " + testFiles);
        System.out.println("Test cases: " + totalTestCases);
        System.out.println("Lines of code: " + totalLinesOfCode);
        System.out.println("Dependencies: " + totalDependencies);

        System.out.printf(
                "Average complexity: %.2f%n",
                getAverageComplexity()
        );

        System.out.printf(
                "Test/source ratio: %.2f%n",
                getTestSourceRatio()
        );
    }
}
