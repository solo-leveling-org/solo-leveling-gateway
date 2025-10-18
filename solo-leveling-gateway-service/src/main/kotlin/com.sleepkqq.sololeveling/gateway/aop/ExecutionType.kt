package com.sleepkqq.sololeveling.gateway.aop

enum class ExecutionType(val value: String, val operationSymbol: OperationSymbol) {
	REST_API("REST API", OperationSymbol.IN),
	GRPC_CALL("gRPC call", OperationSymbol.OUT);
}
