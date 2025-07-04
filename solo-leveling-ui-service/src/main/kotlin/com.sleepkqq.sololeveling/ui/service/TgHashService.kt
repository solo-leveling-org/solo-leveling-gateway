package com.sleepkqq.sololeveling.ui.service

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
	@Value("\${telegram.bot.token}") private val tgBotToken: String
) {

	companion object {

		private const val TG_SECRET_KEY = "WebAppData"
		private const val HASH_FIELD = "hash"
	}

	fun checkHash(initData: String, hash: String): Boolean {
		return validateHash(parseQuery(initData), hash)
	}

	private fun validateHash(parsedQuery: String, receivedHash: String): Boolean {
		val tgBotTokenHash = getHash(TG_SECRET_KEY.toByteArray(StandardCharsets.UTF_8), tgBotToken)
		val dataHash = getHash(tgBotTokenHash, parsedQuery)

		val calculatedHash = Hex.toHexString(dataHash)
		return calculatedHash == receivedHash
	}

	private fun getHash(keyBytes: ByteArray, data: String): ByteArray {
		val hmac = HMac(SHA256Digest())
		hmac.init(KeyParameter(keyBytes))
		val hashBytes = ByteArray(hmac.macSize)
		hmac.update(data.toByteArray(StandardCharsets.UTF_8), 0, data.length)
		hmac.doFinal(hashBytes, 0)

		return hashBytes
	}

	private fun parseQuery(queryString: String): String = queryString.split("&")
		.map { it.split("=", limit = 2) }
		.map {
			URLDecoder.decode(it[0], StandardCharsets.UTF_8) to URLDecoder.decode(
				if (it.size > 1) it[1] else "",
				StandardCharsets.UTF_8
			)
		}
		.filterNot { (key, _) -> key == HASH_FIELD }
		.sortedBy { it.first }
		.joinToString("\n") { "${it.first}=${it.second}" }
}