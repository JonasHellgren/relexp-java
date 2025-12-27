package archunit;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.tngtech.archunit.base.Predicates.alwaysTrue;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

@Log
public class ArchitectureTest {


    protected static JavaClasses importedClasses;

    @BeforeAll
    public static void setUp() throws IOException {
        log.info("Setting up the architecture test environment...");
        Path mainClasses = Paths.get(System.getProperty("user.dir"), "target", "classes");

        importedClasses = new ClassFileImporter()
                .importPath(mainClasses);
        log.info("Setup complete. Classes imported, nof = "+ importedClasses.size());
     //   log.info("Classes imported="+ importedClasses);
    }

    private static List<Path> getAllModuleClassPaths() throws IOException {
        Path rootPath = new File(System.getProperty("user.dir")).toPath();
        try (Stream<Path> pathStream = Files.walk(rootPath)) {
            return pathStream
                    .filter(path -> path.toString().endsWith("build/classes/java/main"))
                    .collect(Collectors.toList());
        }
    }

    @Test
    public void noCircularDependencies() {
        executeRule(
                "noCircularDependencies",
                slices()
                        .matching("..(*)..")
                        .should()
                        .beFreeOfCycles()
                        .because("There should be no cyclic dependencies among packages."));
    }

    @Test
    public void foundationLayerShouldBeIndependent() {
        executeRule(
                "foundationLayerShouldBeIndependent",
                classes()
                        .that()
                        .resideInAPackage("..foundation..")
                        .should()
                        .onlyDependOnClassesThat()
                        .resideInAnyPackage("java..", "..api..","..lombok..")
                        .because("The foundation layer should not depend on other project layers."));
    }

    @Test
    public void testUtilityClassNaming() {
        executeRule(
                "testUtilityClassNaming",
                classes()
                        .that()
                        .resideInAPackage("..util..")
                        .should()
                        .haveSimpleNameEndingWith("Util")
                        .because("Classes in the utility package should have names ending with 'Util'."));
    }

    @Test
    @Disabled
    public void gridrlShouldUseFoundationsOnly() {
        executeRule(
                "controllersShouldUseDTOsOnly",
                noClasses()
                        .that()
                        .resideInAPackage("..gridrl..")
                        .should()
                        .dependOnClassesThat()
                        .resideInAPackage("..foundation..")
                        .because(
                                "gridrl should use only foundation."));
    }

    private void executeRule(String ruleName, ArchRule rule) {
        log.info("Executing: {}"+ ruleName);
        try {
            rule.check(importedClasses);
            log.info("Completed: {}"+ ruleName);
        } catch (AssertionError e) {
            log.warning("Architecture Violation in {}: {}"+ ruleName+ e.getMessage());
            throw e;
        }
    }
    
}
