dependencies {
  implementation("org.springframework:spring-webflux")
  implementation(lib("io.projectreactor.netty:reactor-netty"))

  implementation(lib("org.dhatim:fastexcel"))


  implementation(project(":bamper-integration:bamper-integration-api"))
  implementation(project(":domain:domain-api"))
}
