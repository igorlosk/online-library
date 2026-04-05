package dev.loskutnikov.onlinelibrary;

import dev.loskutnikov.onlinelibrary.security.jwt.JwtTokenManager;
import dev.loskutnikov.onlinelibrary.users.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserTestUtils {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenManager jwtTokenManager;

    public static final String DEFAULT_ADMIN_LOGIN = "admin";
    public static final String DEFAULT_USER_LOGIN = "user";
    private static volatile boolean isUserInitialized = false;

    public UserTestUtils(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenManager jwtTokenManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenManager = jwtTokenManager;
    }

    public String getJwtTokenWithRole(UserRole userRole){
        if(!isUserInitialized){
            initializedTestUsers();
            isUserInitialized = true;
        }
        return switch (userRole){
            case ADMIN -> jwtTokenManager.generateToken(DEFAULT_ADMIN_LOGIN);
            case USER ->  jwtTokenManager.generateToken(DEFAULT_USER_LOGIN);
        };
    }

    private void initializedTestUsers() {
        createUser(DEFAULT_ADMIN_LOGIN, "admin", UserRole.ADMIN);
        createUser(DEFAULT_USER_LOGIN, "user", UserRole.USER);
    }

    private void createUser(
            String login,
            String password,
            UserRole role
    ) {
        if (userRepository.existsByLogin(login)) {
            return;
        }
        var hashedPass = passwordEncoder.encode(password);
        var userToSave = new UserEntity(
                null,
                login,
                hashedPass,
                role.name()
        );
        userRepository.save(userToSave);
    }
}
