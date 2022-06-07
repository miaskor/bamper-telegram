package by.miaskor.bot.service.step.autopart.find

import by.miaskor.bot.domain.AbstractStepBuilder
import by.miaskor.bot.domain.FindAutoPartBuilder
import by.miaskor.bot.domain.FindingAutoPartStep
import by.miaskor.bot.service.cache.TelegramClientCache
import by.miaskor.bot.service.extension.chatId
import by.miaskor.bot.service.extension.text
import by.miaskor.bot.service.step.StepValidator
import by.miaskor.domain.api.connector.AutoPartConnector
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class FindingAutoPartValidator(
  private val autoPartConnector: AutoPartConnector,
  private val findAutoPartBuilderFieldEnricher: FindAutoPartBuilderFieldEnricher,
  private val telegramClientCache: TelegramClientCache
) : StepValidator<FindingAutoPartStep> {
  override fun validate(
    step: FindingAutoPartStep,
    update: Update,
    stepBuilder: AbstractStepBuilder<FindingAutoPartStep>
  ): Mono<Boolean> {
    val findAutoPartBuilder = stepBuilder as FindAutoPartBuilder
    return Mono.just(update.text)
      .filter(step::isAcceptable)
      .doOnNext { findAutoPartBuilderFieldEnricher.enrich(update, step, stepBuilder) }
      .flatMap { telegramClientCache.getTelegramClient(update.chatId) }
      .map { telegramClient -> findAutoPartBuilder.build(telegramClient.currentStoreHouseId()) }
      .flatMap { findAutoPartDto -> autoPartConnector.isExistsByProperties(findAutoPartDto) }
      .defaultIfEmpty(false)
  }
}