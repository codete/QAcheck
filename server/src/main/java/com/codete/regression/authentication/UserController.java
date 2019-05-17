package com.codete.regression.authentication;

import com.codete.regression.authentication.exception.UserNotFoundException;
import com.codete.regression.authentication.user.UserDto;
import com.codete.regression.authentication.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public UserDto getCurrentUser() {
        return userService.getCurrentUser()
                .map(UserDto::new)
                .orElseThrow(UserNotFoundException::new);
    }

    @PutMapping("/me/save")
    public void saveCurrentUserSettings(@RequestBody UserDto userDto) {
        userService.getCurrentUser()
                .ifPresent(user -> userService.saveUserSettings(user, userDto));
    }
}
