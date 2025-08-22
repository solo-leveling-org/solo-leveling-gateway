package com.sleepkqq.sololeveling.gateway.config

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
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding
import org.springframework.context.annotation.Configuration

@Suppress("unused")
@RegisterReflectionForBinding(
	classes = [
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
		JwksBridge::class
	]
)
@Configuration
class NativeBuildConfig
