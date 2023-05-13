import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.meta.jaxb.Logging
import org.yaml.snakeyaml.Yaml
import java.net.URL

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("org.springframework.boot") version libs.versions.springBoot
    id("io.spring.dependency-management") version libs.versions.springDependencyManagement
//    id("org.graalvm.buildtools.native") version libs.versions.graalvmNative
    kotlin("jvm") version libs.versions.kotlin
    kotlin("plugin.spring") version libs.versions.kotlin
    alias(libs.plugins.kover)
    alias(libs.plugins.dokka)
    alias(libs.plugins.sonar)
    alias(libs.plugins.flyway)
    alias(libs.plugins.jooq)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.axion.release)
    alias(libs.plugins.jib)
    alias(libs.plugins.task.tree)
}

group = "name.stepin"
version = scmVersion.version
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

buildscript {
    dependencies {
        classpath("org.yaml:snakeyaml:2.0")
    }
}

repositories {
    mavenCentral()
}

extra["testcontainersVersion"] = "1.18.0"

dependencies {
    // basic deps
    implementation("org.springframework.boot:spring-boot-starter-validation")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // Kotlin deps
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8")
    implementation(libs.log4j.kotlin)

    // Postgres
    implementation("org.postgresql:postgresql")
    implementation("org.postgresql:r2dbc-postgresql")
    implementation("org.springframework:spring-jdbc")
    implementation("org.flywaydb:flyway-core")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.jooq:jooq-kotlin")
    implementation("org.jooq:jooq-kotlin-coroutines")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    jooqGenerator("org.postgresql:postgresql:42.5.1")

    // HTTP server
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-graphql")

    // External information: health, metrics, apis
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
    implementation(libs.springdoc.webflux.ui)

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.graphql:spring-graphql-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:r2dbc")
    testImplementation(libs.mockk.spring)
    testImplementation(libs.archunit)
    testImplementation(libs.wiremock)
}

dependencyManagement {
    imports {
        mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    systemProperty("spring.profiles.active", "test")
}

val cfg: Map<String, Map<String, Map<String, String>>> = Yaml().load(
    File("src/main/resources/application.yml").bufferedReader(),
)
val jdbcCfg: Map<String, String> = cfg["spring"]?.get("datasource") ?: emptyMap()
val r2dbcCfg: Map<String, String> = cfg["spring"]?.get("r2dbc") ?: emptyMap()

flyway {
    url = r2dbcCfg["url"]
    user = r2dbcCfg["username"]
    password = r2dbcCfg["password"]
    schemas = arrayOf("public")
}

jooq {
    configurations {
        create("main") {
            jooqConfiguration.apply {
                logging = Logging.WARN
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = jdbcCfg["url"]
                    user = jdbcCfg["username"]
                    password = jdbcCfg["password"]
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

sonar {
    properties {
        property("sonar.coverage.jacoco.xmlReportPaths", "build/reports/kover/xml/report.xml")
        property("sonar.host.url", "http://localhost:9000")
        property("sonar.projectKey", "kotlin-bootstrap-app")
        property("sonar.projectName", "kotlin-bootstrap-app")
        property("sonar.token", "sqp_821b1d3209761625bdd29259674237d429bce626")
    }
}
tasks.sonar {
    dependsOn(tasks.test) // tests are required to run before generating the report
}

ktlint {
    filter {
        exclude { element ->
            element.file.path.contains("generated-src")
        }
    }
}

// https://github.com/GoogleContainerTools/jib/tree/master/jib-gradle-plugin
jib {
    from {
        image = "azul/zulu-openjdk:17-jre"
    }
    to {
        image = "stepin/kotlin-bootstrap-app"
        tags = setOf("$version")
    }
    container {
        labels.set(
            mapOf(
                "org.opencontainers.image.title" to "kotlin-bootstrap-app",
                "org.opencontainers.image.version" to "$version",
            ),
        )
        creationTime.set("USE_CURRENT_TIMESTAMP")
        ports = listOf("8080", "8081")
    }
}
