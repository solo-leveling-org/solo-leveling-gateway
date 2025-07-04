package com.sleepkqq.sololeveling.ui.broadcast

import com.sleepkqq.sololeveling.avro.notification.Notification
import com.vaadin.flow.shared.Registration
import java.util.concurrent.ConcurrentHashMap

abstract class AbstractTopic<T> : Topic<T> {

	private val listeners: MutableMap<Long, T> = ConcurrentHashMap()

	override fun subscribe(userId: Long, listener: T): Registration {
		listeners[userId] = listener
		return Registration { listeners.remove(userId) }
	}

	override fun publish(userId: Long, notification: Notification) {
		listeners[userId]?.let { notifyListener(it, notification) }
	}

	protected abstract fun notifyListener(listener: T, notification: Notification)
}
