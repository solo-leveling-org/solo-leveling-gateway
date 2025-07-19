package com.sleepkqq.sololeveling.gateway.service

import org.bouncycastle.crypto.digests.SHA256Digest
import org.bouncycastle.crypto.macs.HMac
import org.bouncycastle.crypto.params.KeyParameter
import org.bouncycastle.util.encoders.Hex
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Service
class TgHashService(
	@Value($$"${app.telegram.bot.token}") private val tgBotToken: String
) {

	private companion object {
		const val TG_SECRET_KEY = "WebAppData"

		const val HASH_FIELD = "hash"

		const val QUERY_DELIMITER = "&"
		const val KEY_VALUE_DELIMITER = "="
	}

	fun checkHash(initData: String, hash: String): Boolean {
		return validateHash(parseQuery(initData), hash)
	}

	private fun validateHash(parsedQuery: String, receivedHash: String): Boolean {
		val tgBotTokenHash = getHash(
			TG_SECRET_KEY.toByteArray(StandardCharsets.UTF_8),
			tgBotToken
		)
		val dataHash = getHash(tgBotTokenHash, parsedQuery)

		val calculatedHash = Hex.toHexString(dataHash)
		return calculatedHash == receivedHash
	}

	private fun getHash(keyBytes: ByteArray, data: String): ByteArray {
		val hmac = HMac(SHA256Digest())
		hmac.init(KeyParameter(keyBytes))

		val dataBytes = data.toByteArray(StandardCharsets.UTF_8)
		hmac.update(dataBytes, 0, dataBytes.size)

		val hashBytes = ByteArray(hmac.macSize)
		hmac.doFinal(hashBytes, 0)

		return hashBytes
	}

	private fun parseQuery(queryString: String): String = queryString.split(QUERY_DELIMITER)
		.map { it.split(KEY_VALUE_DELIMITER, limit = 2) }
		.map {
			val key = URLDecoder.decode(it[0], StandardCharsets.UTF_8)
			val value = if (it.size > 1) URLDecoder.decode(it[1], StandardCharsets.UTF_8) else ""
			key to value
		}
		.filterNot { (key, _) -> key == HASH_FIELD }
		.sortedBy { it.first }
		.joinToString("\n") { "${it.first}=${it.second}" }
}