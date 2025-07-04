package com.sleepkqq.sololeveling.ui.config

import com.sleepkqq.sololeveling.ui.view.auth.AuthView
import com.vaadin.flow.spring.security.VaadinWebSecurity
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher

@Configuration
@EnableWebSecurity
class SecurityConfig : VaadinWebSecurity() {

	@Throws(Exception::class)
	override fun configure(http: HttpSecurity) {
		http
			.anonymous { it.disable() }
			.sessionManagement {
				it.sessionFixation()
					.migrateSession()
					.maximumSessions(1)
					.expiredUrl("/login?expired")
			}
			.authorizeHttpRequests {
				it.requestMatchers(
					PathPatternRequestMatcher.withDefaults().matcher("/public/**")
				)
					.permitAll()
			}

		super.configure(http)

		setLoginView(http, AuthView::class.java)
	}
}
