package dev.loskutnikov.onlinelibrary.users;

import jakarta.validation.Valid;
import org.slf4j.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UserService userService;

    private final Logger log = LoggerFactory.getLogger(UsersController.class);

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        log.info("Get request for sing-uo: login={}", signUpRequest.login());
        var user = userService.registerUser(signUpRequest);
    }
}
