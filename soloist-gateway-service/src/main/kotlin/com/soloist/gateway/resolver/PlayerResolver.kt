package com.soloist.gateway.resolver

import com.soloist.gateway.client.PlayerClient
import com.soloist.gateway.graphql.DgsConstants.PLAYER.Agility
import com.soloist.gateway.graphql.DgsConstants.PLAYER.Id
import com.soloist.gateway.graphql.DgsConstants.PLAYER.Intelligence
import com.soloist.gateway.graphql.DgsConstants.PLAYER.Strength
import com.soloist.gateway.graphql.types.DayStreak
import com.soloist.gateway.graphql.types.Level
import com.soloist.gateway.graphql.types.MonthlyActivityResult
import com.soloist.gateway.graphql.types.Player
import com.soloist.gateway.graphql.types.PlayerTaskTopicInput
import com.soloist.gateway.graphql.types.PlayerTopicsResult
import com.soloist.gateway.graphql.types.Stamina
import com.soloist.gateway.graphql.types.User
import graphql.GraphQLContext
import graphql.schema.DataFetchingEnvironment
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
class PlayerResolver(
	private val playerClient: PlayerClient
) {

	companion object {
		private val PLAYER_SCALAR_FIELDS = setOf(Id, Agility, Strength, Intelligence)
		const val PLAYER_ID = "playerId"
	}

	@SchemaMapping
	suspend fun player(
		user: User,
		environment: DataFetchingEnvironment,
		graphQlContext: GraphQLContext
	): Player {
		val playerId = user.id
		graphQlContext.put(PLAYER_ID, playerId)

		val selectionSet = environment.selectionSet
		if (PLAYER_SCALAR_FIELDS.any(selectionSet::contains)) {
			return playerClient.getPlayer(playerId)
		}

		return Player.newBuilder().id(playerId).build()
	}

	@SchemaMapping
	suspend fun taskTopics(player: Player): PlayerTopicsResult =
		playerClient.getPlayerTopics(player.id)

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

	@SchemaMapping
	suspend fun level(player: Player): Level = playerClient.getLevel(player.id)

	@MutationMapping
	suspend fun savePlayerTopics(@Argument topics: List<PlayerTaskTopicInput>): Boolean {
		playerClient.savePlayerTopics(topics)
		return true
	}
}
