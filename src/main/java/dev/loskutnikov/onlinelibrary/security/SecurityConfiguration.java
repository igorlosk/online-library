package dev.loskutnikov.onlinelibrary.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    // цепочка фильтров перед тем как запрос попадает в контроллер
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        return http
                // отключение формы логин - пароль при входе
                .formLogin(AbstractHttpConfigurer::disable)
                // отключение защиты CSRF если есть frontend
                .csrf(AbstractHttpConfigurer::disable)
                // каждый запрос аунтифицирован
                .authorizeHttpRequests(
                        authorizeHttpRequests -> authorizeHttpRequests
                                .requestMatchers(HttpMethod.POST, "/authors")
                                .hasAnyAuthority("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/authors/**")
                                .hasAnyAuthority("ADMIN", "USER")
//                                .requestMatchers(HttpMethod.DELETE, "/authors/**")
//                                .hasAnyAuthority("ADMIN")

                                .requestMatchers(HttpMethod.POST, "/books")
                                .hasAnyAuthority("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/books/**")
                                .hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers(HttpMethod.DELETE, "/books/**")
                                .hasAnyAuthority("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/books/**")
                                .hasAnyAuthority("ADMIN")

                                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                                .requestMatchers(HttpMethod.POST, "/users/auth").permitAll()
                                .anyRequest().authenticated())


                // не создавать сессии
                .sessionManagement(
                        session ->
                                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(customAuthenticationEntryPoint)
                                .accessDeniedHandler(customAccessDeniedHandler))
                // аутентификация по протоколу HttpBasic
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        var authProvider = new DaoAuthenticationProvider(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
