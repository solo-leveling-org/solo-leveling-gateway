package com.sleepkqq.sololeveling.ui.broadcast;

import com.sleepkqq.sololeveling.avro.notification.Notification;
import com.vaadin.flow.shared.Registration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractTopic<T> implements Topic<T> {

  private final Map<Long, T> listeners = new ConcurrentHashMap<>();

  @Override
  public Registration subscribe(long userId, T listener) {
    listeners.put(userId, listener);
    return () -> listeners.remove(userId);
  }

  @Override
  public void publish(long userId, Notification notification) {
    Optional.ofNullable(listeners.get(userId))
        .ifPresent(l -> notifyListener(l, notification));
  }

  protected abstract void notifyListener(T listener, Notification notification);
}
