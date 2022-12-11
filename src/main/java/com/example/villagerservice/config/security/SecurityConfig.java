package com.example.villagerservice.config.security;

import com.example.villagerservice.common.jwt.JwtTokenProvider;
import com.example.villagerservice.config.redis.RedisRepository;
import com.example.villagerservice.config.security.filters.CustomAuthenticationFilter;
import com.example.villagerservice.config.security.filters.JwtAuthenticationFilter;
import com.example.villagerservice.config.security.handler.CustomFailureHandler;
import com.example.villagerservice.config.security.handler.CustomSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Profile(value = {"dev"})
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisRepository redisRepository;

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
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .addFilter(authenticationFilter)
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
        ;

        http
                .cors(AbstractHttpConfigurer::disable)
                ;


        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD","POST","GET","DELETE","PUT"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
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
