package org.example;

public class SourceCodeFile implements ProjectElement {

    private final String name;
    private final int linesOfCode;
    private final int complexity;
    private final boolean hasHardcodedCredentials;

    public SourceCodeFile(String name, int linesOfCode,
                          int complexity,
                          boolean hasHardcodedCredentials) {
        this.name = name;
        this.linesOfCode = linesOfCode;
        this.complexity = complexity;
        this.hasHardcodedCredentials = hasHardcodedCredentials;
    }

    public String getName() {
        return name;
    }

    public int getLinesOfCode() {
        return linesOfCode;
    }

    public int getComplexity() {
        return complexity;
    }

    public boolean hasHardcodedCredentials() {
        return hasHardcodedCredentials;
    }

    @Override
    public void accept(ProjectVisitor visitor) {
        visitor.visit(this);
    }
}
