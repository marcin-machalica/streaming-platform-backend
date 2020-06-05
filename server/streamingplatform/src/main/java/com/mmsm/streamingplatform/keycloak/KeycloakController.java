package com.mmsm.streamingplatform.keycloak;

import com.mmsm.streamingplatform.channel.Channel;
import com.mmsm.streamingplatform.channel.ChannelRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
public class KeycloakController {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserDto {
        private String id;
        private String username;

        public static UserDto of(UserRepresentation userRepresentation) {
            return new UserDto(userRepresentation.getId(), userRepresentation.getUsername());
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserCreate {
        private String username;
        private String firstName;
        private String lastName;
        private String email;
        private String password;

        public UserRepresentation toUserRepresentation() {
            UserRepresentation user = new UserRepresentation();
            user.setUsername(username);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            return user;
        }

        public CredentialRepresentation toCredentialRepresentation() {
            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
            credentialRepresentation.setValue(password);
            credentialRepresentation.setTemporary(false);
            return credentialRepresentation;
        }
    }

    private final KeycloakService keycloakService;
    private final ChannelRepository channelRepository;

    @GetMapping("/users")
    public List<UserDto> getAllUsers() {
        System.out.println("------- test");
        return keycloakService.getAllUserDtos();
    }

    @PostMapping("/users/create")
    public UserDto createUser(@RequestBody UserCreate userCreate) {
        UserRepresentation userRepresentation = keycloakService.createUser(userCreate);
        channelRepository.save(Channel.of(userRepresentation.getId(), userRepresentation.getUsername(), null));
        return UserDto.of(userRepresentation);
        
        // todo - block option to create user from sign in

        // todo - antMatchers
    }
}
