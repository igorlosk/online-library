package dev.loskutnikov.onlinelibrary.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignInRequest(
        @NotBlank
        String login,
        @NotBlank
        String password
) {
}
