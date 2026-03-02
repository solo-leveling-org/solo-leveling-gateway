package com.soloist.gateway.client

import com.soloist.gateway.graphql.types.DayStreak
import com.soloist.gateway.graphql.types.Level
import com.soloist.gateway.graphql.types.MonthlyActivityResult
import com.soloist.gateway.graphql.types.Player
import com.soloist.gateway.graphql.types.PlayerTaskTopicInput
import com.soloist.gateway.graphql.types.PlayerTopicsResult
import com.soloist.gateway.graphql.types.Stamina
import com.soloist.gateway.mapper.ProtoMapper
import com.soloist.proto.player.*
import org.springframework.stereotype.Service

@Service
class PlayerClient(
	private val playerStub: PlayerServiceGrpc.PlayerServiceBlockingStub,
	private val protoMapper: ProtoMapper
) {

	fun getPlayer(id: Long): Player {
		val request = GetPlayerRequest.newBuilder().setPlayerId(id).build()
		val response = playerStub.getPlayer(request)
		return protoMapper.map(response.player)
	}

	fun getPlayerTopics(playerId: Long): PlayerTopicsResult {
		val request = GetPlayerTopicsRequest.newBuilder().setPlayerId(playerId).build()
		val response = playerStub.getPlayerTopics(request)
		return protoMapper.map(response)
	}

	fun savePlayerTopics(topics: List<PlayerTaskTopicInput>) {
		val request = SavePlayerTopicsRequest.newBuilder()
			.addAllTaskTopics(topics.map(protoMapper::map))
			.build()
		playerStub.savePlayerTopics(request)
	}

	fun getMonthlyActivity(playerId: Long, year: Int, month: Int): MonthlyActivityResult {
		val request = GetMonthlyActivityRequest.newBuilder()
			.setPlayerId(playerId)
			.setYear(year)
			.setMonth(month)
			.build()
		val response = playerStub.getMonthlyActivity(request)
		return protoMapper.map(response)
	}

	fun getDayStreak(playerId: Long): DayStreak {
		val request = GetPlayerDayStreakRequest.newBuilder().setPlayerId(playerId).build()
		val response = playerStub.getPlayerDayStreak(request)
		return protoMapper.map(response.dayStreak)
	}

	fun getStamina(playerId: Long): Stamina {
		val request = GetPlayerStaminaRequest.newBuilder().setPlayerId(playerId).build()
		val response = playerStub.getPlayerStamina(request)
		return protoMapper.map(response.stamina)
	}

	fun getLevel(playerId: Long): Level {
		val request = GetPlayerLevelRequest.newBuilder().setPlayerId(playerId).build()
		val response = playerStub.getPlayerLevel(request)
		return protoMapper.map(response.level)
	}
}
