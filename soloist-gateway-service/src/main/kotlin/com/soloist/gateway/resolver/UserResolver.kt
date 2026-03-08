package com.soloist.gateway.resolver

import com.soloist.gateway.client.UserClient
import com.soloist.gateway.graphql.DgsConstants.USER.FirstName
import com.soloist.gateway.graphql.DgsConstants.USER.Id
import com.soloist.gateway.graphql.DgsConstants.USER.LastName
import com.soloist.gateway.graphql.DgsConstants.USER.Locale
import com.soloist.gateway.graphql.DgsConstants.USER.PhotoUrl
import com.soloist.gateway.graphql.DgsConstants.USER.Roles
import com.soloist.gateway.graphql.DgsConstants.USER.Username
import com.soloist.gateway.graphql.types.*
import com.soloist.gateway.service.auth.AuthService
import graphql.schema.DataFetchingEnvironment
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class UserResolver(
	private val authService: AuthService,
	private val userClient: UserClient
) {

	private companion object {
		val USER_SCALAR_FIELDS = setOf(Id, Username, FirstName, LastName, PhotoUrl, Locale, Roles)
	}

	@QueryMapping
	suspend fun me(environment: DataFetchingEnvironment): User {
		val currentUser = authService.getCurrentUser()
		return fetchUser(currentUser.id, environment)
	}

	@QueryMapping
	suspend fun user(@Argument id: Long, environment: DataFetchingEnvironment): User =
		fetchUser(id, environment)

	@MutationMapping
	suspend fun updateUserLocale(@Argument locale: UserLocaleInput): Boolean {
		userClient.updateUserLocale(locale)
		return true
	}

	@QueryMapping
	suspend fun usersLeaderboard(
		@Argument paging: PagingInput,
		@Argument filter: LeaderboardFilterInput
	): UsersLeaderboardResult = userClient.getUsersLeaderboard(paging, filter)

	@QueryMapping
	suspend fun userLeaderboard(@Argument filter: LeaderboardFilterInput): LeaderboardUser =
		userClient.getUserLeaderboard(filter)

	private suspend fun fetchUser(id: Long, environment: DataFetchingEnvironment): User {
		val selectionSet = environment.selectionSet
		if (USER_SCALAR_FIELDS.any(selectionSet::contains)) {
			return userClient.getUser(id)
		}

		return User.newBuilder().id(id).build()
	}
}
