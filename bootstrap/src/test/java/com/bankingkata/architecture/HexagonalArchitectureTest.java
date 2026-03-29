package com.bankingkata.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(
    packages = "com.bankingkata"
    //importOptions = ImportOption.DoNotIncludeTests.class
)
public class HexagonalArchitectureTest {

    @ArchTest
    static final ArchRule domain_model_should_not_depend_on_spring =
        noClasses()
            .that().resideInAPackage("com.bankingkata.model..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("org.springframework..");

    @ArchTest
    static final ArchRule domain_should_not_depend_on_adapters =
        noClasses()
            .that().resideInAnyPackage(
                "com.bankingkata.model..",
                "com.bankingkata.port..",
                "com.bankingkata.exception.."
            )
            .should()
            .dependOnClassesThat()
            .resideInAPackage("com.bankingkata.adapter..");

    @ArchTest
    static final ArchRule application_should_not_depend_on_adapters =
        noClasses()
            .that().resideInAPackage("com.bankingkata.service..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("com.bankingkata.adapter..");

    @ArchTest
    static final ArchRule controllers_should_reside_in_rest_package =
        noClasses()
            .that().haveNameMatching(".*Controller")
            .should()
            .resideOutsideOfPackage("com.bankingkata.adapter.in.rest..");

    @ArchTest
    static final ArchRule persistence_adapters_should_reside_in_persistence_package =
        noClasses()
            .that().haveNameMatching(".*PersistenceAdapter")
            .should()
            .resideOutsideOfPackage("com.bankingkata.adapter.out.persistence..");

    @ArchTest
    static final ArchRule controllers_should_not_depend_on_persistence =
        noClasses()
            .that().resideInAPackage("com.bankingkata.adapter.in.rest..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("com.bankingkata.adapter.out.persistence..");            

}
