package by.miaskor.domain.configuration

import by.miaskor.domain.configuration.settings.DataSourceSettings
import com.zaxxer.hikari.HikariDataSource
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.conf.RenderQuotedNames
import org.jooq.conf.Settings
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
open class DatabaseConfiguration(
  private val dataSourceSettings: DataSourceSettings
) {

  @Bean
  @Primary
  open fun dataSource(): DataSource {
    return DataSourceBuilder.create()
      .driverClassName(dataSourceSettings.driverClassName())
      .username(dataSourceSettings.username())
      .password(dataSourceSettings.password())
      .url(dataSourceSettings.url())
      .type(HikariDataSource::class.java)
      .build()
  }

  @Bean
  open fun connectionProvider(): DataSourceConnectionProvider {
    return DataSourceConnectionProvider(TransactionAwareDataSourceProxy(dataSource()))
  }

  @Bean
  open fun jooqConfiguration(): DefaultConfiguration {
    val settings = Settings().withRenderQuotedNames(RenderQuotedNames.EXPLICIT_DEFAULT_UNQUOTED)

    return DefaultConfiguration().apply {
      set(connectionProvider())
      set(DefaultExecuteListenerProvider(DefaultExecuteListener()))
      set(SQLDialect.MYSQL)
      setDataSource(dataSource())
      setSettings(settings)
    }
  }

  @Bean
  open fun dslContext(): DSLContext {
    return DefaultDSLContext(jooqConfiguration())
  }
}
