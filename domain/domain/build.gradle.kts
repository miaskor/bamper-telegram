dependencies {
  implementation(project(":domain:domain-api"))
  implementation(project(":cloud-drive"))
  implementation(project(":common"))

  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-jooq")
  implementation("org.springframework.boot:spring-boot-starter-data-rest")
  implementation("org.springframework:spring-webflux")

  implementation(lib("io.projectreactor.netty:reactor-netty"))
  implementation(lib("io.projectreactor:reactor-core"))

  implementation(lib("org.jooq:jooq-meta"))
  implementation(lib("org.jooq:jooq-codegen"))

  implementation("org.liquibase:liquibase-core")
  jooqGenerator(lib("mysql:mysql-connector-java"))

  runtimeOnly(lib("mysql:mysql-connector-java"))
  testImplementation(lib("org.junit.jupiter:junit-jupiter-api"))
  testRuntimeOnly(lib("org.junit.jupiter:junit-jupiter-engine"))
}

jooq {
  version.set("3.15.1")  // default (can be omitted)

  configurations {
    create("main") {  // name of the jOOQ configuration
      jooqConfiguration.apply {
        jdbc.apply {
          driver = project.driverClassName
          url = project.url
          user = project.username
          password = project.password
        }
        generator.apply {
          name = "org.jooq.codegen.KotlinGenerator"
          database.apply {
            name = "org.jooq.meta.mysql.MySQLDatabase"
            inputSchema = "bamper_db"
            includes = "brand|car|advertisement|bamper_client|car_part|auto_part|store_house|telegram_client" +
                "|worker_store_house|worker_telegram"
          }
          generate.apply {
            isDeprecated = false
            isRecords = true
            isImmutablePojos = true
            isFluentSetters = false
          }
          target.apply {
            packageName = "by.miaskor.domain"
            directory = "build/generated-src/jooq/main"  // default (can be omitted)
          }
          strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
        }
      }
    }
  }
}

dependencies {
  liquibaseRuntime(lib("org.liquibase:liquibase-core"))
  liquibaseRuntime(lib("org.liquibase:liquibase-groovy-dsl"))
  liquibaseRuntime(lib("info.picocli:picocli"))
  liquibaseRuntime(lib("mysql:mysql-connector-java"))
  liquibaseRuntime(lib("org.yaml:snakeyaml"))
  liquibaseRuntime(sourceSets.main.get().output)
}



liquibase {
  activities.register("main") {
    this.arguments = mapOf(
      "logLevel" to "info",
      "changeLogFile" to "db/changelog/db.changelog-master.yaml",
      "url" to url,
      "username" to username,
      "password" to password
    )
  }
}

