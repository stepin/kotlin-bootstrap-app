import org.jetbrains.dokka.gradle.DokkaTask
import org.jooq.meta.jaxb.Logging
import java.net.URL

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    kotlin("jvm") version libs.versions.kotlin
    kotlin("plugin.allopen") version libs.versions.kotlin
    id("io.quarkus")
    alias(libs.plugins.kover)
    alias(libs.plugins.dokka)
    alias(libs.plugins.sonar)
    alias(libs.plugins.jooq)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.axion.release)
    alias(libs.plugins.task.tree)
}

repositories {
    mavenCentral()
    mavenLocal()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    // basic deps
    implementation(enforcedPlatform("$quarkusPlatformGroupId:$quarkusPlatformArtifactId:$quarkusPlatformVersion"))
    implementation("io.quarkus:quarkus-arc")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.quarkus:quarkus-kotlin")
    implementation("io.quarkus:quarkus-config-yaml")
    implementation("io.quarkus:quarkus-container-image-jib")
    implementation("io.quarkus:quarkus-hibernate-validator")

    // Postgres
    implementation("io.quarkus:quarkus-flyway")
    // NOTE: jOOQ depends on agroal -- i.e. JDBC, no reactive
    implementation("io.quarkus:quarkus-agroal")
    implementation("io.quarkus:quarkus-jdbc-postgresql")

    // Panache
    implementation("io.quarkus:quarkus-hibernate-reactive-panache-kotlin")

    // jOOQ
    implementation(libs.jooq.quarkus)
    implementation(libs.jooq.meta)
    implementation(libs.jooq.codegen)
    jooqGenerator("org.postgresql:postgresql:42.5.1")

    // REST server
    implementation("io.quarkus:quarkus-resteasy-reactive")
    implementation("io.quarkus:quarkus-resteasy-reactive-jackson")

    // REST client
    implementation("io.quarkus:quarkus-rest-client-reactive")
    implementation("io.quarkus:quarkus-rest-client-reactive-jackson")

    // External information: health, metrics, apis
    implementation("io.quarkus:quarkus-smallrye-health")
    implementation("io.quarkus:quarkus-smallrye-openapi")
    implementation("io.quarkus:quarkus-micrometer")
    implementation("io.quarkus:quarkus-micrometer-registry-prometheus")
    implementation("io.quarkus:quarkus-smallrye-graphql")

    // Kotlin logging
    implementation("org.jboss.logmanager:log4j2-jboss-logmanager")
    implementation(libs.log4j.kotlin)

    implementation("io.quarkus:quarkus-jacoco")
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
    testImplementation(libs.mockk.quarkus)
    testImplementation(libs.archunit)
    testImplementation(libs.testcontainers.postgresql)
    testImplementation(libs.testcontainers.jupiter)
}

group = "name.stepin"
version = scmVersion.version

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<Test> {
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}
allOpen {
    annotation("jakarta.ws.rs.Path")
    annotation("jakarta.enterprise.context.ApplicationScoped")
    annotation("io.quarkus.test.junit.QuarkusTest")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
    kotlinOptions.javaParameters = true
}

jooq {
    configurations {
        create("main") {
            jooqConfiguration.apply {
                logging = Logging.WARN
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = "jdbc:postgresql://127.0.0.1:55000/kotlin-bootstrap-app_dev"
                    user = "kotlin-bootstrap-app"
                    password = "SomeP2assword!@e"
                }
                generator.apply {
                    name = "org.jooq.codegen.KotlinGenerator"
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                    }
                    generate.apply {
                        isDeprecated = false
                        isRecords = true
                        isImmutablePojos = true
                        isFluentSetters = true
                    }
                    target.apply {
                        packageName = "name.stepin.db.sql"
                        directory = "build/generated-src/jooq/main"
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}

tasks.withType<DokkaTask>().configureEach {
    dokkaSourceSets {
        named("main") {
            // used as project name in the header
            moduleName.set("Kotlin Bootstrap App")

            // contains descriptions for the module and the packages
            // more info: https://kotlinlang.org/docs/dokka-module-and-package-docs.html
            includes.from("packages.md")

            // adds source links that lead to this repository, allowing readers
            // to easily find source code for inspected declarations
            val repo =
                "http://localhost:3000/stepin/kotlin-bootstrap-app/src/branch/main/src/main/kotlin"
            sourceLink {
                localDirectory.set(file("src/main/kotlin"))
                remoteUrl.set(URL(repo))
                remoteLineSuffix.set("#L")
            }
        }
    }
}

kover {
    engine.set(kotlinx.kover.api.DefaultJacocoEngine)
}
tasks.test {
    finalizedBy(tasks.koverXmlReport) // report is always generated after tests run
}
tasks.koverXmlReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
}

sonarqube {
    properties {
        property("sonar.coverage.jacoco.xmlReportPaths", "build/jacoco-report/jacoco.xml")
        property("sonar.host.url", "http://localhost:9000")
        property("sonar.projectKey", "kotlin-bootstrap-app")
        property("sonar.projectName", "kotlin-bootstrap-app")
        property("sonar.token", "sqp_ddd268b2f87eab096b4785ebdc2d0b00d2732ed2")
    }
}
tasks.sonar {
    dependsOn(tasks.test) // tests are required to run before generating the report
}
