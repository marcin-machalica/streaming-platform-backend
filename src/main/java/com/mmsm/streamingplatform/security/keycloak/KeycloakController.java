package com.mmsm.streamingplatform.security.keycloak;

import com.mmsm.streamingplatform.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/keycloak")
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

    private final KeycloakService keycloakService;

    @GetMapping("/users")
    public List<UserDto> getAllUsers(HttpServletRequest request) {
        String userId = SecurityUtils.getUserIdFromRequest(request);
        log.info("GET ALL USERS [userId = {}]", userId);
        return keycloakService.getAllUserDtos();
    }
}
