package by.miaskor.bot.service.step.autopart.create

import by.miaskor.bot.domain.AbstractStepBuilder
import by.miaskor.bot.domain.AutoPartBuilder
import by.miaskor.bot.domain.CreatingAutoPartStep
import by.miaskor.bot.domain.CreatingAutoPartStep.AUTO_PART
import by.miaskor.bot.domain.CreatingAutoPartStep.CAR
import by.miaskor.bot.domain.CreatingAutoPartStep.PHOTO
import by.miaskor.bot.service.cache.TelegramClientCache
import by.miaskor.bot.service.extension.chatId
import by.miaskor.bot.service.extension.text
import by.miaskor.bot.service.step.StepValidator
import by.miaskor.domain.api.connector.CarConnector
import by.miaskor.domain.api.connector.CarPartConnector
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class CreatingAutoPartValidator(
  private val carConnector: CarConnector,
  private val carPartConnector: CarPartConnector,
  private val telegramClientCache: TelegramClientCache
) : StepValidator<CreatingAutoPartStep> {
  override fun validate(
    step: CreatingAutoPartStep,
    update: Update,
    stepBuilder: AbstractStepBuilder<CreatingAutoPartStep>
  ): Mono<Boolean> {
    val autoPartBuilder = stepBuilder as AutoPartBuilder
    return Mono.just(update.text)
      .filter(step::isAcceptable)
      .flatMap {
        when (step) {
          CAR -> {
            Mono.just(update.chatId)
              .flatMap(telegramClientCache::getTelegramClient)
              .flatMap { telegramClient ->
                carConnector.getByStoreHouseIdAndId(
                  telegramClient.currentStoreHouseId(),
                  update.text.toLong()
                )
              }
              .hasElement()
          }

          AUTO_PART -> {
            Mono.just(update.text)
              .flatMap(carPartConnector::getIdByName)
              .doOnNext { carPartId -> autoPartBuilder.carPartId(carPartId) }
              .hasElement()
          }

          PHOTO -> {
            Mono.justOrEmpty(update.message().photo())
              .map { true }
          }

          else -> Mono.just(true)
        }
      }.defaultIfEmpty(false)
  }
}