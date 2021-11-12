package com.github.aldebaranoro.apollonmusicresourceserver.api.utils;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;

public class KeycloakPrincipalUtils {

    public static String getIdentityProviderId(KeycloakPrincipal<KeycloakSecurityContext> principal) {
        return principal
                .getKeycloakSecurityContext()
                .getToken()
                .getOtherClaims()
                .get("identity_provider_identity")
                .toString();
    }

    public static String getUserId(KeycloakPrincipal<KeycloakSecurityContext> principal) {
        return principal.getName();
    }
}
