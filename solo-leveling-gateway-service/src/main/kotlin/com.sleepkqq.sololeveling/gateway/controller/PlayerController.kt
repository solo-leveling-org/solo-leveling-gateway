package com.sleepkqq.sololeveling.gateway.controller

import com.sleepkqq.sololeveling.gateway.api.PlayerApi
import com.sleepkqq.sololeveling.gateway.dto.*
import com.sleepkqq.sololeveling.gateway.grpc.client.PlayerGrpcApi
import com.sleepkqq.sololeveling.gateway.mapper.ProtoMapper
import com.sleepkqq.sololeveling.gateway.service.AuthService
import jakarta.validation.Valid
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

	override fun completeTask(request: @Valid RestCompleteTaskRequest): ResponseEntity<Void> {
		val currentUser = authService.getCurrentUser()
		playerGrpcApi.completeTask(protoMapper.map(currentUser.id, request))

		return ResponseEntity.noContent().build()
	}
}
