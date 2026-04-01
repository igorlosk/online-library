package dev.loskutnikov.onlinelibrary.users;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User registerUser(SignUpRequest signUpRequest) {
        if (userRepository.existsByLogin(signUpRequest.login())) {
            throw new IllegalArgumentException("Username already taken");
        }
        var userToSave = new UserEntity(
                null,
                signUpRequest.login(),
                signUpRequest.password(),
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
