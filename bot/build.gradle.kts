dependencies {
  implementation("com.github.pengrad:java-telegram-bot-api:6.0.0")
  implementation("io.projectreactor:reactor-core:3.4.17")
  implementation("com.github.ben-manes.caffeine:caffeine:3.1.0")
  implementation("org.springframework:spring-webflux")
  implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.21")
  implementation("io.projectreactor.netty:reactor-netty:1.0.18")

  implementation(project(":domain:domain-api"))
}
