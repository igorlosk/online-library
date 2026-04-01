package dev.loskutnikov.onlinelibrary.users;

public record User(
        Long id,
        String login,
        UserRole role
) {
}
