package com.sleepkqq.sololeveling.ui.view.task;

import com.vaadin.flow.shared.Registration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Broadcaster {

  private static final Executor EXECUTOR = Executors.newSingleThreadExecutor();
  public static final Map<Long, Runnable> LISTENERS = new HashMap<>();

  public static synchronized Registration register(
      long userId,
      Runnable listener
  ) {
    LISTENERS.put(userId, listener);

    return () -> {
      synchronized (Broadcaster.class) {
        LISTENERS.remove(userId);
      }
    };
  }

  public static synchronized void broadcast(long userId) {
    EXECUTOR.execute(() -> Optional.ofNullable(LISTENERS.get(userId)).ifPresent(Runnable::run));
  }
}