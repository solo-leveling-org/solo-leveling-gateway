package com.soloist.gateway.resolver

import com.soloist.gateway.client.BalanceClient
import com.soloist.gateway.graphql.DgsConstants.BALANCE.Amount
import com.soloist.gateway.graphql.DgsConstants.BALANCE.Id
import com.soloist.gateway.graphql.types.*
import com.soloist.gateway.resolver.PlayerResolver.Companion.PLAYER_ID
import graphql.schema.DataFetchingEnvironment
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.ContextValue
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
class BalanceResolver(
	private val balanceClient: BalanceClient
) {

	private companion object {
		val BALANCE_SCALAR_FIELDS = setOf(Id, Amount)
	}

	@SchemaMapping
	suspend fun balance(player: Player, environment: DataFetchingEnvironment): Balance {
		val selectionSet = environment.selectionSet
		if (BALANCE_SCALAR_FIELDS.any(selectionSet::contains)) {
			return balanceClient.getBalance(player.id)
		}

		return Balance()
	}

	@SchemaMapping(typeName = "Balance")
	suspend fun transactions(
		@ContextValue(PLAYER_ID) playerId: Long,
		@Argument paging: PagingInput,
		@Argument options: SearchOptionsInput?
	): SearchBalanceTransactionsResult {
		return balanceClient.searchBalanceTransactions(playerId, paging, options)
	}
}
