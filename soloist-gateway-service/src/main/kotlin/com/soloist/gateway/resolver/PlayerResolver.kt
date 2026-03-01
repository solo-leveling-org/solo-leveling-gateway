package com.soloist.gateway.resolver

import com.soloist.gateway.client.PlayerClient
import com.soloist.gateway.graphql.types.MonthlyActivityResult
import com.soloist.gateway.graphql.types.PlayerTaskTopicInput
import com.soloist.gateway.graphql.types.PlayerTopicsResult
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class PlayerResolver(
	private val playerClient: PlayerClient
) {

	@QueryMapping
	fun playerTopics(): PlayerTopicsResult = playerClient.getPlayerTopics()

	@MutationMapping
	fun savePlayerTopics(@Argument topics: List<PlayerTaskTopicInput>): Boolean {
		playerClient.savePlayerTopics(topics)
		return true
	}

	@QueryMapping
	fun monthlyActivity(@Argument year: Int, @Argument month: Int): MonthlyActivityResult =
		playerClient.getMonthlyActivity(year, month)
}
