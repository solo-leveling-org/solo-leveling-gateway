package com.sleepkqq.sololeveling.ui.controller;

import com.sleepkqq.sololeveling.ui.view.task.Broadcaster;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;

@Service
public class TestScheduler {

  private static final ScheduledExecutorService SCHEDULER = Executors.newScheduledThreadPool(1);

  public void scheduleBroadcast(long userId) {
    SCHEDULER.schedule(() -> Broadcaster.broadcast(userId), 30, TimeUnit.SECONDS);
  }
}