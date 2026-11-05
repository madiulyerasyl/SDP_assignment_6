
package org.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ComplianceVisitor implements ProjectVisitor {

    private final List<String> violations = new ArrayList<>();
    private final Set<String> productionModules = new HashSet<>();
    private final Set<String> testedModules = new HashSet<>();

    @Override
    public void visit(SourceCodeFile file) {
        String moduleName = removeJavaExtension(file.getName());
        productionModules.add(moduleName);

        if (file.hasHardcodedCredentials()) {
            violations.add(file.getName()
                    + ": Hardcoded credentials are prohibited");
        }
    }

    @Override
    public void visit(TestFile file) {
        if (file.getTestCount() > 0) {
            testedModules.add(file.getTestedModule());
        }
    }

    @Override
    public void visit(ConfigurationFile file) {
        if (file.hasExposedCredentials()) {
            violations.add(file.getName()
                    + ": Exposed credentials are prohibited");
        }
    }

    @Override
    public void visit(DependencyFile file) {
        if (file.hasMissingVersions()) {
            violations.add(file.getName()
                    + ": Dependency versions must be specified");
        }

        if (file.hasProhibitedDependencies()) {
            violations.add(file.getName()
                    + ": Prohibited dependencies detected");
        }
    }

    @Override
    public void visit(Directory directory) {
        // Child elements are processed by Directory.accept().
    }

    private String removeJavaExtension(String name) {
        if (name.endsWith(".java")) {
            return name.substring(0, name.length() - 5);
        }

        return name;
    }

    public List<String> getViolations() {
        List<String> result = new ArrayList<>(violations);

        for (String module : productionModules) {
            if (!testedModules.contains(module)) {
                result.add(module
                        + ": Production module has no tests");
            }
        }

        return List.copyOf(result);
    }

    public int getViolationCount() {
        return getViolations().size();
    }

    public boolean isCompliant() {
        return getViolations().isEmpty();
    }

    public void printReport() {
        System.out.println("\n=== COMPLIANCE REPORT ===");

        List<String> allViolations = getViolations();

        if (allViolations.isEmpty()) {
            System.out.println("All compliance rules passed.");
        } else {
            for (String violation : allViolations) {
                System.out.println("[VIOLATION] " + violation);
            }
        }

        System.out.println("Total violations: "
                + allViolations.size());

        System.out.println("FINAL RESULT: "
                + (allViolations.isEmpty()
                ? "COMPLIANT" : "NON-COMPLIANT"));
    }
}
