package dev.loskutnikov.onlinelibrary.security;

import dev.loskutnikov.onlinelibrary.web.ServerErrorDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

    private final ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException, ServletException {
        logger.error("Handling authentication exception", authException);

        var messageResponse = new ServerErrorDto(
                "Failed to authenticate",
                authException.getMessage(),
                LocalDateTime.now()
        );

        var stringResponse = objectMapper.writeValueAsString(messageResponse);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // 401 error
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(stringResponse);

    }
}
