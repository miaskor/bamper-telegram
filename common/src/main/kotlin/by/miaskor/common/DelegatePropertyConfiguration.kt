package by.miaskor.common

import org.cfg4j.provider.ConfigurationProvider
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
open class DelegatePropertyConfiguration(
  private val configurationProvider: ConfigurationProvider,
) {
  @PostConstruct
  fun init() {
    Property.configurationProvider = configurationProvider
  }
}
