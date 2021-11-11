package com.github.aldebaranoro.apollonmusicresourceserver;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @GetMapping("/me")
    public User getCurrentUser(KeycloakPrincipal<KeycloakSecurityContext> principal) {
        return new User(principal.getKeycloakSecurityContext().getToken().getPreferredUsername());
    }
}
