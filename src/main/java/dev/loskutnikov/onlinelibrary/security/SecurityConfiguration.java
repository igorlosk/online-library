package dev.loskutnikov.onlinelibrary.security;

import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

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
//                                .requestMatchers(HttpMethod.POST, "/authors")
//                                .hasAnyAuthority("ADMIN")
                                .anyRequest().authenticated())
                // не создавать сессии
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // аутентификация по протоколу HttpBasic
                .httpBasic(Customizer.withDefaults())
                .build();
    }
}
