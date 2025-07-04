package com.sleepkqq.sololeveling.ui.broadcast

import com.sleepkqq.sololeveling.avro.notification.Notification
import org.springframework.stereotype.Component
import java.util.function.Consumer

@Component
class TaskUpdatesTopic : AbstractTopic<Consumer<Notification>>() {

	override fun notifyListener(listener: Consumer<Notification>, notification: Notification) =
		listener.accept(notification)
}
