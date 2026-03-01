package com.soloist.gateway.client

import com.google.protobuf.Empty
import com.soloist.gateway.graphql.types.MonthlyActivityResult
import com.soloist.gateway.graphql.types.PlayerTaskTopicInput
import com.soloist.gateway.graphql.types.PlayerTopicsResult
import com.soloist.gateway.mapper.ProtoMapper
import com.soloist.proto.player.*
import org.springframework.stereotype.Service

@Service
class PlayerClient(
	private val playerStub: PlayerServiceGrpc.PlayerServiceBlockingStub,
	private val protoMapper: ProtoMapper
) {

	fun getPlayerTopics(): PlayerTopicsResult {
		val response = playerStub.getPlayerTopics(Empty.getDefaultInstance())
		return protoMapper.map(response)
	}

	fun savePlayerTopics(topics: List<PlayerTaskTopicInput>) {
		val request = SavePlayerTopicsRequest.newBuilder()
			.addAllPlayerTaskTopics(topics.map(protoMapper::map))
			.build()
		playerStub.savePlayerTopics(request)
	}

	fun getMonthlyActivity(year: Int, month: Int): MonthlyActivityResult {
		val request = GetMonthlyActivityRequest.newBuilder().setYear(year).setMonth(month).build()
		val response = playerStub.getMonthlyActivity(request)
		return protoMapper.map(response)
	}
}
