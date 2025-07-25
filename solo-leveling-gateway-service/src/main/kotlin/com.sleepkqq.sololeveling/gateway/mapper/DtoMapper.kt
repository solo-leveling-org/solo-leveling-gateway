package com.sleepkqq.sololeveling.gateway.mapper

import com.sleepkqq.sololeveling.proto.user.UserInfo
import com.sleepkqq.sololeveling.proto.user.UserRole
import com.sleepkqq.sololeveling.gateway.model.UserData
import org.springframework.stereotype.Component
import java.util.Locale
import java.util.UUID

@Component
class DtoMapper {

	fun map(user: UserData): UserInfo = UserInfo.newBuilder()
		.setId(user.id)
		.setUsername(user.tag)
		.setFirstName(user.firstName)
		.setLastName(user.lastName)
		.setPhotoUrl(user.photoUrl)
		.setLocale(user.locale.toLanguageTag())
		.addAllRole(user.roles.map { map(it) })
		.build()

	fun map(userInfo: UserInfo): UserData = UserData(
		userInfo.id,
		userInfo.username,
		userInfo.firstName,
		userInfo.lastName,
		userInfo.photoUrl,
		Locale.forLanguageTag(userInfo.locale),
		userInfo.roleList.map { map(it) }
	)

	fun map(userRole: com.sleepkqq.sololeveling.gateway.model.UserRole): UserRole {
		return UserRole.valueOf(userRole.name)
	}

	fun map(userRole: UserRole): com.sleepkqq.sololeveling.gateway.model.UserRole {
		return com.sleepkqq.sololeveling.gateway.model.UserRole.valueOf(userRole.name)
	}

	fun map(input: String): UUID = UUID.fromString(input)

	fun map(input: UUID): String = input.toString()
}
