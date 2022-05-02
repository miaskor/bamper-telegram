package by.miaskor.domain.configuration

import by.miaskor.domain.controller.TelegramClientController
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ControllerConfiguration(
  private val serviceConfiguration: ServiceConfiguration
) {


}
