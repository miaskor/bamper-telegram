package by.miaskor.domain.api.domain

data class EmployeeExistsRequest(
  val employerChatId: Long,
  val employeeUsername: String
)
