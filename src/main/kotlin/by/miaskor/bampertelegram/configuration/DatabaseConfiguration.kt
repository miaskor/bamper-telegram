package by.miaskor.bampertelegram.configuration

import by.miaskor.bampertelegram.configuration.settings.DataSourceSettings
import com.zaxxer.hikari.HikariDataSource
import org.hibernate.dialect.MySQL5Dialect
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DataSourceConnectionProvider
import org.jooq.impl.DefaultConfiguration
import org.jooq.impl.DefaultDSLContext
import org.jooq.impl.DefaultExecuteListener
import org.jooq.impl.DefaultExecuteListenerProvider
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import javax.sql.DataSource

@Configuration
class DatabaseConfiguration(
  private val dataSourceSettings: DataSourceSettings
) {

  @Bean
  @Primary
  fun dataSource(): DataSource {
    return DataSourceBuilder.create()
      .driverClassName(dataSourceSettings.driverClassName())
      .username(dataSourceSettings.username())
      .password(dataSourceSettings.password())
      .url(dataSourceSettings.url())
      .type(HikariDataSource::class.java)
      .build()
  }

  @Bean
  fun connectionProvider(): DataSourceConnectionProvider {
    return DataSourceConnectionProvider(TransactionAwareDataSourceProxy(dataSource()))
  }

  @Bean
  fun jooqConfiguration(): DefaultConfiguration {
    return DefaultConfiguration().apply {
      set(connectionProvider())
      set(DefaultExecuteListenerProvider(DefaultExecuteListener()))
      setDataSource(dataSource())
    }
  }

  @Bean
  fun dslContext(): DSLContext {
    return DefaultDSLContext(jooqConfiguration())
  }
}
