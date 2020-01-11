package ee.taltech.agonaudid.configuration;

import ee.taltech.agonaudid.auth.CustomAuthenticationProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final CustomAuthenticationProvider authProvider;

	public WebSecurityConfiguration(CustomAuthenticationProvider authProvider) {
		this.authProvider = authProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.ALWAYS) // No session will be created or used by spring security
				.and()
				.httpBasic()
				.and()
				.authorizeRequests()
				.antMatchers("/api/hello")
				.permitAll()
				.antMatchers("/api/users/**")
				.permitAll() // allow every URI, that begins with '/api/user/'
				.antMatchers("/api/login")
				.authenticated()
				// .anyRequest().authenticated() // protect all other requests
				.and()
				.csrf()
				.disable(); // disable cross site request forgery, as we don't use cookies - otherwise ALL
		// PUT, POST, DELETE will get HTTP 403!
	}

	// @Override
	// protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	//    auth.inMemoryAuthentication()
	//            .withUser("foo").password("{noop}bar").roles("USER");
	// }
}
