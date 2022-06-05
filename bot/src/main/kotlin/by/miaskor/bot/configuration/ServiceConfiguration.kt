package by.miaskor.bot.configuration

import by.miaskor.bot.domain.CreatingAutoPartStep
import by.miaskor.bot.domain.CreatingCarStep
import by.miaskor.bot.service.KeyboardBuilder
import by.miaskor.bot.service.step.ProcessingStepService
import by.miaskor.bot.service.step.autopart.AutoPartBuilderFieldEnricher
import by.miaskor.bot.service.step.autopart.CreatingAutoPartStepKeyboardBuilder
import by.miaskor.bot.service.step.autopart.CreatingAutoPartStepMessageResolver
import by.miaskor.bot.service.step.autopart.CreatingAutoPartValidator
import by.miaskor.bot.service.step.car.CarBuilderFieldEnricher
import by.miaskor.bot.service.step.car.CreatingCarStepKeyboardBuilder
import by.miaskor.bot.service.step.car.CreatingCarStepMessageResolver
import by.miaskor.bot.service.step.car.CreationCarStepValidation
import com.pengrad.telegrambot.TelegramBot
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ServiceConfiguration(
  private val telegramBot: TelegramBot,
  private val connectorConfiguration: ConnectorConfiguration,
  private val cacheConfiguration: CacheConfiguration
) {

  @Bean
  open fun processingCarStepService(): ProcessingStepService<CreatingCarStep> {
    return ProcessingStepService(
      creationCarStepValidation(),
      creatingCarStepMessageResolver(),
      carBuilderFieldEnricher(),
      creatingCarStepKeyboardBuilder(),
      cacheConfiguration.carBuilderCache(),
      telegramBot
    )
  }

  @Bean
  open fun processingAutoPartStepService(): ProcessingStepService<CreatingAutoPartStep> {
    return ProcessingStepService(
      creatingAutoPartValidator(),
      creatingAutoPartStepMessageResolver(),
      autoPartBuilderFieldEnricher(),
      creatingAutoPartStepKeyboardBuilder(),
      cacheConfiguration.autoPartBuilderCache(),
      telegramBot
    )
  }

  @Bean
  open fun creatingCarStepKeyboardBuilder(): CreatingCarStepKeyboardBuilder {
    return CreatingCarStepKeyboardBuilder(keyboardBuilder())
  }

  @Bean
  open fun creatingCarStepMessageResolver(): CreatingCarStepMessageResolver {
    return CreatingCarStepMessageResolver()
  }

  @Bean
  open fun carBuilderFieldEnricher(): CarBuilderFieldEnricher {
    return CarBuilderFieldEnricher()
  }

  @Bean
  open fun creationCarStepValidation(): CreationCarStepValidation {
    return CreationCarStepValidation(connectorConfiguration.brandConnector())
  }

  @Bean
  open fun creatingAutoPartStepKeyboardBuilder(): CreatingAutoPartStepKeyboardBuilder {
    return CreatingAutoPartStepKeyboardBuilder(keyboardBuilder())
  }

  @Bean
  open fun creatingAutoPartStepMessageResolver(): CreatingAutoPartStepMessageResolver {
    return CreatingAutoPartStepMessageResolver()
  }

  @Bean
  open fun autoPartBuilderFieldEnricher(): AutoPartBuilderFieldEnricher {
    return AutoPartBuilderFieldEnricher()
  }

  @Bean
  open fun creatingAutoPartValidator(): CreatingAutoPartValidator {
    return CreatingAutoPartValidator(
      connectorConfiguration.carConnector(),
      connectorConfiguration.carPartConnector(),
      cacheConfiguration.telegramClientCache()
    )
  }

  @Bean
  open fun keyboardBuilder(): KeyboardBuilder {
    return KeyboardBuilder(cacheConfiguration.telegramClientCache())
  }
}
