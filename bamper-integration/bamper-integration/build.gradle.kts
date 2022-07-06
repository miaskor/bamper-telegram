dependencies {
  implementation("org.springframework:spring-webflux")
  implementation("io.projectreactor.netty:reactor-netty:1.0.18")
  implementation("org.dhatim:fastexcel:0.12.15")
  implementation(project(":bamper-integration:bamper-integration-api"))

}
