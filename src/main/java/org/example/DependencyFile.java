
package org.example;

public class DependencyFile implements ProjectElement {

    private final String name;
    private final int dependencyCount;
    private final boolean hasVulnerableDependencies;
    private final boolean hasMissingVersions;
    private final boolean hasProhibitedDependencies;

    public DependencyFile(String name,
                          int dependencyCount,
                          boolean hasVulnerableDependencies,
                          boolean hasMissingVersions,
                          boolean hasProhibitedDependencies) {
        this.name = name;
        this.dependencyCount = dependencyCount;
        this.hasVulnerableDependencies = hasVulnerableDependencies;
        this.hasMissingVersions = hasMissingVersions;
        this.hasProhibitedDependencies = hasProhibitedDependencies;
    }

    public String getName() {
        return name;
    }

    public int getDependencyCount() {
        return dependencyCount;
    }

    public boolean hasVulnerableDependencies() {
        return hasVulnerableDependencies;
    }

    public boolean hasMissingVersions() {
        return hasMissingVersions;
    }

    public boolean hasProhibitedDependencies() {
        return hasProhibitedDependencies;
    }

    @Override
    public void accept(ProjectVisitor visitor) {
        visitor.visit(this);
    }
}
