dependencies {
  implementation("org.springframework:spring-webflux")
  implementation(lib("io.projectreactor.netty:reactor-netty"))
  implementation(project(":bamper-integration:bamper-integration-api"))

}
