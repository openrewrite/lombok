plugins {
    id ("com.netflix.nebula.maven-nebula-publish") version("latest.release")
    signing
}

group = "org.openrewrite.tools"
version = "1.18.37"

publishing {
    repositories {
        maven {
            name = "OSSRH"
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")

            credentials {
                username = findProperty("sonatypeUsername") as String? ?: System.getenv("OSSRH_USERNAME")
                password = findProperty("sonatypePassword") as String? ?: System.getenv("OSSRH_PASSWORD")
            }
        }
    }
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
                description = "Temporary pending the next stable release of lombok"
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

