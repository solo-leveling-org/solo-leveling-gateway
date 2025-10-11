package com.sleepkqq.sololeveling.gateway.controller

import com.sleepkqq.sololeveling.gateway.api.PlayerApi
import com.sleepkqq.sololeveling.gateway.dto.*
import com.sleepkqq.sololeveling.gateway.grpc.client.PlayerGrpcApi
import com.sleepkqq.sololeveling.gateway.mapper.ProtoMapper
import com.sleepkqq.sololeveling.gateway.service.auth.AuthService
import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@Suppress("unused")
@RestController
class PlayerController(
	private val authService: AuthService,
	private val playerGrpcApi: PlayerGrpcApi,
	private val protoMapper: ProtoMapper
) : PlayerApi {

	override fun generateTasks(): ResponseEntity<Void> {
		val currentUser = authService.getCurrentUser()
		playerGrpcApi.generateTasks(currentUser.id)

		return ResponseEntity.noContent().build()
	}

	override fun getActiveTasks(): ResponseEntity<RestGetActiveTasksResponse> {
		val currentUser = authService.getCurrentUser()
		val grpcResponse = playerGrpcApi.getActiveTasks(currentUser.id)

		return ResponseEntity.ok(protoMapper.map(grpcResponse))
	}

	override fun getCurrentPlayerTopics(): ResponseEntity<RestGetPlayerTopicsResponse> {
		val currentUser = authService.getCurrentUser()
		val grpcResponse = playerGrpcApi.getPlayerTopics(currentUser.id)

		return ResponseEntity.ok(protoMapper.map(grpcResponse))
	}

	override fun getPlayerTopics(playerId: Long): ResponseEntity<RestGetPlayerTopicsResponse> {
		val grpcResponse = playerGrpcApi.getPlayerTopics(playerId)

		return ResponseEntity.ok(protoMapper.map(grpcResponse))
	}

	override fun savePlayerTopics(request: @Valid RestSavePlayerTopicsRequest): ResponseEntity<Void> {
		val currentUser = authService.getCurrentUser()
		playerGrpcApi.savePlayerTopics(protoMapper.map(currentUser.id, request))

		return ResponseEntity.noContent().build()
	}

	override fun skipTask(request: @Valid RestSkipTaskRequest): ResponseEntity<Void> {
		val currentUser = authService.getCurrentUser()
		playerGrpcApi.skipTask(protoMapper.map(currentUser.id, request))

		return ResponseEntity.noContent().build()
	}

	override fun completeTask(request: @Valid RestCompleteTaskRequest): ResponseEntity<RestCompleteTaskResponse> {
		val currentUser = authService.getCurrentUser()
		val grpcResponse = playerGrpcApi.completeTask(protoMapper.map(currentUser.id, request))

		return ResponseEntity.ok(protoMapper.map(grpcResponse))
	}

	override fun getPlayerBalance(): ResponseEntity<RestGetPlayerBalanceResponse> {
		val currentUser = authService.getCurrentUser()
		val grpcResponse = playerGrpcApi.getPlayerBalance(currentUser.id)

		return ResponseEntity.ok(protoMapper.map(grpcResponse))
	}

	override fun searchPlayerBalanceTransactions(
		request: @Valid RestSearchPlayerBalanceTransactionsRequest,
		page: @Min(0) @Valid Int,
		pageSize: @Min(1) @Max(100) @Valid Int
	): ResponseEntity<RestSearchPlayerBalanceTransactionsResponse> {

		val currentUser = authService.getCurrentUser()
		val grpcRequest = protoMapper.map(currentUser.id, request.options, page, pageSize)
		val grpcResponse = playerGrpcApi.searchPlayerBalanceTransactions(grpcRequest)

		return ResponseEntity.ok(protoMapper.map(grpcResponse))
	}
}
