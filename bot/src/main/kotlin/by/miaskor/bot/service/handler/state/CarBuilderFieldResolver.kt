package by.miaskor.bot.service.handler.state

import by.miaskor.bot.domain.CarBuilder
import by.miaskor.bot.domain.CreatingCarStep
import by.miaskor.bot.domain.CreatingCarStep.BODY
import by.miaskor.bot.domain.CreatingCarStep.BRAND_NAME
import by.miaskor.bot.domain.CreatingCarStep.ENGINE_CAPACITY
import by.miaskor.bot.domain.CreatingCarStep.ENGINE_TYPE
import by.miaskor.bot.domain.CreatingCarStep.FUEL_TYPE
import by.miaskor.bot.domain.CreatingCarStep.MODEL
import by.miaskor.bot.domain.CreatingCarStep.TRANSMISSION
import by.miaskor.bot.domain.CreatingCarStep.YEAR
import kotlin.reflect.KFunction1

object CarBuilderFieldResolver {

  fun resolve(creatingCarStep: CreatingCarStep, carBuilder: CarBuilder): KFunction1<String, CarBuilder> {
    return when (creatingCarStep) {
      BRAND_NAME -> carBuilder::brandName
      MODEL -> carBuilder::model
      YEAR -> carBuilder::year
      BODY -> carBuilder::body
      TRANSMISSION -> carBuilder::transmission
      ENGINE_CAPACITY -> carBuilder::engineCapacity
      FUEL_TYPE -> carBuilder::fuelType
      ENGINE_TYPE -> carBuilder::engineType
    }
  }
}
