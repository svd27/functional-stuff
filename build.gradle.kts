import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.junit.platform.engine.TestSource
import org.junit.platform.gradle.plugin.JUnitPlatformExtension

val kotlin_version: String by extra
buildscript {
    var kotlin_version: String by extra
    repositories {
        jcenter()
        mavenCentral()
        maven("https://dl.bintray.com/kotlin/kotlin-eap/")
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.2.30")
        classpath("org.junit.platform:junit-platform-gradle-plugin:1.1.0")
        classpath("com.kncept.junit5.reporter:junit-reporter:1.1.0")
    }
}


plugins {
    id("ch.netzwerg.release") version "1.2.3"
    maven
}

apply {
    plugin("kotlin-platform-common")
}

dependencies {
    "compile"(kotlin("stdlib-common"))
    "compile"("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:0.22.5")
    "testCompile"(kotlin("test-annotations-common"))
    "testCompile"(kotlin("test-common"))
}

maven {
    pom().whenConfigured {
        dependencies = dependencies.filter { d ->
            d.toString().contains("functional-stuff")
        }
    }
}

allprojects {
    group = "info.kinterest.functional"
    apply {
        plugin("maven")
        plugin("maven-publish")
    }
    repositories {
        mavenCentral()
    }
}


val jvm by extra {
    subprojects.filter {
        it.name.endsWith("jvm")
    }
}

configure(jvm) {
    apply {
        plugin("kotlin-platform-jvm")
        plugin("org.junit.platform.gradle.plugin")
        maven
    }
    val compileKotlin: KotlinCompile by tasks
    compileKotlin.kotlinOptions {
        jvmTarget = "1.8"
    }
    val compileTestKotlin: KotlinCompile by tasks
    compileTestKotlin.kotlinOptions {
        jvmTarget = "1.8"
    }



    configure<JUnitPlatformExtension> {
        filters {
            engines {
                include("junit-jupiter")
                include("spek")
            }
            includeClassNamePattern("Test.*")
        }

    }

    dependencies {
        "compile"(kotlin("reflect"))
        "expectedBy"(rootProject)
        "compile"(kotlin("stdlib-jdk8"))
        "testCompile"("org.jetbrains.spek:spek-api:1.1.5") {
            exclude("org.jetbrains.kotlin")
        }
        "testRuntime"("org.jetbrains.spek:spek-junit-platform-engine:1.1.5") {
            exclude("org.jetbrains.kotlin")
        }
        "testCompile"("org.junit.jupiter:junit-jupiter-api:5.1.0")
        "testRuntime"("org.junit.jupiter:junit-jupiter-engine:5.1.0")
    }
}


val js by extra {
    subprojects.filter {
        it.name.endsWith("js")
    }
}

configure(js) {
    apply {
        plugin("kotlin-platform-js")
    }
    dependencies {
        "expectedBy"(rootProject)
        "compile"(kotlin("stdlib-js"))
    }
}
