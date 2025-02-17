package com.mmsm.streamingplatform.comment.commentrating;

import com.mmsm.streamingplatform.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@RestController
@RequestMapping("/videos/{videoId}/comments/{commentId}")
@AllArgsConstructor
public class CommentRatingController {

    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    static class CanOnlyBePerformedByVideoAuthorException extends RuntimeException {
        CanOnlyBePerformedByVideoAuthorException() {
            super("This action can only be performed by the author");
        }
    }

    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    static class NotDirectVideoCommentException extends RuntimeException {
        NotDirectVideoCommentException() {
            super("This action can only be performed on a direct video comment");
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class CommentFavouriteDto {
        private Long favouriteCount;
        private Boolean isFavourite;
        private Boolean isVideoAuthorFavourite;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentRatingRepresentation {
        private Long commentId;
        private Boolean isUpVote;
        private Boolean isDownVote;
        private Boolean isFavourite;
    }

    private final CommentRatingService commentRatingService;

    @PostMapping("/up-vote")
    CommentRatingRepresentation upVoteComment(@PathVariable Long videoId, @PathVariable Long commentId,
                                              HttpServletRequest request) {
        String userId = SecurityUtils.getUserIdFromRequest(request);
        log.info("UP VOTE COMMENT [userId = {}, videoId = {}, commentId = {}]", userId, videoId, commentId);
        return commentRatingService.upVoteComment(commentId, userId);
    }

    @PostMapping("/down-vote")
    CommentRatingRepresentation downVoteComment(@PathVariable Long videoId, @PathVariable Long commentId,
                                                HttpServletRequest request) {
        String userId = SecurityUtils.getUserIdFromRequest(request);
        log.info("DOWN VOTE COMMENT [userId = {}, videoId = {}, commentId = {}]", userId, videoId, commentId);
        return commentRatingService.downVoteComment(commentId, userId);
    }

    @PostMapping("/favourite")
    CommentFavouriteDto favouriteComment(@PathVariable Long videoId, @PathVariable Long commentId,
                                         HttpServletRequest request) {
        String userId = SecurityUtils.getUserIdFromRequest(request);
        log.info("FAVOURITE COMMENT [userId = {}, videoId = {}, commentId = {}]", userId, videoId, commentId);
        return commentRatingService.favouriteComment(videoId, commentId, userId);
    }

    @PostMapping("/pin")
    void pinComment(@PathVariable Long videoId, @PathVariable Long commentId, HttpServletRequest request) {
        String userId = SecurityUtils.getUserIdFromRequest(request);
        log.info("PIN COMMENT [userId = {}, videoId = {}, commentId = {}]", userId, videoId, commentId);
        commentRatingService.pinComment(videoId, commentId, userId);
    }
}
