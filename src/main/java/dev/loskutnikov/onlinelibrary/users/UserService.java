package dev.loskutnikov.onlinelibrary.users;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public User registerUser(@Valid SignUpRequest signUpRequest) {
        return null;
    }
}
