package com.soloist.gateway.mapper

import com.google.protobuf.Timestamp
import com.soloist.gateway.graphql.types.Balance
import com.soloist.gateway.graphql.types.BalanceTransactionCause
import com.soloist.gateway.graphql.types.BalanceTransactionType
import com.soloist.gateway.graphql.types.CreateCustomTaskResult
import com.soloist.gateway.graphql.types.Task
import com.soloist.gateway.graphql.types.DayStreak
import com.soloist.gateway.graphql.types.TaskType
import com.soloist.gateway.graphql.types.EnumFilterInput
import com.soloist.gateway.graphql.types.LeaderboardFilterInput
import com.soloist.gateway.graphql.types.LeaderboardUser
import com.soloist.gateway.graphql.types.LocalizedField
import com.soloist.gateway.graphql.types.Money
import com.soloist.gateway.graphql.types.MonthlyActivityResult
import com.soloist.gateway.graphql.types.PagingInput
import com.soloist.gateway.graphql.types.Player
import com.soloist.gateway.graphql.types.ProofType
import com.soloist.gateway.graphql.types.SearchBalanceTransactionsResult
import com.soloist.gateway.graphql.types.SearchOptionsInput
import com.soloist.gateway.graphql.types.Stamina
import com.soloist.gateway.graphql.types.TaskHistoryResult
import com.soloist.gateway.graphql.types.TasksResult
import com.soloist.gateway.graphql.types.User
import com.soloist.gateway.graphql.types.UserLocaleInput
import com.soloist.gateway.graphql.types.UserRole
import com.soloist.gateway.graphql.types.UsersLeaderboardResult
import com.soloist.gateway.extensions.toBigDecimal
import com.soloist.gateway.extensions.toOffsetDateTime
import com.soloist.gateway.extensions.toTimestamp
import com.soloist.gateway.model.UserData
import com.soloist.proto.balance.BalanceView
import com.soloist.proto.balance.SearchBalanceTransactionsRequest
import com.soloist.proto.balance.SearchBalanceTransactionsResponse
import com.soloist.proto.common.EnumFilter
import com.soloist.proto.common.RequestPaging
import com.soloist.proto.common.RequestQueryOptions
import com.soloist.proto.player.DayStreakView
import com.soloist.proto.player.GetMonthlyActivityResponse
import com.soloist.proto.player.PlayerView
import com.soloist.proto.player.StaminaView
import com.soloist.proto.task.CreateCustomTaskResponse
import com.soloist.proto.task.TaskView
import com.soloist.proto.task.GetTaskHistoryResponse
import com.soloist.proto.task.GetTasksResponse
import com.soloist.proto.user.GetUserLeaderboardRequest
import com.soloist.proto.user.GetUsersLeaderboardRequest
import com.soloist.proto.user.GetUsersLeaderboardResponse
import com.soloist.proto.user.UserInput
import com.soloist.proto.user.UserLocale
import com.soloist.proto.user.UserView
import org.mapstruct.CollectionMappingStrategy
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named
import org.mapstruct.NullValueCheckStrategy
import org.mapstruct.NullValueMappingStrategy
import org.mapstruct.NullValuePropertyMappingStrategy
import org.mapstruct.ReportingPolicy
import java.math.BigDecimal
import java.time.Instant
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

	fun map(input: com.soloist.proto.common.BalanceTransactionType): BalanceTransactionType =
		BalanceTransactionType.valueOf(input.name)

	fun map(input: com.soloist.proto.common.BalanceTransactionCause): BalanceTransactionCause =
		BalanceTransactionCause.valueOf(input.name)

	fun map(input: com.soloist.proto.common.TaskType): TaskType =
		TaskType.valueOf(input.name)

	fun map(input: com.soloist.proto.common.ProofType): ProofType = ProofType.valueOf(input.name)

	fun map(input: Timestamp): OffsetDateTime? = input.toOffsetDateTime()

	@Named("timestampToLocalDate")
	fun mapToLocalDate(input: Timestamp): LocalDate =
		Instant.ofEpochSecond(input.seconds, input.nanos.toLong())
			.atZone(java.time.ZoneOffset.UTC)
			.toLocalDate()

	@Mapping(target = "username", source = "tag")
	abstract fun map(input: UserData): UserInput

	@Mapping(target = "roles", source = "rolesList")
	abstract fun map(input: UserView): User

	abstract fun map(input: PlayerView): Player

	abstract fun map(input: BalanceView): Balance

	fun map(input: com.google.type.Money): Money = Money.newBuilder()
		.currencyCode(input.currencyCode)
		.amount(input.toBigDecimal())
		.build()

	fun map(input: com.google.type.Decimal): BigDecimal = input.toBigDecimal()

	fun map(input: LocalDate): Timestamp = input.toTimestamp()

	@Mapping(target = "tasks", source = "tasksList")
	abstract fun map(input: GetTasksResponse): TasksResult

	@Mapping(target = "day", source = "day", qualifiedByName = ["timestampToLocalDate"])
	abstract fun map(input: TaskView): Task

	@Mapping(target = "task", source = "task")
	abstract fun map(input: CreateCustomTaskResponse): CreateCustomTaskResult

	@Mapping(target = "tasks", source = "tasksList")
	abstract fun map(input: GetTaskHistoryResponse): TaskHistoryResult

	abstract fun mapTransactions(
		playerId: Long,
		paging: PagingInput,
		options: SearchOptionsInput?
	): SearchBalanceTransactionsRequest

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

	abstract fun map(input: UserLocaleInput): UserLocale

	abstract fun map(input: DayStreakView): DayStreak

	abstract fun map(input: StaminaView): Stamina
}
