package archunit;

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

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@Log
public class ArchitectureTest {


    protected static JavaClasses importedClasses;

    @BeforeAll
    public static void setUp() throws IOException {
        log.info("Setting up the architecture test environment...");
        Path mainClasses = Paths.get(System.getProperty("user.dir"), "target", "classes");

        importedClasses = new ClassFileImporter()
                .importPath(mainClasses);

        //importedClasses = new ClassFileImporter().importClasspath();
        /*importedClasses =
                new ClassFileImporter()
                        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                        .importPaths(getAllModuleClassPaths());*/
        log.info("Setup complete. Classes imported, nof ="+ importedClasses.size());
        log.info("Classes imported="+ importedClasses);
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
    @Disabled
    public void foundationLayerShouldBeIndependent() {
        executeRule(
                "apiLayerShouldBeIndependent",
                classes()
                        .that()
                        .resideInAPackage("..foundation..")
                        .should()
                        .onlyDependOnClassesThat()
                        .resideInAnyPackage("java..", "..api..")
                        .because("The foundation layer should depend on other layers."));
    }

    @Test
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
