dependencies {
  implementation(project(":domain:domain-api"))
  implementation(project(":bamper-integration:bamper-integration-api"))
  implementation(project(":common"))

  implementation(lib("com.github.pengrad:java-telegram-bot-api"))
  implementation(lib("com.github.ben-manes.caffeine:caffeine"))

  implementation(lib("io.projectreactor:reactor-core"))
  implementation(lib("io.projectreactor.netty:reactor-netty"))
  implementation(lib("io.projectreactor.kotlin:reactor-kotlin-extensions"))

  implementation("org.springframework:spring-webflux")
  implementation(lib("org.jetbrains.kotlin:kotlin-reflect"))

}
