package by.miaskor.cloud.drive.settings

interface CloudDriveSettings {
  fun authToken(): String
  fun baseUrl(): String
  fun getUploadedUri(): String
  fun createFolderUri(): String
  fun getDownloadedUri(): String
  fun folderInformationUri(): String
}
