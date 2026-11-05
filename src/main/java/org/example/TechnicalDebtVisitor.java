
package org.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TechnicalDebtVisitor implements ProjectVisitor {

    private final List<String> findings = new ArrayList<>();
    private final Set<String> productionModules = new HashSet<>();
    private final Set<String> testedModules = new HashSet<>();

    private int debtPoints = 0;

    @Override
    public void visit(SourceCodeFile file) {
        String moduleName = file.getName();

        if (moduleName.endsWith(".java")) {
            moduleName = moduleName.substring(
                    0, moduleName.length() - 5
            );
        }

        productionModules.add(moduleName);

        if (file.getComplexity() > 10) {
            debtPoints += 10;
            findings.add(file.getName()
                    + ": High complexity (+10 points)");
        }

        if (file.getLinesOfCode() > 300) {
            debtPoints += 5;
            findings.add(file.getName()
                    + ": Large source file (+5 points)");
        }
    }

    @Override
    public void visit(TestFile file) {
        if (file.getTestCount() > 0) {
            testedModules.add(file.getTestedModule());
        }

        if (file.getTestCount() == 0) {
            debtPoints += 5;
            findings.add(file.getName()
                    + ": Empty test file (+5 points)");
        }
    }

    @Override
    public void visit(ConfigurationFile file) {
        if (file.isDebugModeEnabled()) {
            debtPoints += 3;
            findings.add(file.getName()
                    + ": Debug mode enabled (+3 points)");
        }
    }

    @Override
    public void visit(DependencyFile file) {
        if (file.hasMissingVersions()) {
            debtPoints += 5;
            findings.add(file.getName()
                    + ": Missing dependency versions (+5 points)");
        }
    }

    @Override
    public void visit(Directory directory) {
        // Children are visited by Directory.accept().
    }

    public List<String> getFindings() {
        List<String> result = new ArrayList<>(findings);

        for (String module : productionModules) {
            if (!testedModules.contains(module)) {
                result.add(module
                        + ": No tests (+10 points)");
            }
        }

        return List.copyOf(result);
    }

    public int getDebtPoints() {
        int total = debtPoints;

        for (String module : productionModules) {
            if (!testedModules.contains(module)) {
                total += 10;
            }
        }

        return total;
    }

    public String getDebtLevel() {
        int total = getDebtPoints();

        if (total == 0) {
            return "NONE";
        } else if (total <= 15) {
            return "LOW";
        } else if (total <= 30) {
            return "MEDIUM";
        } else {
            return "HIGH";
        }
    }

    public void printReport() {
        System.out.println("\n=== TECHNICAL DEBT REPORT ===");

        List<String> allFindings = getFindings();

        if (allFindings.isEmpty()) {
            System.out.println("No technical debt detected.");
        } else {
            for (String finding : allFindings) {
                System.out.println("[DEBT] " + finding);
            }
        }

        System.out.println("Total debt points: " + getDebtPoints());
        System.out.println("Debt level: " + getDebtLevel());
    }
}
