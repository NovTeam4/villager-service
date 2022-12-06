package com.example.villagerservice.config.security;

import com.example.villagerservice.common.jwt.JwtTokenProvider;
import com.example.villagerservice.config.redis.RedisRepository;
import com.example.villagerservice.config.security.filters.CustomAuthenticationFilter;
import com.example.villagerservice.config.security.filters.JwtAuthenticationFilter;
import com.example.villagerservice.config.security.filters.LocalAuthenticationFilter;
import com.example.villagerservice.config.security.handler.CustomFailureHandler;
import com.example.villagerservice.config.security.handler.CustomSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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
public class JwtLocalSecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisRepository redisRepository;
    private final Environment env;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        AuthenticationManager authenticationManager = authenticationManager(http.getSharedObject(AuthenticationConfiguration.class));
        CustomAuthenticationFilter authenticationFilter = createCustomAuthenticationFilter(authenticationManager);

        http
                .headers()
                .frameOptions()
                .sameOrigin()
        ;

        http
                .authorizeRequests()
                .antMatchers("/api/v1/auth/**", "/h2-console/**", "/docs/**")
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
                .addFilter(authenticationFilter)
        ;

        if(Boolean.parseBoolean(env.getProperty("jwt.active"))) {
            http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        } else {
            http.addFilterBefore(new LocalAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        }

        return http.build();
    }

    private CustomAuthenticationFilter createCustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        CustomAuthenticationFilter authenticationFilter = new CustomAuthenticationFilter(authenticationManager);
        authenticationFilter.setFilterProcessesUrl("/api/v1/auth/login");
        authenticationFilter.setAuthenticationFailureHandler(new CustomFailureHandler());
        authenticationFilter.setAuthenticationSuccessHandler(new CustomSuccessHandler(jwtTokenProvider, redisTemplate, redisRepository));
        return authenticationFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
        return authConfiguration.getAuthenticationManager();
    }
}
