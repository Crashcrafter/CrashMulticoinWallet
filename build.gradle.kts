plugins {
    kotlin("jvm") version "1.5.20"
    application
}

group = "de.crash"
version = "0.1"

repositories {
    mavenCentral()
    mavenLocal()
	maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://dl.bintray.com/kotlin/ktor") }
}

val exposedVersion: String by project
dependencies {
	implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
	implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
	implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
	implementation("mysql:mysql-connector-java:8.0.25") //GPL 2 License
	implementation("org.mindrot:jbcrypt:0.4")
	implementation("org.apache.commons:commons-email:1.5")
	implementation("dev.crash:kryptoLib:0.1")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.4")
	implementation("com.github.kenglxn.QRGen:javase:2.6.0")
	implementation("io.ktor:ktor-server-netty:1.6.1")
	implementation("io.ktor:ktor-html-builder:1.6.1")
	implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.3")
    implementation(kotlin("stdlib"))
}

/*kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "16"
        }
    }
    js(IR) {
        browser {
            binaries.executable()
            webpackTask {
                cssSupport.enabled = true
            }
            runTask {
                cssSupport.enabled = true
            }
        }
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-server-netty:1.6.1")
                implementation("io.ktor:ktor-html-builder:1.6.1")
                implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.3")
            }
        }
    }
}*/

application {
    mainClass.set("de.crash.crashwallet.ServerKt")
}
/*
tasks.getByName<JavaExec>("run") {
    dependsOn(tasks.getByName<Jar>("jvmJar"))
    classpath(tasks.getByName<Jar>("jvmJar"))
}

*/