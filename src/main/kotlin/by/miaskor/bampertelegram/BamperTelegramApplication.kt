package by.miaskor.bampertelegram

import org.jooq.DSLContext
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BamperTelegramApplication

fun main(args: Array<String>) {
  val runApplication = runApplication<BamperTelegramApplication>(*args)
  val bean = runApplication.getBean(DSLContext::class.java)
  val into = bean.selectFrom("brand")
    .where("brand_name = 'Acura'")
    .fetch()
    .into(Brand::class.java)
  println(into)
}

data class Brand(
  val id: Long,
  val brandName: String,
  val model: String,
  val yearManufactureFrom: String,
  val yearManufactureTo: String
)
