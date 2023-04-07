package de.webenm.mdbconnector;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Value("${mdbconnector.restricttolocalhost:true}")
	private boolean restrictToLocalhost;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		if (restrictToLocalhost) {
			http.authorizeRequests()
				.antMatchers("/**")
				.access("hasIpAddress(\"127.0.0.1\") or hasIpAddress(\"::1\")");
		}
	}
}
