package by.miaskor.bot.service.step.car

import by.miaskor.bot.domain.AbstractStepBuilder
import by.miaskor.bot.domain.CarBuilder
import by.miaskor.bot.domain.CreatingCarStep
import by.miaskor.bot.domain.CreatingCarStep.BRAND_NAME
import by.miaskor.bot.domain.CreatingCarStep.MODEL
import by.miaskor.bot.service.extension.text
import by.miaskor.bot.service.step.StepValidator
import by.miaskor.domain.api.connector.BrandConnector
import by.miaskor.domain.api.domain.BrandDto
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class CreationCarStepValidation(
  private val brandConnector: BrandConnector
) : StepValidator<CreatingCarStep> {
  override fun validate(
    step: CreatingCarStep,
    update: Update,
    stepBuilder: AbstractStepBuilder<CreatingCarStep>
  ): Mono<Boolean> {
    val carBuilder = stepBuilder as CarBuilder
    return Mono.just(update.text)
      .filter(step::isAcceptable)
      .flatMap {
        when (step) {
          BRAND_NAME -> {
            Mono.just(update.text)
              .flatMap(brandConnector::getByBrandName)
              .hasElement()
          }

          MODEL -> {
            Mono.fromSupplier {
              BrandDto(
                brandName = carBuilder.getBrandName(),
                model = update.text
              )
            }.flatMap(brandConnector::getByBrandNameAndModel)
              .doOnNext { carBuilder.brandId(it.id) }
              .hasElement()
          }

          else -> Mono.just(true)
        }
      }.defaultIfEmpty(false)
  }
}
