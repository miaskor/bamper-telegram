package by.miaskor.plugin

import org.cfg4j.provider.ConfigurationProviderBuilder
import org.cfg4j.source.files.FilesConfigurationSource
import org.gradle.api.Plugin
import org.gradle.api.Project
import kotlin.io.path.Path

private const val MYSQL_DATA_SOURCE_FILE_PATH_KEY = "mysqlCredentialFilePath"
private const val MYSQL_PROPERTY_FILE_PATH = "/bamper-telegram-properties/data-source/mysql.yaml"
private const val BINDING_DATA_SOURCE_KEY = "mysqlDataSourceBindingKey"
private const val DEFAULT_BINDING_DATA_SOURCE_KEY = "bamper.datasource"

class MySQLCredentialsPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    val previousDirOfProject = project.gradle.rootProject.projectDir.path.substringBeforeLast("/")

    //todo(add possibility somehow get value from the project or somewhere else )
    //now is using only default path
    val mysqlCredentialFilePath = project.properties[MYSQL_DATA_SOURCE_FILE_PATH_KEY]?.toString()
      ?: "$previousDirOfProject$MYSQL_PROPERTY_FILE_PATH"
    val filesConfigurationSource = FilesConfigurationSource {
      listOf(Path(mysqlCredentialFilePath))
    }

    val configurationProvider = ConfigurationProviderBuilder()
      .withConfigurationSource(filesConfigurationSource)
      .build()

    //todo(add possibility somehow get value from the project or somewhere else )
    //now is using only default path
    val bindingDataSourceKey = project.properties[BINDING_DATA_SOURCE_KEY]
      ?.toString() ?: DEFAULT_BINDING_DATA_SOURCE_KEY
    val dataSourceSettings = configurationProvider.bind(bindingDataSourceKey, DataSourceSettings::class.java)

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
