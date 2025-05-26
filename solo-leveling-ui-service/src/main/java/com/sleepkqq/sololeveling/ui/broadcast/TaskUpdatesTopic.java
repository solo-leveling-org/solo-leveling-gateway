package com.sleepkqq.sololeveling.ui.broadcast;

import com.sleepkqq.sololeveling.avro.notification.Notification;
import java.util.function.Consumer;
import org.springframework.stereotype.Component;

@Component
public class TaskUpdatesTopic extends AbstractTopic<Consumer<Notification>> {

  @Override
  protected void notifyListener(Consumer<Notification> listener, Notification notification) {
    listener.accept(notification);
  }
}
