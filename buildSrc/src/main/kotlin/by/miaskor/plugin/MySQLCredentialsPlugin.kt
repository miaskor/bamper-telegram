package by.miaskor.plugin

import org.cfg4j.provider.ConfigurationProviderBuilder
import org.cfg4j.source.files.FilesConfigurationSource
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File
import kotlin.io.path.Path

private const val MYSQL_DATA_SOURCE_FILE_PATH_KEY = "mysqlCredentialFilePath"
private const val BINDING_DATA_SOURCE_KEY = "mysqlDataSourceBindingKey"
private const val DEFAULT_BINDING_DATA_SOURCE_KEY = "bamper.datasource"

class MySQLCredentialsPlugin : Plugin<Project> {

  override fun apply(project: Project) {
    val rootParent = project.rootProject.projectDir.parentFile

    val mysqlCredentialFilePath =
      project.findProperty(MYSQL_DATA_SOURCE_FILE_PATH_KEY)?.toString()
        ?: File(rootParent, "bamper-telegram-properties/data-source/mysql.yaml").absolutePath

    require(File(mysqlCredentialFilePath).exists()) {
      "MySQL credentials file not found: $mysqlCredentialFilePath"
    }

    val configurationProvider = ConfigurationProviderBuilder()
      .withConfigurationSource(
        FilesConfigurationSource {
          listOf(Path(mysqlCredentialFilePath))
        }
      )
      .build()

    val bindingKey =
      project.findProperty(BINDING_DATA_SOURCE_KEY)?.toString()
        ?: DEFAULT_BINDING_DATA_SOURCE_KEY

    val dataSourceSettings =
      configurationProvider.bind(bindingKey, DataSourceSettings::class.java)

    project.extensions.apply {
      add("driverClassName", dataSourceSettings.driverClassName())
      add("username", dataSourceSettings.username())
      add("password", dataSourceSettings.password())
      add("url", dataSourceSettings.url())
    }
  }
}

fun Project.mysqlDataSource(closure: (Project) -> Unit) {
  closure(this)
}
