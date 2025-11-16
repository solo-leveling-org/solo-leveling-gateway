package com.sleepkqq.sololeveling.gateway.config

import io.confluent.kafka.schemaregistry.client.rest.entities.Mode
import io.confluent.kafka.schemaregistry.client.rest.entities.Schema
import io.confluent.kafka.schemaregistry.client.rest.entities.SchemaString
import io.confluent.kafka.schemaregistry.client.rest.entities.SubjectVersion
import io.confluent.kafka.schemaregistry.client.rest.entities.requests.CompatibilityCheckResponse
import io.confluent.kafka.schemaregistry.client.rest.entities.requests.ConfigUpdateRequest
import io.confluent.kafka.schemaregistry.client.rest.entities.requests.ModeUpdateRequest
import io.confluent.kafka.schemaregistry.client.rest.entities.requests.RegisterSchemaRequest
import io.confluent.kafka.schemaregistry.client.rest.entities.requests.RegisterSchemaResponse
import io.confluent.kafka.serializers.KafkaAvroDeserializer
import io.confluent.kafka.serializers.KafkaAvroSerializer
import io.confluent.kafka.serializers.context.NullContextNameStrategy
import io.confluent.kafka.serializers.context.strategy.ContextNameStrategy
import io.confluent.kafka.serializers.subject.RecordNameStrategy
import io.confluent.kafka.serializers.subject.TopicNameStrategy
import io.confluent.kafka.serializers.subject.TopicRecordNameStrategy
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.impl.DefaultClaimsBuilder
import io.jsonwebtoken.impl.DefaultJwtBuilder
import io.jsonwebtoken.impl.DefaultJwtHeaderBuilder
import io.jsonwebtoken.impl.DefaultJwtParser
import io.jsonwebtoken.impl.DefaultJwtParserBuilder
import io.jsonwebtoken.impl.io.StandardCompressionAlgorithms
import io.jsonwebtoken.impl.security.DefaultDynamicJwkBuilder
import io.jsonwebtoken.impl.security.DefaultJwkParserBuilder
import io.jsonwebtoken.impl.security.DefaultJwkSetBuilder
import io.jsonwebtoken.impl.security.DefaultJwkSetParserBuilder
import io.jsonwebtoken.impl.security.DefaultKeyOperationBuilder
import io.jsonwebtoken.impl.security.DefaultKeyOperationPolicyBuilder
import io.jsonwebtoken.impl.security.JwksBridge
import io.jsonwebtoken.impl.security.KeysBridge
import io.jsonwebtoken.impl.security.StandardEncryptionAlgorithms
import io.jsonwebtoken.impl.security.StandardKeyAlgorithms
import io.jsonwebtoken.impl.security.StandardKeyOperations
import io.jsonwebtoken.impl.security.StandardSecureDigestAlgorithms
import io.jsonwebtoken.security.SignatureAlgorithm
import org.apache.kafka.common.serialization.ByteArrayDeserializer
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding
import org.springframework.context.annotation.Configuration

@RegisterReflectionForBinding(
	classes = [
		// jjwt
		Claims::class,
		Jwts::class,
		Jwts.ENC::class,
		Jwts.KEY::class,
		Jwts.SIG::class,
		Jwts.ZIP::class,
		DefaultJwtParser::class,
		SignatureAlgorithm::class,
		KeysBridge::class,
		StandardEncryptionAlgorithms::class,
		StandardKeyAlgorithms::class,
		StandardSecureDigestAlgorithms::class,
		StandardCompressionAlgorithms::class,
		DefaultJwtHeaderBuilder::class,
		DefaultClaimsBuilder::class,
		DefaultJwtBuilder::class,
		DefaultJwtParserBuilder::class,
		StandardKeyOperations::class,
		DefaultKeyOperationBuilder::class,
		DefaultKeyOperationPolicyBuilder::class,
		DefaultDynamicJwkBuilder::class,
		DefaultJwkParserBuilder::class,
		DefaultJwkSetBuilder::class,
		DefaultJwkSetParserBuilder::class,
		JwksBridge::class,
		// Avro
		KafkaAvroDeserializer::class,
		KafkaAvroSerializer::class,
		RecordNameStrategy::class,
		TopicNameStrategy::class,
		TopicRecordNameStrategy::class,
		NullContextNameStrategy::class,
		ContextNameStrategy::class,
		StringDeserializer::class,
		StringSerializer::class,
		ByteArrayDeserializer::class,
		Schema::class,
		SchemaString::class,
		SubjectVersion::class,
		RegisterSchemaRequest::class,
		RegisterSchemaResponse::class,
		ConfigUpdateRequest::class,
		ModeUpdateRequest::class,
		CompatibilityCheckResponse::class,
		Mode::class
	]
)
@Configuration
class NativeBuildConfig
