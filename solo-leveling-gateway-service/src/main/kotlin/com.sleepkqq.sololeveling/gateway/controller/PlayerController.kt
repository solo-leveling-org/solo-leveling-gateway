package com.sleepkqq.sololeveling.gateway.controller

import com.sleepkqq.sololeveling.gateway.api.PlayerRestApi
import com.sleepkqq.sololeveling.gateway.dto.*
import com.sleepkqq.sololeveling.gateway.grpc.client.PlayerApi
import com.sleepkqq.sololeveling.gateway.mapper.ProtoMapper
import com.sleepkqq.sololeveling.gateway.service.auth.AuthService
import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class PlayerController(
	private val authService: AuthService,
	private val playerApi: PlayerApi,
	private val protoMapper: ProtoMapper
) : PlayerRestApi {

	override fun generateTasks(): ResponseEntity<Void> {
		val currentUser = authService.getCurrentUser()
		playerApi.generateTasks(currentUser.id)

		return ResponseEntity.noContent().build()
	}

	override fun getActiveTasks(): ResponseEntity<RestGetActiveTasksResponse> {
		val currentUser = authService.getCurrentUser()
		val grpcResponse = playerApi.getActiveTasks(currentUser.id)

		return ResponseEntity.ok(protoMapper.map(grpcResponse))
	}

	override fun getCurrentPlayerTopics(): ResponseEntity<RestGetPlayerTopicsResponse> {
		val currentUser = authService.getCurrentUser()
		val grpcResponse = playerApi.getPlayerTopics(currentUser.id)

		return ResponseEntity.ok(protoMapper.map(grpcResponse))
	}

	override fun getPlayerTopics(playerId: Long): ResponseEntity<RestGetPlayerTopicsResponse> {
		val grpcResponse = playerApi.getPlayerTopics(playerId)

		return ResponseEntity.ok(protoMapper.map(grpcResponse))
	}

	override fun savePlayerTopics(request: @Valid RestSavePlayerTopicsRequest): ResponseEntity<Void> {
		val currentUser = authService.getCurrentUser()
		playerApi.savePlayerTopics(protoMapper.map(currentUser.id, request))

		return ResponseEntity.noContent().build()
	}

	override fun skipTask(request: @Valid RestSkipTaskRequest): ResponseEntity<Void> {
		val currentUser = authService.getCurrentUser()
		playerApi.skipTask(protoMapper.map(currentUser.id, request))

		return ResponseEntity.noContent().build()
	}

	override fun completeTask(request: @Valid RestCompleteTaskRequest): ResponseEntity<RestCompleteTaskResponse> {
		val currentUser = authService.getCurrentUser()
		val grpcResponse = playerApi.completeTask(protoMapper.map(currentUser.id, request))

		return ResponseEntity.ok(protoMapper.map(grpcResponse))
	}

	override fun searchPlayerTasks(
		request: @Valid RestSearchRequest,
		page: @Min(value = 0) @Valid Int,
		pageSize: @Min(value = 1) @Max(value = 100) @Valid Int
	): ResponseEntity<RestSearchPlayerTasksResponse> {

		val currentUser = authService.getCurrentUser()
		val grpcRequest = protoMapper.mapSearchTasksRequest(currentUser.id, request.options, page, pageSize)
		val grpcResponse = playerApi.searchPlayerTasks(grpcRequest)

		return ResponseEntity.ok(protoMapper.map(grpcResponse))
	}

	override fun getPlayerBalance(): ResponseEntity<RestGetPlayerBalanceResponse> {
		val currentUser = authService.getCurrentUser()
		val grpcResponse = playerApi.getPlayerBalance(currentUser.id)

		return ResponseEntity.ok(protoMapper.map(grpcResponse))
	}

	override fun searchPlayerBalanceTransactions(
		request: @Valid RestSearchRequest,
		page: @Min(0) @Valid Int,
		pageSize: @Min(1) @Max(100) @Valid Int
	): ResponseEntity<RestSearchPlayerBalanceTransactionsResponse> {

		val currentUser = authService.getCurrentUser()
		val grpcRequest = protoMapper.mapSearchTransactionsRequest(currentUser.id, request.options, page, pageSize)
		val grpcResponse = playerApi.searchPlayerBalanceTransactions(grpcRequest)

		return ResponseEntity.ok(protoMapper.map(grpcResponse))
	}
}
