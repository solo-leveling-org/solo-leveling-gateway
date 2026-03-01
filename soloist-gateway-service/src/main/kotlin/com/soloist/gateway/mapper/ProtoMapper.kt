package com.soloist.gateway.mapper

import com.google.protobuf.Timestamp
import com.soloist.gateway.graphql.types.*
import com.soloist.gateway.extensions.toBigDecimal
import com.soloist.gateway.extensions.toOffsetDateTime
import com.soloist.gateway.extensions.toTimestamp
import com.soloist.gateway.model.UserData
import com.soloist.proto.balance.BalanceView
import com.soloist.proto.balance.SearchBalanceTransactionsResponse
import com.soloist.proto.common.EnumFilter
import com.soloist.proto.common.RequestPaging
import com.soloist.proto.common.RequestQueryOptions
import com.soloist.proto.common.SearchEntitiesRequest
import com.soloist.proto.player.GetMonthlyActivityResponse
import com.soloist.proto.player.GetPlayerTopicsResponse
import com.soloist.proto.player.PlayerView
import com.soloist.proto.task.*
import com.soloist.proto.user.GetUserAdditionalInfoResponse
import com.soloist.proto.user.GetUserLeaderboardRequest
import com.soloist.proto.user.GetUsersLeaderboardRequest
import com.soloist.proto.user.GetUsersLeaderboardResponse
import com.soloist.proto.user.UserInput
import com.soloist.proto.user.UserView
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

	fun map(input: com.soloist.proto.common.UserRole): UserRole = UserRole.valueOf(input.name)

	fun map(input: com.soloist.proto.common.Assessment): Assessment = Assessment.valueOf(input.name)

	fun map(input: com.soloist.proto.common.PlayerTaskStatus): PlayerTaskStatus =
		PlayerTaskStatus.valueOf(input.name)

	fun map(input: com.soloist.proto.common.Rarity): Rarity = Rarity.valueOf(input.name)

	fun map(input: com.soloist.proto.common.TaskTopic): TaskTopic = TaskTopic.valueOf(input.name)

	fun map(input: com.soloist.proto.common.BalanceTransactionType): BalanceTransactionType =
		BalanceTransactionType.valueOf(input.name)

	fun map(input: com.soloist.proto.common.BalanceTransactionCause): BalanceTransactionCause =
		BalanceTransactionCause.valueOf(input.name)

	fun map(input: Timestamp): OffsetDateTime? = input.toOffsetDateTime()

	@Mapping(target = "username", source = "tag")
	abstract fun map(input: UserData): UserInput

	abstract fun map(input: UserView): User

	@Mapping(target = "taskTopics", source = "taskTopicsList")
	abstract fun map(input: PlayerView): Player

	abstract fun map(input: BalanceView): Balance

	fun map(input: com.google.type.Money): Money = Money(input.currencyCode, input.toBigDecimal())

	fun map(input: LocalDate): Timestamp = input.toTimestamp()

	@Mapping(target = "tasks", source = "tasksList")
	abstract fun map(input: GetActiveTasksResponse): ActiveTasksResult

	@Mapping(target = "task.topics", source = "input.task.topicsList")
	abstract fun map(input: PlayerTaskView): PlayerTask

	@Mapping(target = "playerTaskTopics", source = "playerTaskTopicsList")
	abstract fun map(input: GetPlayerTopicsResponse): PlayerTopicsResult

	abstract fun map(input: PlayerTaskTopicInput): com.soloist.proto.player.PlayerTaskTopicInput

	abstract fun map(input: CompleteTaskResponse): CompleteTaskResult

	@Mapping(target = "roles", source = "rolesList")
	abstract fun map(input: GetUserAdditionalInfoResponse): UserAdditionalInfoResult

	abstract fun map(paging: PagingInput, options: SearchOptionsInput?): SearchEntitiesRequest

	@Mapping(target = "filter.enumFiltersList", source = "options.filter.enumFilters")
	@Mapping(target = "filter.dateFiltersList", source = "options.filter.dateFilters")
	@Mapping(target = "sortsList", source = "options.sorts")
	abstract fun map(options: SearchOptionsInput?): RequestQueryOptions

	@Mapping(target = "valuesList", source = "values")
	abstract fun map(input: EnumFilterInput): EnumFilter

	@Mapping(target = "transactions", source = "transactionsList")
	@Mapping(target = "options.filters", source = "options.filtersList")
	@Mapping(target = "options.sorts", source = "options.sortsList")
	abstract fun map(input: SearchBalanceTransactionsResponse): SearchBalanceTransactionsResult

	@Mapping(target = "tasks", source = "tasksList")
	@Mapping(target = "options.filters", source = "options.filtersList")
	@Mapping(target = "options.sorts", source = "options.sortsList")
	abstract fun map(input: SearchPlayerTasksResponse): SearchPlayerTasksResult

	@Mapping(target = "tasks", source = "tasksList")
	abstract fun map(input: GetDailyTasksResponse): DailyTasksResult

	@Mapping(target = "activeDays", source = "activeDaysList")
	abstract fun map(input: GetMonthlyActivityResponse): MonthlyActivityResult

	@Mapping(target = "items", source = "input.itemsList")
	abstract fun map(input: com.soloist.proto.common.LocalizedField): LocalizedField

	@Mapping(target = "type", source = "filter.type")
	@Mapping(target = "range", source = "filter.range")
	abstract fun map(paging: PagingInput, filter: LeaderboardFilterInput): GetUsersLeaderboardRequest

	@Mapping(target = "type", source = "filter.type")
	@Mapping(target = "range", source = "filter.range")
	abstract fun map(filter: LeaderboardFilterInput): GetUserLeaderboardRequest

	@Mapping(target = "users", source = "usersList")
	abstract fun map(input: GetUsersLeaderboardResponse): UsersLeaderboardResult

	abstract fun map(input: com.soloist.proto.user.LeaderboardUser): LeaderboardUser

	abstract fun map(input: PagingInput): RequestPaging
}
