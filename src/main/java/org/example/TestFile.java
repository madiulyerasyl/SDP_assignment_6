
package org.example;

public class TestFile implements ProjectElement {

    private final String name;
    private final int linesOfCode;
    private final int testCount;
    private final String testedModule;

    public TestFile(String name, int linesOfCode,
                    int testCount, String testedModule) {
        this.name = name;
        this.linesOfCode = linesOfCode;
        this.testCount = testCount;
        this.testedModule = testedModule;
    }

    public String getName() {
        return name;
    }

    public int getLinesOfCode() {
        return linesOfCode;
    }

    public int getTestCount() {
        return testCount;
    }

    public String getTestedModule() {
        return testedModule;
    }

    @Override
    public void accept(ProjectVisitor visitor) {
        visitor.visit(this);
    }
}
