package com.sleepkqq.sololeveling.ui.model

import org.springframework.security.core.GrantedAuthority

enum class UserRole : GrantedAuthority {
	USER,
	ADMIN;

	override fun getAuthority(): String = name

	val isUser: Boolean
		get() = this == USER || isAdmin

	val isAdmin: Boolean
		get() = this == ADMIN
}