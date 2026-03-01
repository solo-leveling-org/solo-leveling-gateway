package com.soloist.gateway.resolver

import com.soloist.gateway.client.UserClient
import com.soloist.gateway.graphql.types.*
import com.soloist.gateway.service.auth.AuthService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.util.*

@Controller
class UserResolver(
	private val authService: AuthService,
	private val userClient: UserClient
) {

	@QueryMapping
	fun me(): User {
		val currentUser = authService.getCurrentUser()
		return userClient.getUser(currentUser.id)
	}

	@QueryMapping
	fun user(@Argument id: Long): User = userClient.getUser(id)

	@QueryMapping
	fun userAdditionalInfo(): UserAdditionalInfoResult = userClient.getUserAdditionalInfo()

	@MutationMapping
	fun updateUserLocale(@Argument locale: String): Boolean {
		userClient.updateUserLocale(Locale.forLanguageTag(locale))
		return true
	}

	@QueryMapping
	fun usersLeaderboard(
		@Argument paging: PagingInput,
		@Argument filter: LeaderboardFilterInput
	): UsersLeaderboardResult = userClient.getUsersLeaderboard(paging, filter)

	@QueryMapping
	fun userLeaderboard(@Argument filter: LeaderboardFilterInput): LeaderboardUser =
		userClient.getUserLeaderboard(filter)
}
