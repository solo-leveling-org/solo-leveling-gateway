package com.sleepkqq.sololeveling.gateway.model

import com.sleepkqq.sololeveling.gateway.dto.RestTgUserData
import org.springframework.security.core.userdetails.UserDetails
import java.util.Locale


class UserData(
	val id: Long,
	val tag: String?,
	val firstName: String?,
	val lastName: String?,
	val photoUrl: String?,
	val locale: Locale,
	val roles: Set<UserRole>
) : UserDetails {

	override fun getAuthorities(): Set<UserRole> = roles

	override fun getPassword(): String? = null

	override fun getUsername(): String = "$id"

	companion object {
		private const val RU_LANGUAGE_TAG = "ru"
		private val SUPPORTED_LANGUAGE_TAGS = setOf(RU_LANGUAGE_TAG)

		fun fromTgUser(tgUser: RestTgUserData): UserData {
			return UserData(
				tgUser.id,
				tgUser.username,
				tgUser.firstName,
				tgUser.lastName,
				tgUser.photoUrl,
				tgUser.languageCode
					?.takeIf { it in SUPPORTED_LANGUAGE_TAGS }
					?.let { Locale.forLanguageTag(it) }
					?: Locale.ENGLISH,
				setOf()
			)
		}
	}
}
