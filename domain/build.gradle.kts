dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.springframework.boot:spring-boot-starter-data-rest")
    implementation("org.liquibase:liquibase-core")
    implementation("org.jooq:jooq-meta:3.14.15")
    implementation("org.jooq:jooq-codegen:3.14.15")

    runtimeOnly("mysql:mysql-connector-java")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}
