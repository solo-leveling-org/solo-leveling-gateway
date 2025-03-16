package com.sleepkqq.sololeveling.config;

import com.sleepkqq.sololeveling.proto.config.DefaultGrpcClientConfig;
import com.sleepkqq.sololeveling.proto.task.TaskServiceGrpc;
import com.sleepkqq.sololeveling.proto.task.TaskServiceGrpc.TaskServiceBlockingStub;
import com.sleepkqq.sololeveling.proto.user.UserServiceGrpc;
import com.sleepkqq.sololeveling.proto.user.UserServiceGrpc.UserServiceBlockingStub;
import io.grpc.ManagedChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcConfigClient extends DefaultGrpcClientConfig {

  public GrpcConfigClient(@Value("${app.host}") String host) {
    super(host);
  }

  @Bean
  public ManagedChannel userServiceChannel(@Value("${app.grpc.services.user.port}") int port) {
    return createManagedChannel(port);
  }

  @Bean
  public UserServiceBlockingStub userServiceBlockingStub(
      ManagedChannel userServiceChannel
  ) {
    return UserServiceGrpc.newBlockingStub(userServiceChannel);
  }

  @Bean
  public ManagedChannel taskServiceChannel(@Value("${app.grpc.services.task.port}") int port) {
    return createManagedChannel(port);
  }

  @Bean
  public TaskServiceBlockingStub taskServiceBlockingStub(
      ManagedChannel taskServiceChannel
  ) {
    return TaskServiceGrpc.newBlockingStub(taskServiceChannel);
  }
}
