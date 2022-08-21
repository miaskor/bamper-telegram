package by.miaskor.plugin

import org.cfg4j.provider.ConfigurationProviderBuilder
import org.cfg4j.source.files.FilesConfigurationSource
import org.gradle.api.Plugin
import org.gradle.api.Project
import kotlin.io.path.Path

class MySQLCredentialsPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    val filesConfigurationSource = FilesConfigurationSource({
      listOf(
        Path(DOMAIN_PROPERTY_PATH)
      )
    })

    val configurationProvider = ConfigurationProviderBuilder()
      .withConfigurationSource(filesConfigurationSource)
      .build()

    val dataSourceSettings = configurationProvider.bind("bamper.datasource", DataSourceSettings::class.java)
    target.extensions.apply {
      add("driverClassName", dataSourceSettings.driverClassName())
      add("username", dataSourceSettings.username())
      add("password", dataSourceSettings.password())
      add("url", dataSourceSettings.url())
    }
  }

  private companion object {
    private const val COMMON_PATH = "/home/miaskor/Documents/pet-projects/bamper-telegram-properties/data-source"
    private const val DOMAIN_PROPERTY_PATH = "$COMMON_PATH/mysql.yaml"
  }
}
