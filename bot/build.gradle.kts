dependencies {
  implementation(lib("com.github.pengrad:java-telegram-bot-api"))
  implementation(lib("io.projectreactor:reactor-core"))
  implementation(lib("com.github.ben-manes.caffeine:caffeine"))
  implementation("org.springframework:spring-webflux")
  implementation(lib("org.jetbrains.kotlin:kotlin-reflect"))
  implementation(lib("io.projectreactor.netty:reactor-netty"))
  implementation(lib("io.projectreactor.kotlin:reactor-kotlin-extensions"))


  implementation(project(":domain:domain-api"))
  implementation(project(":bamper-integration:bamper-integration-api"))
}
