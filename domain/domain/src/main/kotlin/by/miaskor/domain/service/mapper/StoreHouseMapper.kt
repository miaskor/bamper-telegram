package by.miaskor.domain.service.mapper

import by.miaskor.domain.api.domain.StoreHouseDto
import by.miaskor.domain.tables.pojos.StoreHouse

class StoreHouseMapper {

  fun map(storeHouse: StoreHouse): StoreHouseDto {
    return StoreHouseDto(
      id = storeHouse.id ?: -1,
      chatId = storeHouse.telegramChatId ?: -1,
      name = storeHouse.storeHouseName ?: ""
    )
  }
}
