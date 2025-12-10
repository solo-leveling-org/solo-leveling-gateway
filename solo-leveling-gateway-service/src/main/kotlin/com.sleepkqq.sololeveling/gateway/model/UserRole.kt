package com.sleepkqq.sololeveling.gateway.model

import org.springframework.security.core.GrantedAuthority

enum class UserRole : GrantedAuthority {
	USER,
	ADMIN,
	DEVELOPER,
	MANAGER;

	override fun getAuthority(): String = name
}