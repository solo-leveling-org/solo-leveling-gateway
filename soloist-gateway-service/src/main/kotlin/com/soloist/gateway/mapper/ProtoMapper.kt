package com.soloist.gateway.mapper

import com.google.protobuf.Timestamp
import com.google.type.Money
import com.soloist.gateway.extensions.toOffsetDateTime
import com.soloist.gateway.extensions.toBigDecimal
import com.soloist.gateway.extensions.toTimestamp
import com.soloist.gateway.model.UserData
import com.soloist.proto.player.*
import com.soloist.proto.user.GetUserAdditionalInfoResponse
import com.soloist.proto.user.GetUserLeaderboardRequest
import com.soloist.proto.user.GetUserLeaderboardResponse
import com.soloist.proto.user.GetUsersLeaderboardRequest
import com.soloist.proto.user.GetUsersLeaderboardResponse
import com.soloist.proto.user.UserInput
import com.soloist.proto.user.UserRole
import com.soloist.proto.user.UserView
import com.soloist.gateway.dto.RestAssessment
import com.soloist.gateway.dto.RestCompleteTaskResponse
import com.soloist.gateway.dto.RestDayRange
import com.soloist.gateway.dto.RestEnumFilter
import com.soloist.gateway.dto.RestGetActiveTasksResponse
import com.soloist.gateway.dto.RestGetDailyTasksResponse
import com.soloist.gateway.dto.RestGetMonthlyActivityResponse
import com.soloist.gateway.dto.RestGetPlayerBalanceResponse
import com.soloist.gateway.dto.RestGetPlayerTopicsResponse
import com.soloist.gateway.dto.RestGetUserLeaderboardResponse
import com.soloist.gateway.dto.RestGetUsersLeaderboardResponse
import com.soloist.gateway.dto.RestLeaderboardType
import com.soloist.gateway.dto.RestLocalizedField
import com.soloist.gateway.dto.RestMoney
import com.soloist.gateway.dto.RestPlayer
import com.soloist.gateway.dto.RestPlayerBalance
import com.soloist.gateway.dto.RestPlayerBalanceTransactionCause
import com.soloist.gateway.dto.RestPlayerBalanceTransactionType
import com.soloist.gateway.dto.RestPlayerTask
import com.soloist.gateway.dto.RestPlayerTaskStatus
import com.soloist.gateway.dto.RestRequestQueryOptions
import com.soloist.gateway.dto.RestSavePlayerTopicsRequest
import com.soloist.gateway.dto.RestSearchPlayerBalanceTransactionsResponse
import com.soloist.gateway.dto.RestSearchPlayerTasksResponse
import com.soloist.gateway.dto.RestTaskRarity
import com.soloist.gateway.dto.RestTaskTopic
import com.soloist.gateway.dto.RestUser
import com.soloist.gateway.dto.RestUserAdditionalInfoResponse
import com.soloist.gateway.dto.RestUserRole
import org.mapstruct.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.OffsetDateTime

