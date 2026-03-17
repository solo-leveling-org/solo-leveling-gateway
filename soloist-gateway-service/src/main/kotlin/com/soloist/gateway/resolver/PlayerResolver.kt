package com.soloist.gateway.resolver

import com.soloist.gateway.client.PlayerClient
import com.soloist.gateway.graphql.types.DayStreak
import com.soloist.gateway.graphql.types.MonthlyActivityResult
import com.soloist.gateway.graphql.types.Player
import com.soloist.gateway.graphql.types.Stamina
import com.soloist.gateway.graphql.types.User
import graphql.GraphQLContext
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.ContextValue
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
class PlayerResolver(
	private val playerClient: PlayerClient
) {

	companion object {
		const val PLAYER_ID = "playerId"
	}

	@SchemaMapping
	suspend fun player(
		user: User,
		graphQlContext: GraphQLContext
	): Player {
		val playerId = user.id
		graphQlContext.put(PLAYER_ID, playerId)
		return Player.newBuilder().id(playerId).build()
	}

	@SchemaMapping
	suspend fun monthlyActivity(
		player: Player,
		@Argument year: Int,
		@Argument month: Int
	): MonthlyActivityResult = playerClient.getMonthlyActivity(player.id, year, month)

	@SchemaMapping
	suspend fun dayStreak(player: Player): DayStreak = playerClient.getDayStreak(player.id)

	@SchemaMapping
	suspend fun stamina(player: Player): Stamina = playerClient.getStamina(player.id)
}
