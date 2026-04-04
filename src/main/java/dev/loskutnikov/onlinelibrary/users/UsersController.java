package dev.loskutnikov.onlinelibrary.users;

import dev.loskutnikov.onlinelibrary.security.jwt.AuthenticationService;
import jakarta.validation.Valid;
import org.slf4j.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UserService userService;

    private final Logger log = LoggerFactory.getLogger(UsersController.class);

    private final AuthenticationService authenticationService;

    public UsersController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        log.info("Get request for sing-uo: login={}", signUpRequest.login());
        var user = userService.registerUser(signUpRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new UserDto(
                        user.id(),
                        user.login()
                ));
    }

    @PostMapping("/auth")
    public ResponseEntity<JwtTokenResponse> authenticate(
            @Valid @RequestBody SignInRequest signInRequest
    ) {
        log.info("Get request for sing-in: login={}", signInRequest.login());
        var token = authenticationService.authenticateUser(signInRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new JwtTokenResponse(token));
    }
}
