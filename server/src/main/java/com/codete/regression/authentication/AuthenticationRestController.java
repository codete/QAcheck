package com.codete.regression.authentication;

import com.codete.regression.authentication.user.User;
import com.codete.regression.authentication.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationRestController {

    private final UserService userService;

    public AuthenticationRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/signup", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userService.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }
        if (userService.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>("Email is already in use!", HttpStatus.BAD_REQUEST);
        }
        User user = userService.createUser(signUpRequest);
        return ResponseEntity.ok(user.getApiKey());
    }
}
