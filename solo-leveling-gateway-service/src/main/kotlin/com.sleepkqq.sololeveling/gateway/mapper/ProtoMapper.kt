package com.sleepkqq.sololeveling.gateway.mapper

import com.google.type.Money
import com.sleepkqq.sololeveling.gateway.dto.RestAssessment
import com.sleepkqq.sololeveling.gateway.dto.RestCompleteTaskRequest
import com.sleepkqq.sololeveling.gateway.dto.RestGetActiveTasksResponse
import com.sleepkqq.sololeveling.gateway.dto.RestGetPlayerTopicsResponse
import com.sleepkqq.sololeveling.gateway.dto.RestLevel
import com.sleepkqq.sololeveling.gateway.dto.RestMoney
import com.sleepkqq.sololeveling.gateway.dto.RestPlayer
import com.sleepkqq.sololeveling.gateway.dto.RestPlayerBalance
import com.sleepkqq.sololeveling.gateway.dto.RestPlayerTask
import com.sleepkqq.sololeveling.gateway.dto.RestPlayerTaskStatus
import com.sleepkqq.sololeveling.gateway.dto.RestPlayerTaskTopic
import com.sleepkqq.sololeveling.gateway.dto.RestSavePlayerTopicsRequest
import com.sleepkqq.sololeveling.gateway.dto.RestSkipTaskRequest
import com.sleepkqq.sololeveling.gateway.dto.RestTask
import com.sleepkqq.sololeveling.gateway.dto.RestTaskRarity
import com.sleepkqq.sololeveling.gateway.dto.RestTaskTopic
import com.sleepkqq.sololeveling.gateway.dto.RestUser
import com.sleepkqq.sololeveling.gateway.dto.RestUserRole
import com.sleepkqq.sololeveling.gateway.extensions.toBigDecimal
import com.sleepkqq.sololeveling.gateway.model.UserData
import com.sleepkqq.sololeveling.gateway.model.UserRole
import com.sleepkqq.sololeveling.proto.player.Assessment
import com.sleepkqq.sololeveling.proto.player.CompleteTaskRequest
import com.sleepkqq.sololeveling.proto.player.GetActiveTasksResponse
import com.sleepkqq.sololeveling.proto.player.GetPlayerTopicsResponse
import com.sleepkqq.sololeveling.proto.player.LevelView
import com.sleepkqq.sololeveling.proto.player.PlayerBalanceView
import com.sleepkqq.sololeveling.proto.player.PlayerTaskInput
import com.sleepkqq.sololeveling.proto.player.PlayerTaskStatus
import com.sleepkqq.sololeveling.proto.player.PlayerTaskTopicView
import com.sleepkqq.sololeveling.proto.player.PlayerTaskView
import com.sleepkqq.sololeveling.proto.player.PlayerView
import com.sleepkqq.sololeveling.proto.player.SavePlayerTopicsRequest
import com.sleepkqq.sololeveling.proto.player.SkipTaskRequest
import com.sleepkqq.sololeveling.proto.player.TaskRarity
import com.sleepkqq.sololeveling.proto.player.TaskTopic
import com.sleepkqq.sololeveling.proto.player.TaskView
import com.sleepkqq.sololeveling.proto.user.UserInput
import com.sleepkqq.sololeveling.proto.user.UserView
import org.mapstruct.CollectionMappingStrategy
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named
import org.mapstruct.NullValueCheckStrategy
import org.mapstruct.NullValueMappingStrategy
import org.mapstruct.NullValuePropertyMappingStrategy
import org.mapstruct.ReportingPolicy
import org.springframework.stereotype.Component

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

	@Named("toEntityUserRole")
	fun map(input: com.sleepkqq.sololeveling.proto.user.UserRole): UserRole =
		UserRole.valueOf(input.name)

	@Named("toRestUserRole")
	fun mapRest(input: com.sleepkqq.sololeveling.proto.user.UserRole): RestUserRole =
		RestUserRole.valueOf(input.name)

	@Named("toRestAssessment")
	fun mapRest(input: Assessment): RestAssessment =
		RestAssessment.valueOf(input.name)

	@Named("toRestPlayerTaskStatus")
	fun mapRest(input: PlayerTaskStatus): RestPlayerTaskStatus =
		RestPlayerTaskStatus.valueOf(input.name)

	@Named("toRestTaskRarity")
	fun mapRest(input: TaskRarity): RestTaskRarity =
		RestTaskRarity.valueOf(input.name)

	@Named("toRestTaskTopic")
	fun mapRest(input: TaskTopic): RestTaskTopic =
		RestTaskTopic.valueOf(input.name)

	@Mapping(target = "rolesList", source = "roles")
	@Mapping(target = "username", source = "tag")
	abstract fun map(input: UserData): UserInput

	@Mapping(target = "player", source = "player", qualifiedByName = ["toRestPlayer"])
	@Mapping(target = "roles", source = "rolesList", qualifiedByName = ["toRestUserRole"])
	abstract fun map(input: UserView): RestUser

	@Named("toRestPlayer")
	@Mapping(target = "level", source = "level", qualifiedByName = ["toRestLevel"])
	@Mapping(target = "balance", source = "balance", qualifiedByName = ["toRestPlayerBalance"])
	abstract fun map(input: PlayerView): RestPlayer

	@Named("toRestLevel")
	@Mapping(target = "assessment", source = "assessment", qualifiedByName = ["toRestAssessment"])
	abstract fun map(input: LevelView): RestLevel

	@Named("toRestPlayerBalance")
	@Mapping(target = "balance", source = "balance", qualifiedByName = ["toRestMoney"])
	abstract fun map(input: PlayerBalanceView): RestPlayerBalance

	@Named("toRestMoney")
	fun map(input: Money): RestMoney = RestMoney()
		.currencyCode(input.currencyCode)
		.amount(input.toBigDecimal())

	@Mapping(target = "tasks", source = "tasksList", qualifiedByName = ["toRestPlayerTask"])
	abstract fun map(input: GetActiveTasksResponse): RestGetActiveTasksResponse

	@Named("toRestPlayerTask")
	@Mapping(target = "task", source = "task", qualifiedByName = ["toRestTask"])
	@Mapping(target = "status", source = "status", qualifiedByName = ["toRestPlayerTaskStatus"])
	abstract fun map(input: PlayerTaskView): RestPlayerTask

	@Named("toRestTask")
	@Mapping(target = "rarity", source = "rarity", qualifiedByName = ["toRestTaskRarity"])
	@Mapping(target = "topics", source = "topicsList", qualifiedByName = ["toRestTaskTopic"])
	abstract fun map(input: TaskView): RestTask

	@Mapping(
		target = "playerTaskTopics",
		source = "playerTaskTopicsList",
		qualifiedByName = ["toRestPlayerTaskTopic"]
	)
	abstract fun map(input: GetPlayerTopicsResponse): RestGetPlayerTopicsResponse

	@Named("toRestPlayerTaskTopic")
	@Mapping(target = "level", source = "level", qualifiedByName = ["toRestLevel"])
	@Mapping(target = "taskTopic", source = "taskTopic", qualifiedByName = ["toRestTaskTopic"])
	abstract fun map(input: PlayerTaskTopicView): RestPlayerTaskTopic

	@Mapping(target = "topicsList", source = "input.topics")
	abstract fun map(playerId: Long, input: RestSavePlayerTopicsRequest): SavePlayerTopicsRequest

	@Mapping(
		target = "playerTask",
		source = "input.playerTask",
		qualifiedByName = ["toProtoPlayerTaskInput"]
	)
	abstract fun map(playerId: Long, input: RestSkipTaskRequest): SkipTaskRequest

	@Mapping(
		target = "playerTask",
		source = "input.playerTask",
		qualifiedByName = ["toProtoPlayerTaskInput"]
	)
	abstract fun map(playerId: Long, input: RestCompleteTaskRequest): CompleteTaskRequest

	@Named("toProtoPlayerTaskInput")
	@Mapping(target = "task.topicsList", source = "input.task.topics")
	abstract fun map(input: RestPlayerTask): PlayerTaskInput
}
