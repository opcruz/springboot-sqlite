package com.demo.sqlite.security;

import com.demo.sqlite.utils.JWTAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity
@Configuration
class WebSecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(d -> d.requestMatchers(HttpMethod.POST, "/users/**").permitAll())
                .authorizeHttpRequests(d -> d.requestMatchers(HttpMethod.GET, "/swagger-ui/**", "/v3/api-docs/**").permitAll())
                .authorizeHttpRequests(d -> d.requestMatchers(HttpMethod.POST, "/stocks/**").hasAuthority("employee"))
                .authorizeHttpRequests(d -> d.requestMatchers(HttpMethod.PUT, "/stocks/**").hasAuthority("employee"))
                .authorizeHttpRequests(d -> d.requestMatchers(HttpMethod.DELETE, "/stocks/**").hasAuthority("employee"))
                .authorizeHttpRequests(d -> d.requestMatchers(HttpMethod.GET, "/stocks/**").permitAll())
                .authorizeHttpRequests(d -> d.requestMatchers("/carts/**").hasAuthority("client"))
                .authorizeHttpRequests(d -> d.requestMatchers("/orders/**").hasAuthority("client"))
                .authorizeHttpRequests(d -> d.anyRequest().authenticated())
                .build();
    }
}