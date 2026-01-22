package archunit;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
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
import java.util.Locale;
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
        log.info("Setup complete. Classes imported, nof = " + importedClasses.size());
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
    public void foundationLayerShouldBeIndependent() {
        executeRule(
                "foundationLayerShouldBeIndependent",
                classes()
                        .that()
                        .resideInAPackage("..foundation..")
                        .should()
                        .onlyDependOnClassesThat()
                        .resideInAnyPackage(
                                "java..",
                                "..api..",
                                "..lombok..",
                                "..foundation..",
                                "..com.google.common..",
                                "..org.apache.commons..",
                                "..org.jetbrains.annotations..",
                                "..tec.units.ri..",
                                "..javax.measure..")
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

    /**
     * In many chapter folders there are core sub folders, hence not .that().resideInPackage("..core..")
     */

    @Test
    public void coreSubFoldersShouldNotUseChapterClasses() {
        executeRule(
                "gridrlShouldUseFoundationsOnly",
                noClasses()
                        .that().resideInAnyPackage(
                                "..foundation..",
                                "..gridrl..",
                                "..learninggadgets..",
                                "..learningutils..",
                                "..nextlevelrl..",
                                "..plotting_core..",
                                "..plotting_rl..")
                        .should().dependOnClassesThat()
                        .resideInAnyPackage(
                                "chapters.."         // if you want to block chapter code
                        )
                        .because("gridrl should only depend on foundation (and external libs), not other project layers.")
        );
    }

    private void executeRule(String ruleName, ArchRule rule) {
        log.info("Executing: {}" + ruleName);
        try {
            rule.check(importedClasses);
            log.info("Completed: {}" + ruleName);
        } catch (AssertionError e) {
            log.warning("Architecture Violation in {}: {}" + ruleName + e.getMessage());
            throw e;
        }
    }

}
