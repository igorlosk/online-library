package dev.loskutnikov.onlinelibrary.users;

import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public User registerUser(SignUpRequest signUpRequest) {
        if (userRepository.existsByLogin(signUpRequest.login())) {
            throw new IllegalArgumentException("Username already taken");
        }
        var hashedPass = passwordEncoder.encode(signUpRequest.password());
        var userToSave = new UserEntity(
                null,
                signUpRequest.login(),
                hashedPass,
                UserRole.USER.name()
        );
        var saved = userRepository.save(userToSave);
        return new User(
                saved.getId(),
                saved.getLogin(),
                UserRole.valueOf(saved.getRole())
        );

    }
}
