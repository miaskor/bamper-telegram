package by.miaskor.bot.service.carstep

import by.miaskor.bot.domain.CarBuilder
import by.miaskor.bot.domain.CreatingCarStep
import by.miaskor.bot.domain.CreatingCarStep.BRAND_NAME
import by.miaskor.bot.domain.CreatingCarStep.MODEL
import by.miaskor.domain.api.connector.BrandConnector
import by.miaskor.domain.api.domain.BrandDto
import reactor.core.publisher.Mono

class CreationCarStepValidation(
  private val brandConnector: BrandConnector
) {

  fun validate(creatingCarStep: CreatingCarStep, message: String, carBuilder: CarBuilder): Mono<Boolean> {
    return Mono.just(message)
      .filter(creatingCarStep::isAcceptable)
      .flatMap {
        when (creatingCarStep) {
          BRAND_NAME -> {
            Mono.just(message)
              .flatMap(brandConnector::getByBrandName)
              .hasElement()
          }

          MODEL -> {
            Mono.fromSupplier {
              BrandDto(
                brandName = carBuilder.getBrandName(),
                model = message
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
