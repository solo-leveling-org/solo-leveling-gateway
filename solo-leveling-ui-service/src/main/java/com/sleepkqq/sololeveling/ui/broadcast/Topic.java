package com.sleepkqq.sololeveling.ui.broadcast;

import com.sleepkqq.sololeveling.avro.notification.Notification;
import com.vaadin.flow.shared.Registration;

public interface Topic<T> {

  Registration subscribe(long userId, T listener);

  void publish(long userId, Notification notification);
}
