package com.demo1.rates1.config;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	 @Override
	 protected void configure(final HttpSecurity http) throws Exception {
		
		http
				.authorizeRequests()
		        .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ADMIN") 		// Securing Spring Actuator
				.antMatchers("/","/index", "/login", "/resources/**", "/static/**", "/css/**", "/js/**", "/images/**").permitAll()
				.anyRequest().authenticated().and()
				.formLogin().loginPage("/login").failureUrl("/login?error=true").defaultSuccessUrl("/home").and()
				.logout().clearAuthentication(true) 
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login")
				.deleteCookies("JSESSIONID").invalidateHttpSession(true)
				.permitAll().and().exceptionHandling()
				.and().headers().xssProtection().and()
				
				// https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Security-Policy
				.contentSecurityPolicy(						
						  "img-src 'self' data:; " // Images location
						+ "script-src 'self' ;"    // JS files location
						+ "style-src 'self'; "     // CSS files location 
						+ "font-src 'self'; "	   // Font files location (e.g fontawesome icons)
						+ "default-src 'self';");  // Serves as a fallback for the other fetch directives.
			
		
		/**
				.contentSecurityPolicy(						
						  "img-src 'self' cdnjs.cloudflare.com data:; " // Images location
						+ "script-src 'self' cdn.jsdelivr.net;"    		// JS files location
						+ "style-src 'self' cdnjs.cloudflare.com; "     // CSS files location 
						+ "font-src 'self' cdnjs.cloudflare.com; "	    // Font files location (e.g fontawesome icons)
						+ "default-src 'self';");  // Serves as a fallback for the other fetch directives.
		

		// If loading Bootstrap, JQuery, Fontawesome 6 from CDN
		 * 
		<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.1/jquery.min.js" integrity="sha512-aVKKRRi/Q/YV+4mjoKBsE4x3H+BkegoM/em46NNlCqNTmUYADjBbeNefNxYV7giUp0VxICtqdrbqU7iVaeZNXA==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
		<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-gH2yIJqKdNHPEq0n4Mqa/HGKIhSkIHeL5AyhkYV8i59U5AR6csBvApHHNl/vI1Bx" crossorigin="anonymous">
		<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-A3rJD856KowSb7dwlZdYEkO39Gagi7vIsF0jrRAoQmDKKtQBHUuLZ9AsSv4jD4Xa" crossorigin="anonymous"></script>
				
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.0/css/fontawesome.min.css" integrity="sha512-RvQxwf+3zJuNwl4e0sZjQeX7kUa3o82bDETpgVCH2RiwYSZVDdFJ7N/woNigN/ldyOOoKw8584jM4plQdt8bhA==" crossorigin="anonymous" referrerpolicy="no-referrer" />
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.0/css/solid.min.css" integrity="sha512-uj2QCZdpo8PSbRGL/g5mXek6HM/APd7k/B5Hx/rkVFPNOxAQMXD+t+bG4Zv8OAdUpydZTU3UHmyjjiHv2Ww0PA==" crossorigin="anonymous" referrerpolicy="no-referrer" />
		**/

	 }

	 /**
	  * using in memory same users for demo purposes only  
	  */
	 @Override
	  protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
	    auth.inMemoryAuthentication()
	        .withUser("user1").password(passwordEncoder().encode("password1")).roles("USER")
	        .and()
	        .withUser("admin1").password(passwordEncoder().encode("password1")).roles("ADMIN");
	  }
	  
	  @Bean
	  public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	  }
	
}


/**
 scrape_configs:
  - job_name: 'prometheus-spring'
    scrape_interval: 1m
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['my.local.machine:8080']
    basic_auth:
      username: "test-user"
      password: "test-password"
**/