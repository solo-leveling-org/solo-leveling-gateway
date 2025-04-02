package com.sleepkqq.sololeveling.ui.config;

import com.sleepkqq.sololeveling.proto.config.DefaultGrpcClientConfig;
import com.sleepkqq.sololeveling.proto.player.PlayerServiceGrpc;
import com.sleepkqq.sololeveling.proto.player.PlayerServiceGrpc.PlayerServiceBlockingStub;
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
  public ManagedChannel playerManagedChannel(@Value("${app.grpc.services.player.port}") int port) {
    return createManagedChannel(port);
  }

  @Bean
  public UserServiceBlockingStub userServiceBlockingStub(
      ManagedChannel playerManagedChannel
  ) {
    return UserServiceGrpc.newBlockingStub(playerManagedChannel);
  }

  @Bean
  public PlayerServiceBlockingStub playerServiceBlockingStub(
      ManagedChannel playerManagedChannel
  ) {
    return PlayerServiceGrpc.newBlockingStub(playerManagedChannel);
  }
}
