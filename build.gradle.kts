plugins {
//    id("java")
    java
    application
}
//application {
//    mainClass.set("TranslatorText")
//}
group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.google.code.gson:gson:2.9.0")
}



tasks.test {
    useJUnitPlatform()
}