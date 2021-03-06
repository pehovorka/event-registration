package com.cscu9yw.eventregistrationbackend.security;

import com.cscu9yw.eventregistrationbackend.filter.CustomAuthenticationFilter;
import com.cscu9yw.eventregistrationbackend.filter.CustomAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private Environment environment;

    public SecurityConfig(UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean(), environment);
        // Change the URL of the admin login endpoint from default '/login'.
        customAuthenticationFilter.setFilterProcessesUrl("/admin/login");

        // Our sessions are stateless, we use JWT. For that reason, CSRF is disabled.
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.cors();

        // Set default HTTP status 401 for not logged in users.
        http.exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));


        // Allow listing events for anybody.
        http.authorizeHttpRequests().antMatchers(HttpMethod.GET, "/events/**").permitAll();

        // Allow registering random user (attendee) for anybody.
        http.authorizeHttpRequests().antMatchers(HttpMethod.POST, "/users").permitAll();

        // Allow requesting user (attendee) data for anybody (they need to know the user UID).
        http.authorizeHttpRequests().antMatchers(HttpMethod.GET, "/users/*").permitAll();

        // Allow creating and deleting registrations for anybody (they need to know the user UID).
        http.authorizeHttpRequests().antMatchers(HttpMethod.POST, "/registrations").permitAll();
        http.authorizeHttpRequests().antMatchers(HttpMethod.DELETE, "/registrations").permitAll();

        // Enable access to refresh token EP for anybody (the refresh token is validated in AdminController).
        http.authorizeHttpRequests().antMatchers(HttpMethod.GET, "/admin/token/refresh").permitAll();

        // Enable access to API docs to anybody
        http.authorizeHttpRequests().antMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll();
        http.authorizeHttpRequests().antMatchers(HttpMethod.GET, "/api-docs/**").permitAll();

        // Allow everything else only for administrators.
        http.authorizeHttpRequests().anyRequest().authenticated();

        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(environment), UsernamePasswordAuthenticationFilter.class);

        // Set 'X-Frame-Options' to 'SAMEORIGIN' in order for H2 console to work.
        http.headers().frameOptions().sameOrigin();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        // Enable all for debug purposes.
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
