package com.soloist.gateway.client

import com.soloist.gateway.graphql.types.*
import com.soloist.gateway.graphql.types.PlayerTaskTopicInput
import com.soloist.gateway.mapper.ProtoMapper
import com.soloist.proto.player.*
import org.springframework.stereotype.Service

@Service
class PlayerClient(
	private val playerStub: PlayerServiceGrpcKt.PlayerServiceCoroutineStub,
	private val protoMapper: ProtoMapper
) {

	suspend fun getPlayer(id: Long): Player {
		val request = GetPlayerRequest.newBuilder().setPlayerId(id).build()
		val response = playerStub.getPlayer(request)
		return protoMapper.map(response.player)
	}

	suspend fun getPlayerTopics(playerId: Long): PlayerTopicsResult {
		val request = GetPlayerTopicsRequest.newBuilder().setPlayerId(playerId).build()
		val response = playerStub.getPlayerTopics(request)
		return protoMapper.map(response)
	}

	suspend fun savePlayerTopics(topics: List<PlayerTaskTopicInput>) {
		val request = SavePlayerTopicsRequest.newBuilder()
			.addAllTaskTopics(topics.map(protoMapper::map))
			.build()
		playerStub.savePlayerTopics(request)
	}

	suspend fun getMonthlyActivity(playerId: Long, year: Int, month: Int): MonthlyActivityResult {
		val request = GetMonthlyActivityRequest.newBuilder()
			.setPlayerId(playerId)
			.setYear(year)
			.setMonth(month)
			.build()
		val response = playerStub.getMonthlyActivity(request)
		return protoMapper.map(response)
	}

	suspend fun getDayStreak(playerId: Long): DayStreak {
		val request = GetDayStreakRequest.newBuilder().setPlayerId(playerId).build()
		val response = playerStub.getDayStreak(request)
		return protoMapper.map(response.dayStreak)
	}

	suspend fun getStamina(playerId: Long): Stamina {
		val request = GetStaminaRequest.newBuilder().setPlayerId(playerId).build()
		val response = playerStub.getStamina(request)
		return protoMapper.map(response.stamina)
	}

	suspend fun getLevel(playerId: Long): Level {
		val request = GetLevelRequest.newBuilder().setPlayerId(playerId).build()
		val response = playerStub.getLevel(request)
		return protoMapper.map(response.level)
	}
}
