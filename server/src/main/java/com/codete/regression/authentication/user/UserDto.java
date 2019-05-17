package com.codete.regression.authentication.user;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserDto {
    private String apiKey;
    private boolean whiteTheme;

    public UserDto(User user) {
        this.apiKey = user.getApiKey();
        this.whiteTheme = user.isWhiteTheme();
    }
}
