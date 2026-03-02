package com.soloist.gateway.client

import com.soloist.gateway.graphql.types.Balance
import com.soloist.gateway.graphql.types.PagingInput
import com.soloist.gateway.graphql.types.SearchBalanceTransactionsResult
import com.soloist.gateway.graphql.types.SearchOptionsInput
import com.soloist.gateway.mapper.ProtoMapper
import com.soloist.proto.balance.*
import org.springframework.stereotype.Service

@Service
class BalanceClient(
	private val balanceStub: BalanceServiceGrpc.BalanceServiceBlockingStub,
	private val protoMapper: ProtoMapper
) {

	fun getBalance(playerId: Long): Balance {
		val request = GetBalanceRequest.newBuilder().setPlayerId(playerId).build()
		val response = balanceStub.getBalance(request)
		return protoMapper.map(response.balance)
	}

	fun searchBalanceTransactions(
		playerId: Long,
		paging: PagingInput,
		options: SearchOptionsInput?
	): SearchBalanceTransactionsResult {
		val request = protoMapper.mapTransactions(playerId, paging, options)
		val response = balanceStub.searchBalanceTransactions(request)
		return protoMapper.map(response)
	}
}
