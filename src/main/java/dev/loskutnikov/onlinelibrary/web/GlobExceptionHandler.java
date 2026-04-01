package dev.loskutnikov.onlinelibrary.web;

import jakarta.persistence.EntityNotFoundException;
import org.apache.tomcat.websocket.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobExceptionHandler {

    private final static Logger log = LoggerFactory.getLogger(GlobExceptionHandler.class);

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ServerErrorDto> handleValidationException(Exception e) {
        log.error("Got validation exception", e);

        String detailedMessage = e instanceof MethodArgumentNotValidException
                ? constractMethodArgumentNotValidMessage((MethodArgumentNotValidException) e)
                : e.getMessage();
        var newDto = new ServerErrorDto(
                "Ошибка валидации запроса",
                detailedMessage,
                LocalDateTime.now());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(newDto);
    }


    @ExceptionHandler()
    public ResponseEntity<ServerErrorDto> handleGenericException(Exception e) {
        log.error("Server error", e);
        var newDto = new ServerErrorDto(
                "Server error",
                e.getMessage(),
                LocalDateTime.now());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(newDto);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ServerErrorDto> handlerNotFoundException(EntityNotFoundException e) {
        log.error("Got exception", e);
        var newDto = new ServerErrorDto(
                "Сущность не найдена",
                e.getMessage(),
                LocalDateTime.now());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(newDto);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ServerErrorDto> handleAuthorisationException(AuthorizationDeniedException e) {
        log.error("Handle authorisation exception", e);
        var newDto = new ServerErrorDto(
                "Forbidden",
                e.getMessage(),
                LocalDateTime.now());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(newDto);
    }

    private static String constractMethodArgumentNotValidMessage(
            MethodArgumentNotValidException e) {
        return e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));
    }
}
