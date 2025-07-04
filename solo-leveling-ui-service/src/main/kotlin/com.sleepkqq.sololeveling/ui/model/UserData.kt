package com.sleepkqq.sololeveling.ui.model

import org.springframework.security.core.userdetails.UserDetails
import java.util.Locale


class UserData(
	val id: Long,
	private val username: String?,
	val firstName: String?,
	val lastName: String?,
	val photoUrl: String?,
	val locale: Locale,
	val roles: List<UserRole>
) : UserDetails {

	override fun getAuthorities(): List<UserRole> = roles

	override fun getPassword(): String? = null

	override fun getUsername(): String? = username

	companion object {

		fun fromTgUser(tgUser: TgUserData): UserData {
			return UserData(
				tgUser.id,
				tgUser.username,
				tgUser.firstName,
				tgUser.lastName,
				tgUser.photoUrl,
				tgUser.languageCode
					?.takeIf { "ru".equals(it, ignoreCase = true) }
					.let { Locale.forLanguageTag(it) }
					?: Locale.ENGLISH,
				listOf(UserRole.USER)
			)
		}
	}
}