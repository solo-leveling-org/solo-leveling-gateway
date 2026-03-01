package com.soloist.gateway.client

import com.google.protobuf.Empty
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

	fun getBalance(): Balance {
		val response = balanceStub.getBalance(Empty.getDefaultInstance())
		return protoMapper.map(response.balance)
	}

	fun searchBalanceTransactions(
		paging: PagingInput,
		options: SearchOptionsInput?
	): SearchBalanceTransactionsResult {
		val request = protoMapper.map(paging, options)
		val response = balanceStub.searchBalanceTransactions(request)
		return protoMapper.map(response)
	}
}
