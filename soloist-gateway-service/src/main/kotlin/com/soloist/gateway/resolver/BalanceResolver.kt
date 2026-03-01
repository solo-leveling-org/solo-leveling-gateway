package com.soloist.gateway.resolver

import com.soloist.gateway.client.BalanceClient
import com.soloist.gateway.graphql.types.Balance
import com.soloist.gateway.graphql.types.PagingInput
import com.soloist.gateway.graphql.types.SearchBalanceTransactionsResult
import com.soloist.gateway.graphql.types.SearchOptionsInput
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class BalanceResolver(
	private val balanceClient: BalanceClient
) {

	@QueryMapping
	fun balance(): Balance = balanceClient.getBalance()

	@QueryMapping
	fun searchBalanceTransactions(
		@Argument paging: PagingInput,
		@Argument options: SearchOptionsInput?
	): SearchBalanceTransactionsResult = balanceClient.searchBalanceTransactions(paging, options)
}
