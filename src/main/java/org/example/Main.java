
package org.example;

public class Main {

    public static void main(String[] args) {

        Directory project = new Directory("fintech-project");

        Directory src = new Directory("src");

        src.addElement(new SourceCodeFile(
                "PaymentService.java", 250, 18, true
        ));

        src.addElement(new SourceCodeFile(
                "UserService.java", 180, 8, false
        ));

        Directory tests = new Directory("tests");

        tests.addElement(new TestFile(
                "PaymentServiceTest.java", 120, 8,
                "PaymentService"
        ));

        Directory config = new Directory("config");

        config.addElement(new ConfigurationFile(
                "application.properties", true, true, false
        ));

        project.addElement(src);
        project.addElement(tests);
        project.addElement(config);

        project.addElement(new DependencyFile(
                "pom.xml", 8, true, false, false
        ));

        SecurityAuditVisitor security = new SecurityAuditVisitor();
        project.accept(security);
        security.printReport();

        ProjectStatisticsVisitor statistics =
                new ProjectStatisticsVisitor();

        project.accept(statistics);
        statistics.printReport();


        QualityGateVisitor quality = new QualityGateVisitor();

        project.accept(quality);

        quality.printReport();


        ComplianceVisitor compliance = new ComplianceVisitor();

        project.accept(compliance);

        compliance.printReport();


        TechnicalDebtVisitor technicalDebt = new TechnicalDebtVisitor();

        project.accept(technicalDebt);

        technicalDebt.printReport();



    }
}
