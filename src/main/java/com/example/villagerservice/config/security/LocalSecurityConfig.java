package com.example.villagerservice.config.security;

import com.example.villagerservice.common.jwt.JwtTokenProvider;
import com.example.villagerservice.config.security.oauth2.CustomAuthorityMapper;
import com.example.villagerservice.config.security.oauth2.service.CustomOAuth2UserService;
import com.example.villagerservice.config.security.oauth2.service.CustomOidcUserService;
import com.example.villagerservice.config.redis.RedisRepository;
import com.example.villagerservice.config.security.filters.CustomAuthenticationFilter;
import com.example.villagerservice.config.security.filters.JwtAuthenticationFilter;
import com.example.villagerservice.config.security.filters.LocalAuthenticationFilter;
import com.example.villagerservice.config.security.handler.CustomFailureHandler;
import com.example.villagerservice.config.security.handler.CustomSuccessHandler;
import com.example.villagerservice.config.security.properties.CorsProperties;
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
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Profile(value = {"local", "town-test"})
public class LocalSecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final CorsProperties corsProperties;
    private final Environment env;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOidcUserService customOidcUserService;
    private final CustomSuccessHandler successHandler;
    private final CustomFailureHandler failureHandler;

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
                .cors()
                .and()
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

        http
                .oauth2Login(oauth2 ->
                        oauth2.userInfoEndpoint(userInfoEndpointConfig ->
                                userInfoEndpointConfig
                                        .userService(customOAuth2UserService)
                                        .oidcUserService(customOidcUserService)));
        http
                .oauth2Login()
                .successHandler(successHandler)
                .failureHandler(failureHandler);

        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource corsConfigSource = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedHeaders(Arrays.asList(corsProperties.getAllowedHeaders().split(",")));
        corsConfig.setAllowedMethods(Arrays.asList(corsProperties.getAllowedMethods().split(",")));
        corsConfig.setAllowedOrigins(Arrays.asList(corsProperties.getAllowedOrigins().split(",")));
        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(corsConfig.getMaxAge());

        corsConfigSource.registerCorsConfiguration("/**", corsConfig);
        return corsConfigSource;
    }

    private CustomAuthenticationFilter createCustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        CustomAuthenticationFilter authenticationFilter = new CustomAuthenticationFilter(authenticationManager);
        authenticationFilter.setFilterProcessesUrl("/api/v1/auth/login");
        authenticationFilter.setAuthenticationFailureHandler(failureHandler);
        authenticationFilter.setAuthenticationSuccessHandler(successHandler);
        return authenticationFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
        return authConfiguration.getAuthenticationManager();
    }

    @Bean
    public GrantedAuthoritiesMapper customAuthorityMapper() {
        return new CustomAuthorityMapper();
    }
}
