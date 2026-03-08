package com.soloist.gateway.config.web

import com.soloist.config.interceptor.UserContextHolder
import io.micrometer.context.ThreadLocalAccessor

class UserContextAccessor : ThreadLocalAccessor<Long> {

	private companion object {
		const val KEY = "userId"
	}

	override fun key() = KEY
	override fun getValue(): Long? = UserContextHolder.getUserId()
	override fun setValue(value: Long) = UserContextHolder.setUserId(value)
	override fun setValue() = UserContextHolder.clear()
}