@Mapper(
	componentModel = "spring",
	unmappedTargetPolicy = ReportingPolicy.IGNORE,
	collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
	nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT,
	nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
abstract class ProtoMapper {

	fun map(input: UserRole): RestUserRole = RestUserRole.valueOf(input.name)

	fun map(input: Assessment): RestAssessment = RestAssessment.valueOf(input.name)

	fun map(input: PlayerTaskStatus): RestPlayerTaskStatus = RestPlayerTaskStatus.valueOf(input.name)

	fun map(input: TaskRarity): RestTaskRarity = RestTaskRarity.valueOf(input.name)

	fun map(input: TaskTopic): RestTaskTopic = RestTaskTopic.valueOf(input.name)

	fun map(input: PlayerBalanceTransactionType): RestPlayerBalanceTransactionType =
		RestPlayerBalanceTransactionType.valueOf(input.name)

	fun map(input: PlayerBalanceTransactionCause): RestPlayerBalanceTransactionCause =
		RestPlayerBalanceTransactionCause.valueOf(input.name)

	fun map(input: Timestamp): OffsetDateTime? = input.toOffsetDateTime()

	@Mapping(target = "username", source = "tag")
	abstract fun map(input: UserData): UserInput

	abstract fun map(input: UserView): RestUser

	@Mapping(target = "taskTopics", source = "taskTopicsList")
	abstract fun map(input: PlayerView): RestPlayer

	abstract fun map(input: PlayerBalanceView): RestPlayerBalance

	fun map(input: Money): RestMoney = RestMoney()
		.currencyCode(input.currencyCode)
		.amount(input.toBigDecimal())

	fun mapDecimal(input: Money): BigDecimal = input.toBigDecimal()

	fun map(input: LocalDate): Timestamp = input.toTimestamp()

	@Mapping(target = "tasks", source = "tasksList")
	abstract fun map(input: GetActiveTasksResponse): RestGetActiveTasksResponse

	@Mapping(target = "task.topics", source = "input.task.topicsList")
	abstract fun map(input: PlayerTaskView): RestPlayerTask

	@Mapping(target = "playerTaskTopics", source = "playerTaskTopicsList")
	abstract fun map(input: GetPlayerTopicsResponse): RestGetPlayerTopicsResponse

	@Mapping(target = "playerTaskTopicsList", source = "input.playerTaskTopics")
	abstract fun map(input: RestSavePlayerTopicsRequest): SavePlayerTopicsRequest

	abstract fun map(input: CompleteTaskResponse): RestCompleteTaskResponse

	@Mapping(target = "roles", source = "rolesList")
	abstract fun map(input: GetUserAdditionalInfoResponse): RestUserAdditionalInfoResponse

	abstract fun map(page: Int, pageSize: Int): RequestPaging

	@Mapping(target = "paging", expression = "java(map(page, pageSize))")
	abstract fun map(
		options: RestRequestQueryOptions?,
		page: Int,
		pageSize: Int
	): SearchEntitiesRequest

	@Mapping(target = "filter.enumFiltersList", source = "options.filter.enumFilters")
	@Mapping(target = "filter.dateFiltersList", source = "options.filter.dateFilters")
	@Mapping(target = "sortsList", source = "options.sorts")
	abstract fun map(options: RestRequestQueryOptions): RequestQueryOptions

	@Mapping(target = "valuesList", source = "values")
	abstract fun map(input: RestEnumFilter): EnumFilter

	@Mapping(target = "transactions", source = "transactionsList")
	@Mapping(target = "options.filters", source = "options.filtersList")
	@Mapping(target = "options.sorts", source = "options.sortsList")
	abstract fun map(input: SearchPlayerBalanceTransactionsResponse): RestSearchPlayerBalanceTransactionsResponse

	@Mapping(target = "tasks", source = "tasksList")
	@Mapping(target = "options.filters", source = "options.filtersList")
	@Mapping(target = "options.sorts", source = "options.sortsList")
	abstract fun map(input: SearchPlayerTasksResponse): RestSearchPlayerTasksResponse

	@Mapping(target = "tasks", source = "tasksList")
	abstract fun map(input: GetDailyTasksResponse): RestGetDailyTasksResponse

	@Mapping(target = "activeDays", source = "activeDaysList")
	abstract fun map(input: GetMonthlyActivityResponse): RestGetMonthlyActivityResponse

	abstract fun map(input: GetPlayerBalanceResponse): RestGetPlayerBalanceResponse

	@Mapping(target = "items", source = "input.itemsList")
	abstract fun map(input: LocalizedField): RestLocalizedField

	@Mapping(target = "paging", expression = "java(map(page, pageSize))")
	abstract fun map(
		type: RestLeaderboardType,
		range: RestDayRange?,
		page: Int,
		pageSize: Int
	): GetUsersLeaderboardRequest

	abstract fun map(type: RestLeaderboardType, range: RestDayRange?): GetUserLeaderboardRequest

	@Mapping(target = "users", source = "usersList")
	abstract fun map(input: GetUsersLeaderboardResponse): RestGetUsersLeaderboardResponse

	abstract fun map(input: GetUserLeaderboardResponse): RestGetUserLeaderboardResponse
}
