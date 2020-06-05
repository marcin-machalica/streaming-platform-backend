package com.mmsm.streamingplatform.keycloak;

import com.mmsm.streamingplatform.keycloak.KeycloakController.*;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KeycloakService {

    private final Keycloak keycloak;
    private final RealmResource realmResource;

    @Value("${keycloak-api-user-id}")
    private String API_USER_ID;

    @Value(("${keycloak-admin-group-id}"))
    private String ADMIN_GROUP_ID;

    KeycloakService(@Value("${keycloak.auth-server-url}") String serverUrl,
                    @Value("${keycloak.realm}") String realm,
                    @Value("${keycloak-api-username}") String username,
                    @Value("${keycloak-api-password}") String password,
                    @Value("${keycloak-api-client}") String clientId) {
        this.keycloak = Keycloak.getInstance(serverUrl, realm, username, password, clientId);
        this.realmResource = keycloak.realm(realm);
    }

    public List<UserDto> getAllUserDtos() {
        return realmResource.users().list().stream()
            .filter(userRepresentation -> !userRepresentation.getId().equals(API_USER_ID))
            .filter(userRepresentation -> !isAdmin(userRepresentation))
            .map(UserDto::of)
            .collect(Collectors.toList());
    }

    public UserDto getUserDtoById(String id) {
        if (id == null) {
            return null;
        }
        return UserDto.of(realmResource.users().get(id).toRepresentation());
    }

    public boolean isAdmin(String userId) {
        return realmResource.groups().group(ADMIN_GROUP_ID).members().stream()
                .anyMatch(member -> member.getId().equals(userId));
    }

    private boolean isAdmin(UserRepresentation userRepresentation) {
        return realmResource.groups().group(ADMIN_GROUP_ID).members().stream()
                .anyMatch(member -> member.getId().equals(userRepresentation.getId()));
    }

    UserRepresentation createUser(UserCreate userCreate) {
        UserRepresentation user = userCreate.toUserRepresentation();
        user.setEnabled(true);

        UsersResource usersResource = realmResource.users();
        Response response = usersResource.create(user);
        String userId = CreatedResponseUtil.getCreatedId(response);

        CredentialRepresentation credentialRepresentation = userCreate.toCredentialRepresentation();
        UserResource userResource = usersResource.get(userId);
        userResource.resetPassword(credentialRepresentation);

        return userResource.toRepresentation();
    }
}
