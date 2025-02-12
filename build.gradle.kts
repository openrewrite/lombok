plugins {
    id ("com.netflix.nebula.maven-publish") version("latest.release")
    id("io.github.gradle-nexus.publish-plugin") version("latest.release")
    signing
}

group = "org.openrewrite.tools"
version = "1.18.37"
description = "Temporary pending the next stable release of lombok"

nexusPublishing {
    repositories {
        sonatype()
    }
}

publishing {
    publications {
        named<MavenPublication>("nebula") {
            artifact(file("libs/lombok-1.18.37.jar")) {
                classifier = null
            }
            artifact(file("libs/lombok-1.18.37-sources.jar")) {
                classifier = "sources"
            }
            artifact(file("libs/lombok-1.18.37-javadoc.jar")) {
                classifier = "javadoc"
            }
            pom {
                name = project.name
                url = "https://github.com/openrewrite/lombok"
                licenses {
                    license {
                        name = "The Apache License, Version 2.0"
                        url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                    }
                }
                developers {
                    developer {
                        id = "moderne"
                        name = "Moderne"
                        email = "team@moderne.io"
                    }
                }
                scm {
                    url = "https://github.com/openrewrite/lombok"
                }
            }
        }
    }
}

if (project.hasProperty("releasing")) {
    signing {
        useInMemoryPgpKeys(
            project.findProperty("signingKey") as String,
            project.findProperty("signingPassword") as String
        )
        sign(publishing.publications.named("nebula").get())
    }
}

tasks.register("final") {
    description = "Our github automation expects a task with this name to exist"
}
