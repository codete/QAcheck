package com.codete.regression.testengine.userapp;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("user-app")
public class UserAppController {

    private final UserAppService userAppService;

    public UserAppController(UserAppService userAppService) {
        this.userAppService = userAppService;
    }

    @PreAuthorize("#username == authentication.name")
    @GetMapping
    public List<UserAppDto> getUserApps(@RequestParam("username") String username) {
        return userAppService.findAllByUserUsername(username)
                .stream()
                .map(UserAppDto::new)
                .collect(Collectors.toList());
    }

    @PreAuthorize("@userAppService.doesUserHaveAccess( #id, authentication.name )")
    @DeleteMapping("/{id}")
    public void deleteUserApp(@PathVariable Long id) {
        userAppService.deleteUserApp(id);
    }

}
