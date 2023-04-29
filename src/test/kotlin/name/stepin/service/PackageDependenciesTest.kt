package name.stepin.service

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import name.stepin.DependenciesTestUtils.basePackage
import name.stepin.DependenciesTestUtils.genericPackages
import org.junit.jupiter.api.Test

class PackageDependenciesTest {

    @Test
    fun `service package should depend only on other services, client, db, dao, and utils`() {
        val thisPackage = this.javaClass.packageName
        val packageClasses = ClassFileImporter()
            .withImportOption(ImportOption.DoNotIncludeTests())
            .importPackages(thisPackage)

        val rule = classes()
            .should().onlyDependOnClassesThat().resideInAnyPackage(
                *genericPackages.toTypedArray(),
                "$thisPackage..",
                "$basePackage.utils..",
                "$basePackage.client..",
                "$basePackage.db..",
                "$basePackage.dao.."
            )

        rule.check(packageClasses)
    }
}
