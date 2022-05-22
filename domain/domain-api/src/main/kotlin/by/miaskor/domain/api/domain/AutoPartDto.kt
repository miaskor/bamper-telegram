package by.miaskor.domain.api.domain

import org.springframework.core.io.buffer.DataBuffer
import reactor.core.publisher.Flux

data class AutoPartDto(
  val description: String,
  val photo: Flux<DataBuffer>,
  val price: Double,
  val quality: Boolean,
  val currency: String,
  val partNumber: String,
  val carId: Long,
  val carPartId: Long,
  val chatId: Long,
  val storeHouseName: String
)
