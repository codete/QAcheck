package com.codete.regression.testengine.userapp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
class UserAppDto {
    private Long id;
    private String appName;

    UserAppDto(UserApp userApp) {
        this.id = userApp.getId();
        this.appName = userApp.getAppName();
    }
}
