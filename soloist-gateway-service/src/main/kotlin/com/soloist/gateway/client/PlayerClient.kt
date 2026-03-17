package com.soloist.gateway.client

import com.soloist.gateway.graphql.types.DayStreak
import com.soloist.gateway.graphql.types.MonthlyActivityResult
import com.soloist.gateway.graphql.types.Player
import com.soloist.gateway.graphql.types.Stamina
import com.soloist.gateway.mapper.ProtoMapper
import com.soloist.proto.player.GetDayStreakRequest
import com.soloist.proto.player.GetMonthlyActivityRequest
import com.soloist.proto.player.GetPlayerRequest
import com.soloist.proto.player.GetStaminaRequest
import com.soloist.proto.player.PlayerServiceGrpcKt
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
}
