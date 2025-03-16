package com.sleepkqq.sololeveling.config;

import com.sleepkqq.sololeveling.view.auth.AuthView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends VaadinWebSecurity {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .anonymous(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session
            .sessionFixation().migrateSession()
            .maximumSessions(1)
            .expiredUrl("/login?expired")
        )
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                new AntPathRequestMatcher("/public/**")
            )
            .permitAll()
        );

    super.configure(http);

    setLoginView(http, AuthView.class);
  }
}
