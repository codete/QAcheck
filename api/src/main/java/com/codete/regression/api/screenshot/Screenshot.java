package com.codete.regression.api.screenshot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Screenshot {

    @NotNull
    private String stepName;

    @NotNull
    private byte[] bytes;
}
