package com.mmsm.streamingplatform.video.videorating;

import com.mmsm.streamingplatform.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/videos/{videoId}")
public class VideoRatingController {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VideoRatingRepresentation {
        private Boolean isUpVote;
        private Boolean isDownVote;
    }

    private final VideoRatingService videoRatingService;

    @PostMapping("/up-vote")
    public VideoRatingRepresentation upVoteVideo(@PathVariable Long videoId, HttpServletRequest request) {
        String userId = SecurityUtils.getUserIdFromRequest(request);
        log.info("UP VOTE VIDEO [userId = {}, videoId = {}]", userId, videoId);
        return videoRatingService.upVoteVideo(videoId, userId);
    }

    @PostMapping("/down-vote")
    public VideoRatingRepresentation downVoteVideo(@PathVariable Long videoId, HttpServletRequest request) {
        String userId = SecurityUtils.getUserIdFromRequest(request);
        log.info("DOWN VOTE VIDEO [userId = {}, videoId = {}]", userId, videoId);
        return videoRatingService.downVoteVideo(videoId, userId);
    }
}
