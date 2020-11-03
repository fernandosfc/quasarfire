package com.mercadolibre.quasarfire.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	@Override
	public UserDetailsService userDetailsService() {
		@SuppressWarnings("deprecation")
		UserDetails user = User.withDefaultPasswordEncoder()
				.username("user")
				.password("password")
				.roles("ADMIN")
				.build();
		return new InMemoryUserDetailsManager(user);
	}
	
    // Secure the endpoins with HTTP Basic authentication
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.httpBasic()
	        .and()
	        .authorizeRequests()
	        .antMatchers(HttpMethod.POST, "/topsecret/").hasRole("ADMIN")
	        .antMatchers(HttpMethod.GET, "/topsecret_split/").hasAnyRole("CCO", "ADMIN")
	        .antMatchers(HttpMethod.POST, "/topsecret_split/**").hasAnyRole("SATELLITE", "ADMIN")
	        .and()
	        .csrf().disable()
	        .formLogin().disable();
    }

}