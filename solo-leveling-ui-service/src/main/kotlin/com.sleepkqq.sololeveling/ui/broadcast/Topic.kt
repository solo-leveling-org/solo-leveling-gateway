package com.sleepkqq.sololeveling.ui.broadcast

import com.sleepkqq.sololeveling.avro.notification.Notification
import com.vaadin.flow.shared.Registration

interface Topic<T> {

	fun subscribe(userId: Long, listener: T): Registration

	fun publish(userId: Long, notification: Notification)
}
