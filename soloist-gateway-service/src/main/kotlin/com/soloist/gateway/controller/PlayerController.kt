package com.soloist.gateway.controller

import com.soloist.gateway.api.PlayerRestApi
import com.soloist.gateway.grpc.client.PlayerApi
import com.soloist.gateway.mapper.ProtoMapper
import com.soloist.gateway.dto.RestCompleteTaskResponse
import com.soloist.gateway.dto.RestGetActiveTasksResponse
import com.soloist.gateway.dto.RestGetDailyTasksResponse
import com.soloist.gateway.dto.RestGetMonthlyActivityResponse
import com.soloist.gateway.dto.RestGetPlayerBalanceResponse
import com.soloist.gateway.dto.RestGetPlayerTopicsResponse
import com.soloist.gateway.dto.RestSavePlayerTopicsRequest
import com.soloist.gateway.dto.RestSearchPlayerBalanceTransactionsResponse
import com.soloist.gateway.dto.RestSearchPlayerTasksResponse
import com.soloist.gateway.dto.RestSearchRequest
import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class PlayerController(
	private val playerApi: PlayerApi,
	private val protoMapper: ProtoMapper
) : PlayerRestApi {

	override fun generateTasks(): ResponseEntity<Void> {
		playerApi.generateTasks()

		return ResponseEntity.noContent().build()
	}

	override fun getActiveTasks(): ResponseEntity<RestGetActiveTasksResponse> {
		val grpcResponse = playerApi.getActiveTasks()

		return ResponseEntity.ok(protoMapper.map(grpcResponse))
	}

	override fun getPlayerTopics(): ResponseEntity<RestGetPlayerTopicsResponse> {
		val grpcResponse = playerApi.getPlayerTopics()

		return ResponseEntity.ok(protoMapper.map(grpcResponse))
	}

	override fun savePlayerTopics(request: @Valid RestSavePlayerTopicsRequest): ResponseEntity<Void> {
		playerApi.savePlayerTopics(protoMapper.map(request))

		return ResponseEntity.noContent().build()
	}

	override fun skipTask(id: UUID): ResponseEntity<Void> {
		playerApi.skipTask(id)

		return ResponseEntity.noContent().build()
	}

	override fun completeTask(id: UUID): ResponseEntity<RestCompleteTaskResponse> {
		val grpcResponse = playerApi.completeTask(id)

		return ResponseEntity.ok(protoMapper.map(grpcResponse))
	}

	override fun searchPlayerTasks(
		request: @Valid RestSearchRequest,
		page: @Min(value = 0) @Valid Int,
		pageSize: @Min(value = 1) @Max(value = 100) @Valid Int
	): ResponseEntity<RestSearchPlayerTasksResponse> {
		val grpcRequest = protoMapper.map(request.options, page, pageSize)
		val grpcResponse = playerApi.searchPlayerTasks(grpcRequest)

		return ResponseEntity.ok(protoMapper.map(grpcResponse))
	}

	override fun getDailyTasks(): ResponseEntity<RestGetDailyTasksResponse> {
		val grpcResponse = playerApi.getDailyTasks()

		return ResponseEntity.ok(protoMapper.map(grpcResponse))
	}

	override fun getMonthlyActivity(
		year: @NotNull @Min(value = 2000) @Max(value = 2100) @Valid Int,
		month: @NotNull @Min(value = 1) @Max(value = 12) @Valid Int
	): ResponseEntity<RestGetMonthlyActivityResponse> {
		val grpcResponse = playerApi.getMonthlyActivity(year, month)

		return ResponseEntity.ok(protoMapper.map(grpcResponse))
	}

	override fun getPlayerBalance(): ResponseEntity<RestGetPlayerBalanceResponse> {
		val grpcResponse = playerApi.getPlayerBalance()

		return ResponseEntity.ok(protoMapper.map(grpcResponse))
	}

	override fun searchPlayerBalanceTransactions(
		request: @Valid RestSearchRequest,
		page: @Min(0) @Valid Int,
		pageSize: @Min(1) @Max(100) @Valid Int
	): ResponseEntity<RestSearchPlayerBalanceTransactionsResponse> {
		val grpcRequest = protoMapper.map(request.options, page, pageSize)
		val grpcResponse = playerApi.searchPlayerBalanceTransactions(grpcRequest)

		return ResponseEntity.ok(protoMapper.map(grpcResponse))
	}
}
