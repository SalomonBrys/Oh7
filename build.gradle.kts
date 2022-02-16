plugins {
    id("org.asciidoctor.jvm.convert") version "3.3.2"
    id("org.asciidoctor.jvm.pdf") version "3.3.2"
}

repositories {
    mavenCentral()
}

tasks.create<Copy>("htmlSources") {
    from("rules")
    from("README.adoc") {
        eachFile { name = "index.adoc" }
    }

    from("fonts") {
        into("fonts")
    }

    into("build/adoc-html")
}

tasks.asciidoctor {
    dependsOn("htmlSources")

    setSourceDir("build/adoc-html")
    setOutputDir("docs")
    baseDirFollowsSourceDir()
}

tasks.asciidoctorPdf {
    dependsOn("asciidoctor")
    dependsOn("cards")

    setSourceDir("rules")
    setOutputDir("docs")

    setTheme("pdf")

    baseDirFollowsSourceDir()
    setFontsDirs(listOf("fonts"))
}

asciidoctorj {
    attribute("docinfo", "shared")
    attribute("stem", "")
}

pdfThemes {
    local("pdf") {
        themeDir = projectDir
    }
}

tasks.create<Sync>("fonts") {
    from("fonts")
    into("docs/fonts")
}

tasks.create<Copy>("cards") {
    from("design/Cards-A4.pdf")
    into("docs")
}

tasks.named("build") {
    dependsOn("cards")
    dependsOn("fonts")
    dependsOn("asciidoctor")
    dependsOn("asciidoctorPdf")
}