package by.miaskor.domain

import by.miaskor.domain.repository.BrandRepository
import by.miaskor.domain.tables.pojos.Brand
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class BamperTelegramApplication

fun main(args: Array<String>) {
  val runApplication = runApplication<BamperTelegramApplication>(*args)
  val bean = runApplication.getBean("brandRepository")
  bean as BrandRepository
  bean.save(
    Brand(
      brandName = "misha", yearManufactureFrom = "2022", yearManufactureTo = "2023",
      model = "xxs"
    )
  )
}
