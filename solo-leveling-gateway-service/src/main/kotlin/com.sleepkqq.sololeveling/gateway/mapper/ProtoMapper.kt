package com.sleepkqq.sololeveling.gateway.mapper

import com.google.protobuf.Timestamp
import com.google.type.Money
import com.sleepkqq.sololeveling.gateway.dto.*
import com.sleepkqq.sololeveling.gateway.exception.toLocalDateTime
import com.sleepkqq.sololeveling.gateway.extensions.toBigDecimal
import com.sleepkqq.sololeveling.gateway.model.UserData
import com.sleepkqq.sololeveling.proto.player.*
import com.sleepkqq.sololeveling.proto.user.UserInput
import com.sleepkqq.sololeveling.proto.user.UserView
import org.mapstruct.*
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Suppress("unused")
@Component
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
}
