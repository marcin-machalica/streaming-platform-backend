package com.mmsm.streamingplatform.channel;

import com.mmsm.streamingplatform.utils.ControllerUtils;
import com.mmsm.streamingplatform.utils.SecurityUtils;
import com.mmsm.streamingplatform.video.Video;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mmsm.streamingplatform.video.VideoController.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.NotSupportedException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
public class ChannelController {

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    static class ChannelNotFoundByNameException extends RuntimeException {
        ChannelNotFoundByNameException(String name) {
            super("Channel not found with name: " + name);
        }
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public static class ChannelNotFoundByUserIdException extends RuntimeException {
        public ChannelNotFoundByUserIdException(String userId) {
            super("Channel not found with userId: " + userId);
        }
    }

    @ResponseStatus(code = HttpStatus.CONFLICT)
    static class ChannelAlreadyCreatedException extends RuntimeException {
        ChannelAlreadyCreatedException() {
            super("Channel was already created by the user");
        }
    }

    @ResponseStatus(code = HttpStatus.CONFLICT)
    static class ChannelNameAlreadyExistsException extends RuntimeException {
        ChannelNameAlreadyExistsException() {
            super("Channel's name has to be unique");
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class ChannelAbout {
        private String name;
        private Boolean isAuthor;
        private String description;
        private Long subscriptionCount;
        private Instant createdDate;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class ChannelUpdate {
        private String name;
        private String description;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChannelIdentity {
        private String name;
    }

    private final ChannelService channelService;

    @GetMapping("/current-channel")
    public ChannelIdentity getChannelIdentity(HttpServletRequest request) {
        String userId = SecurityUtils.getUserIdFromRequest(request);
        log.info("GET CURRENT CHANNEL IDENTITY [userId = {}]", userId);
        return channelService.getChannelIdentity(userId);
    }

    @GetMapping("/channels/{channelName}/videos")
    public List<VideoRepresentation> getAllVideos(@PathVariable String channelName, HttpServletRequest request) {
        String userId = SecurityUtils.getUserIdFromRequest(request);
        log.info("GET ALL CHANNEL VIDEOS [userId = {}, channelName = {}]", userId, channelName);
        return channelService.getAllVideos(channelName);
    }

    @GetMapping("/channels/{channelName}")
    public ChannelAbout getChannelAbout(@PathVariable String channelName, HttpServletRequest request) {
        String userId = SecurityUtils.getUserIdFromRequest(request);
        log.info("GET CHANNEL ABOUT [userId = {}, channelName = {}]", userId, channelName);
        return channelService.getChannelAbout(channelName, userId);
    }

    @PostMapping("/channels")
    public ResponseEntity<ChannelAbout> createChannel(@RequestBody ChannelUpdate channelUpdate,
                                                      HttpServletRequest request) throws URISyntaxException {
        String userId = SecurityUtils.getUserIdFromRequest(request);
        log.info("CREATE CHANNEL [userId = {}, channelName = {}]", userId, channelUpdate.getName());
        ChannelAbout channelAbout = channelService.createChannel(channelUpdate, userId);
        URI uri = new URI("/api/v1/videos/" + channelAbout.getName());
        return ControllerUtils.getCreatedResponse(channelAbout, uri);
    }

    @PutMapping("/channels/{channelName}")
    public ChannelAbout updateChannel(@RequestBody ChannelUpdate channelUpdate, @PathVariable String channelName,
                                      HttpServletRequest request) {
        String userId = SecurityUtils.getUserIdFromRequest(request);
        log.info("UPDATE CHANNEL [userId = {}, channelName = {}, channelUpdateName = {}]", userId, channelName, channelUpdate.getName());
        return channelService.updateChannel(channelUpdate, channelName, userId);
    }

    @PostMapping("/channels/{channelName}/avatar")
    public void updateAvatar(@RequestParam MultipartFile file, @PathVariable String channelName,
                             HttpServletRequest request) throws IOException {
        String userId = SecurityUtils.getUserIdFromRequest(request);
        log.info("UPDATE AVATAR [userId = {}, channelName = {}]", userId, channelName);
        channelService.updateAvatar(channelName, file);
    }

    @GetMapping(value = "/channels/{channelName}/avatar", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getAvatar(@PathVariable String channelName, HttpServletRequest request) {
        String userId = SecurityUtils.getUserIdFromRequest(request);
        log.info("GET AVATAR [userId = {}, channelName = {}]", userId, channelName);
        return channelService.getAvatar(channelName);
    }

    @DeleteMapping("/channels/{channelName}")
    public void deleteChannelByName(@PathVariable String channelName, HttpServletRequest request) {
        String userId = SecurityUtils.getUserIdFromRequest(request);
        log.info("DELETE CHANNEL [userId = {}, channelName = {}]", userId, channelName);
        channelService.deleteChannelByName(channelName, userId);
    }
}
