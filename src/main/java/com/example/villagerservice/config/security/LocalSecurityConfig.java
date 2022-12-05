package com.example.villagerservice.config.security;

import com.example.villagerservice.common.jwt.JwtTokenProvider;
import com.example.villagerservice.config.security.filters.LocalAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Profile(value = {"local"})
public class LocalSecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .headers()
                .frameOptions()
                .sameOrigin()
        ;

        http
                .authorizeRequests()
                .antMatchers("/api/v1/auth/**", "/h2-console/**")
                .permitAll()
                .anyRequest()
                .authenticated();

        http
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .addFilterBefore(new LocalAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }
}
