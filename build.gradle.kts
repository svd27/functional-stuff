import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
    }
    val compileKotlin: KotlinCompile by tasks
    compileKotlin.kotlinOptions {
        jvmTarget = "1.8"
    }
    val compileTestKotlin: KotlinCompile by tasks
    compileTestKotlin.kotlinOptions {
        jvmTarget = "1.8"
    }

    dependencies {
        "expectedBy"(rootProject)
        "compile"(kotlin("stdlib-jdk8"))
        "testCompile"("org.junit.jupiter:junit-jupiter-api:5.1.0")
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
