package com.example.spring_task_manager.service;

import com.example.spring_task_manager.dto.RegisterRequest;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeycloakService {

    private final Keycloak keycloak;

    public KeycloakService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public String createUser(RegisterRequest request) {

        UserRepresentation user = new UserRepresentation();

        user.setEmail(request.email());
        user.setEnabled(true);

        CredentialRepresentation password = new CredentialRepresentation();
        password.setType(CredentialRepresentation.PASSWORD);
        password.setValue(request.password());
        password.setTemporary(false);

        user.setCredentials(List.of(password));

        Response response = keycloak
                .realm("task-manager")
                .users()
                .create(user);

        RealmResource realmResource = keycloak.realm("task-manager");
        String keycloakUserId = CreatedResponseUtil.getCreatedId(response);

        RoleRepresentation role = realmResource
                .roles()
                .get(request.position())
                .toRepresentation();
        realmResource
                .users()
                .get(keycloakUserId)
                .roles()
                .realmLevel()
                .add(List.of(role));



        return keycloakUserId;
    }

    public void deleteUser(String keycloakUserId) {
        keycloak
                .realm("task-manager")
                .users()
                .delete(keycloakUserId)
                .close();
    }
}
