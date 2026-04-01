package dev.loskutnikov.onlinelibrary.users;

import jakarta.validation.constraints.*;

public record SignUpRequest(
        @NotBlank
        @Size(min = 5)
        String login,
        @NotBlank
        @Size(min = 5)
        String password
) {

}
