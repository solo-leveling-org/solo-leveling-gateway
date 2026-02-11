package com.sleepkqq.sololeveling.gateway.mapper

import com.google.protobuf.Timestamp
import com.google.type.Money
import com.sleepkqq.sololeveling.gateway.dto.*
import com.sleepkqq.sololeveling.gateway.extensions.toOffsetDateTime
import com.sleepkqq.sololeveling.gateway.extensions.toBigDecimal
import com.sleepkqq.sololeveling.gateway.extensions.toTimestamp
import com.sleepkqq.sololeveling.gateway.model.UserData
import com.sleepkqq.sololeveling.proto.player.*
import com.sleepkqq.sololeveling.proto.user.GetUserAdditionalInfoResponse
import com.sleepkqq.sololeveling.proto.user.GetUserLeaderboardRequest
import com.sleepkqq.sololeveling.proto.user.GetUserLeaderboardResponse
import com.sleepkqq.sololeveling.proto.user.GetUsersLeaderboardRequest
import com.sleepkqq.sololeveling.proto.user.GetUsersLeaderboardResponse
import com.sleepkqq.sololeveling.proto.user.UserInput
import com.sleepkqq.sololeveling.proto.user.UserRole
import com.sleepkqq.sololeveling.proto.user.UserView
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
