dependencies {
  implementation("org.springframework:spring-webflux")
  implementation("io.projectreactor.netty:reactor-netty:1.0.18")
  implementation(project(":bamper-integration:bamper-integration-api"))

}
