package com.mmsm.streamingplatform.video;

import com.mmsm.streamingplatform.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/videos")
public class VideoGuardController {

    private final VideoService videoService;

    @GetMapping("/{videoId}/can-access")
    public Boolean canAccessVideo(@PathVariable Long videoId, HttpServletRequest request) {
        String userId = SecurityUtils.getUserIdFromRequest(request);
        log.info("CAN ACCESS VIDEO [userId = {}, videoId = {}]", userId, videoId);
        return videoService.canAccessVideo(videoId, userId);
    }
}


