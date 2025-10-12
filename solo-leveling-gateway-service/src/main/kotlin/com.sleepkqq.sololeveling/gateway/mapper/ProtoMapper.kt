package com.sleepkqq.sololeveling.gateway.mapper

import com.google.protobuf.Timestamp
import com.google.type.Money
import com.sleepkqq.sololeveling.gateway.dto.*
import com.sleepkqq.sololeveling.gateway.extensions.toLocalDateTime
import com.sleepkqq.sololeveling.gateway.extensions.toBigDecimal
import com.sleepkqq.sololeveling.gateway.extensions.toTimestamp
import com.sleepkqq.sololeveling.gateway.model.UserData
import com.sleepkqq.sololeveling.proto.player.*
import com.sleepkqq.sololeveling.proto.user.UserInput
import com.sleepkqq.sololeveling.proto.user.UserLocaleResponse
import com.sleepkqq.sololeveling.proto.user.UserView
import org.mapstruct.*
import java.time.LocalDateTime

@Suppress("unused")
@Mapper(
	componentModel = "spring",
	unmappedTargetPolicy = ReportingPolicy.IGNORE,
	collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
	nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT,
	nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
abstract class ProtoMapper {

	fun map(input: com.sleepkqq.sololeveling.proto.user.UserRole): RestUserRole =
		RestUserRole.valueOf(input.name)

	fun map(input: Assessment): RestAssessment =
		RestAssessment.valueOf(input.name)

	fun map(input: PlayerTaskStatus): RestPlayerTaskStatus =
		RestPlayerTaskStatus.valueOf(input.name)

	fun map(input: TaskRarity): RestTaskRarity =
		RestTaskRarity.valueOf(input.name)

	fun map(input: TaskTopic): RestTaskTopic =
		RestTaskTopic.valueOf(input.name)

	fun map(input: PlayerBalanceTransactionType): RestPlayerBalanceTransactionType =
		RestPlayerBalanceTransactionType.valueOf(input.name)

	fun map(input: PlayerBalanceTransactionCause): RestPlayerBalanceTransactionCause =
		RestPlayerBalanceTransactionCause.valueOf(input.name)

	fun map(input: Timestamp): LocalDateTime = input.toLocalDateTime()

	@Mapping(target = "rolesList", source = "roles")
	@Mapping(target = "username", source = "tag")
	abstract fun map(input: UserData): UserInput

	@Mapping(target = "roles", source = "rolesList")
	abstract fun map(input: UserView): RestUser

	@Mapping(target = "taskTopics", source = "taskTopicsList")
	abstract fun map(input: PlayerView): RestPlayer

	abstract fun map(input: PlayerBalanceView): RestPlayerBalance

	fun map(input: Money): RestMoney = RestMoney()
		.currencyCode(input.currencyCode)
		.amount(input.toBigDecimal())

	fun map(input: LocalDateTime): Timestamp = input.toTimestamp()

	@Mapping(target = "tasks", source = "tasksList")
	abstract fun map(input: GetActiveTasksResponse): RestGetActiveTasksResponse

	@Mapping(target = "task.topics", source = "input.task.topicsList")
	abstract fun map(input: PlayerTaskView): RestPlayerTask

	@Mapping(target = "playerTaskTopics", source = "playerTaskTopicsList")
	abstract fun map(input: GetPlayerTopicsResponse): RestGetPlayerTopicsResponse

	@Mapping(target = "playerTaskTopicsList", source = "input.playerTaskTopics")
	abstract fun map(playerId: Long, input: RestSavePlayerTopicsRequest): SavePlayerTopicsRequest

	@Mapping(target = "playerTask", source = "input.playerTask")
	abstract fun map(playerId: Long, input: RestSkipTaskRequest): SkipTaskRequest

	@Mapping(target = "playerTask", source = "input.playerTask")
	abstract fun map(playerId: Long, input: RestCompleteTaskRequest): CompleteTaskRequest

	@Mapping(target = "task.topicsList", source = "input.task.topics")
	abstract fun map(input: RestPlayerTask): PlayerTaskInput

	abstract fun map(input: CompleteTaskResponse): RestCompleteTaskResponse

	abstract fun map(input: UserLocaleResponse): RestUserLocaleResponse

	@Mapping(
		target = "options",
		expression = "java(map(options, page, pageSize))"
	)
	abstract fun map(
		playerId: Long,
		options: RestRequestQueryOptions?,
		page: Int,
		pageSize: Int
	): SearchPlayerBalanceTransactionsRequest

	@Mapping(target = "filter.stringFiltersList", source = "options.filter.stringFilters")
	@Mapping(target = "filter.dateFiltersList", source = "options.filter.dateFilters")
	@Mapping(target = "sortsList", source = "options.sorts")
	abstract fun map(
		options: RestRequestQueryOptions,
		page: Int,
		pageSize: Int
	): RequestQueryOptions

	@Mapping(target = "valuesList", source = "values")
	abstract fun map(input: RestStringFilter): StringFilter

	@Mapping(target = "transactions", source = "transactionsList")
	@Mapping(target = "options.filters", source = "options.filtersList")
	@Mapping(target = "options.sorts", source = "options.sortsList")
	abstract fun map(input: SearchPlayerBalanceTransactionsResponse): RestSearchPlayerBalanceTransactionsResponse

	abstract fun map(input: GetPlayerBalanceResponse): RestGetPlayerBalanceResponse

	@Mapping(target = "items", source = "input.itemsList")
	abstract fun map(input: LocalizedField): RestLocalizedField
}
