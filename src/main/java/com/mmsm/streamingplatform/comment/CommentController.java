package com.mmsm.streamingplatform.comment;

import com.mmsm.streamingplatform.channel.ChannelController.ChannelIdentity;
import com.mmsm.streamingplatform.comment.commentrating.CommentRatingController;
import com.mmsm.streamingplatform.comment.commentrating.CommentRatingController.CommentRatingRepresentation;
import com.mmsm.streamingplatform.security.keycloak.KeycloakController.UserDto;
import com.mmsm.streamingplatform.utils.ControllerUtils;
import com.mmsm.streamingplatform.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/videos/{videoId}/comments")
public class CommentController {

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public static class CommentNotFoundException extends RuntimeException {
        public CommentNotFoundException(Long id) {
            super("Comment not found with id: " + id);
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentRepresentation {
        private Long id;
        private Long parentId;
        private ChannelIdentity channelIdentity;
        private String authorId;
        private String message;
        private Long upVoteCount;
        private Long downVoteCount;
        private Long favouriteCount;
        private Integer directRepliesCount;
        private Integer allRepliesCount;
        private Boolean isVideoAuthorFavourite;
        private Boolean isPinned;
        private Boolean wasEdited;
        private Boolean isDeleted;
        private Instant dateCreated;
        private CommentRatingController.CommentRatingRepresentation currentUserCommentRating;
        private List<CommentRepresentation> directReplies;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentWithRepliesAndAuthors {
        private Comment comment;
        private UserDto author; // todo
        private CommentRatingRepresentation commentRatingRepresentation;
        private List<CommentWithRepliesAndAuthors> commentsAndAuthors;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveComment {
        private Long parentId;
        private String message;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentUpdate {
        private String message;
    }

    private final CommentService commentService;

    @GetMapping("/{commentId}")
    CommentRepresentation getCommentDtoWithReplies(@PathVariable Long videoId, @PathVariable Long commentId, HttpServletRequest request) {
        String userId = SecurityUtils.getUserIdFromRequest(request);
        log.info("GET COMMENT DTO WITH REPLIES [userId = {}, videoId = {}, commentId = {}]", userId, videoId, commentId);
        return commentService.getCommentDtoWithReplies(commentId, userId);
    }

    @PostMapping
    ResponseEntity<CommentRepresentation> saveComment(@RequestBody SaveComment saveComment, @PathVariable Long videoId,
                                                      HttpServletRequest request) throws URISyntaxException {
        String userId = SecurityUtils.getUserIdFromRequest(request);
        log.info("SAVE COMMENT [userId = {}, videoId = {}, parentId = {}]", userId, videoId, saveComment.getParentId());

        CommentRepresentation savedComment = commentService.saveComment(saveComment, videoId, userId);
        URI uri = savedComment != null ? new URI("/api/v1/videos/" + videoId + "/comments/" + savedComment.getId()) : null;
        return ControllerUtils.getCreatedResponse(savedComment, uri);
    }

    @PutMapping("/{commentId}")
    CommentRepresentation updateComment(@RequestBody CommentUpdate commentUpdate, @PathVariable Long videoId,
                                        @PathVariable Long commentId, HttpServletRequest request) {
        String userId = SecurityUtils.getUserIdFromRequest(request);
        log.info("UPDATE COMMENT [userId = {}, videoId = {}, commentId = {}]", userId, videoId, commentId);
        return commentService.updateComment(commentUpdate, commentId, userId);
    }

    @DeleteMapping("/{commentId}")
    void deleteComment(@PathVariable Long videoId, @PathVariable Long commentId, HttpServletRequest request) {
        String userId = SecurityUtils.getUserIdFromRequest(request);
        log.info("DELETE COMMENT [userId = {}, videoId = {}, commentId = {}]", userId, videoId, commentId);
        commentService.deleteCommentById(videoId, commentId, userId);
    }
}
