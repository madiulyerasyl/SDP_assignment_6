
package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VisitorTest {

    @Test
    void testSourceCodeStatistics() {
        SourceCodeFile file = new SourceCodeFile(
                "PaymentService.java", 200, 8, false
        );

        ProjectStatisticsVisitor visitor =
                new ProjectStatisticsVisitor();

        file.accept(visitor);

        assertEquals(1, visitor.getTotalFiles());
        assertEquals(200, visitor.getTotalLinesOfCode());
        assertEquals(8.0, visitor.getAverageComplexity());
    }

    @Test
    void testTestFileStatistics() {
        TestFile file = new TestFile(
                "PaymentServiceTest.java", 100, 5,
                "PaymentService"
        );

        ProjectStatisticsVisitor visitor =
                new ProjectStatisticsVisitor();

        file.accept(visitor);

        assertEquals(1, visitor.getTestFiles());
        assertEquals(5, visitor.getTotalTestCases());
        assertEquals(100, visitor.getTotalLinesOfCode());
    }

    @Test
    void testSecurityHardcodedCredentials() {
        SourceCodeFile file = new SourceCodeFile(
                "PaymentService.java", 200, 8, true
        );

        SecurityAuditVisitor visitor =
                new SecurityAuditVisitor();

        file.accept(visitor);

        assertEquals(1, visitor.getCriticalIssues());
        assertEquals(80, visitor.getSecurityScore());
    }

    @Test
    void testSecurityConfiguration() {
        ConfigurationFile file = new ConfigurationFile(
                "application.properties", true, true, false
        );

        SecurityAuditVisitor visitor =
                new SecurityAuditVisitor();

        file.accept(visitor);

        assertEquals(2, visitor.getCriticalIssues());
        assertEquals(1, visitor.getWarningIssues());
        assertEquals(55, visitor.getSecurityScore());
    }

    @Test
    void testVulnerableDependencies() {
        DependencyFile file = new DependencyFile(
                "pom.xml", 5, true, false, false
        );

        SecurityAuditVisitor visitor =
                new SecurityAuditVisitor();

        file.accept(visitor);

        assertEquals(1, visitor.getCriticalIssues());
    }

    @Test
    void testQualityGatePass() {
        Directory project = new Directory("project");

        project.addElement(new SourceCodeFile(
                "Service.java", 100, 8, false
        ));

        project.addElement(new TestFile(
                "ServiceTest.java", 80, 5, "Service"
        ));

        QualityGateVisitor visitor =
                new QualityGateVisitor();

        project.accept(visitor);

        assertTrue(visitor.isReadyForRelease());
    }

    @Test
    void testQualityGateFail() {
        Directory project = new Directory("project");

        project.addElement(new SourceCodeFile(
                "Service.java", 200, 18, true
        ));

        QualityGateVisitor visitor =
                new QualityGateVisitor();

        project.accept(visitor);

        assertFalse(visitor.isReadyForRelease());
        assertFalse(visitor.isSecurityPassed());
        assertFalse(visitor.isComplexityPassed());
        assertFalse(visitor.isTestsPassed());
    }

    @Test
    void testComplianceMissingVersions() {
        DependencyFile file = new DependencyFile(
                "pom.xml", 5, false, true, false
        );

        ComplianceVisitor visitor = new ComplianceVisitor();

        file.accept(visitor);

        assertFalse(visitor.isCompliant());
        assertEquals(1, visitor.getViolationCount());
    }


    @Test
    void testNestedDirectories() {
        Directory root = new Directory("root");
        Directory src = new Directory("src");
        Directory services = new Directory("services");

        services.addElement(new SourceCodeFile(
                "PaymentService.java", 150, 7, false
        ));

        src.addElement(services);
        root.addElement(src);

        ProjectStatisticsVisitor visitor =
                new ProjectStatisticsVisitor();

        root.accept(visitor);

        assertEquals(3, visitor.getDirectoryCount());
        assertEquals(1, visitor.getTotalFiles());
        assertEquals(150, visitor.getTotalLinesOfCode());
    }

    @Test
    void testEmptyDirectory() {
        Directory empty = new Directory("empty");

        ProjectStatisticsVisitor visitor =
                new ProjectStatisticsVisitor();

        empty.accept(visitor);

        assertEquals(1, visitor.getDirectoryCount());
        assertEquals(0, visitor.getTotalFiles());
        assertEquals(0.0, visitor.getAverageComplexity());
        assertEquals(0.0, visitor.getTestSourceRatio());
    }

    @Test
    void testComplianceMissingTests() {
        Directory project = new Directory("project");

        project.addElement(new SourceCodeFile(
                "UserService.java", 100, 5, false
        ));

        ComplianceVisitor visitor = new ComplianceVisitor();

        project.accept(visitor);

        assertFalse(visitor.isCompliant());
        assertEquals(1, visitor.getViolationCount());
        assertTrue(visitor.getViolations().stream()
                .anyMatch(v -> v.contains("no tests")));
    }

    @Test
    void testComplianceProhibitedDependencies() {
        DependencyFile file = new DependencyFile(
                "pom.xml", 4, false, false, true
        );

        ComplianceVisitor visitor = new ComplianceVisitor();

        file.accept(visitor);

        assertFalse(visitor.isCompliant());
        assertEquals(1, visitor.getViolationCount());
    }

    @Test
    void testTechnicalDebtVisitor() {
        Directory project = new Directory("project");

        project.addElement(new SourceCodeFile(
                "Service.java", 350, 15, false
        ));

        TechnicalDebtVisitor visitor =
                new TechnicalDebtVisitor();

        project.accept(visitor);

        assertEquals(25, visitor.getDebtPoints());
        assertEquals("MEDIUM", visitor.getDebtLevel());
    }

    @Test
    void testQualityGateBoundaryValues() {
        Directory project = new Directory("project");

        for (int i = 1; i <= 5; i++) {
            project.addElement(new SourceCodeFile(
                    "Service" + i + ".java", 100, 10, false
            ));
        }

        for (int i = 1; i <= 2; i++) {
            project.addElement(new TestFile(
                    "Service" + i + "Test.java",
                    50, 3, "Service" + i
            ));
        }

        QualityGateVisitor visitor =
                new QualityGateVisitor();

        project.accept(visitor);

        assertEquals(10.0, visitor.getAverageComplexity());
        assertEquals(0.40, visitor.getTestSourceRatio(), 0.0001);
        assertTrue(visitor.isReadyForRelease());
    }

    @Test
    void testDirectoryRejectsNull() {
        Directory directory = new Directory("src");

        assertThrows(
                IllegalArgumentException.class,
                () -> directory.addElement(null)
        );
    }

    @Test
    void testSecurityScoreMinimumZero() {
        Directory project = new Directory("project");

        for (int i = 0; i < 10; i++) {
            project.addElement(new SourceCodeFile(
                    "Unsafe" + i + ".java",
                    100, 20, true
            ));
        }

        SecurityAuditVisitor visitor =
                new SecurityAuditVisitor();

        project.accept(visitor);

        assertEquals(10, visitor.getCriticalIssues());
        assertEquals(10, visitor.getWarningIssues());
        assertEquals(0, visitor.getSecurityScore());
    }

}
