package com.mmsm.streamingplatform.video;

import com.mmsm.streamingplatform.channel.ChannelController.ChannelIdentity;
import com.mmsm.streamingplatform.comment.CommentController;
import com.mmsm.streamingplatform.utils.ControllerUtils;
import com.mmsm.streamingplatform.utils.SecurityUtils;
import com.mmsm.streamingplatform.video.Video.VideoVisibility;
import com.mmsm.streamingplatform.video.videorating.VideoRatingController;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.NotSupportedException;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;


@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/videos")
public class VideoController {

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public static class VideoNotFoundException extends RuntimeException {
        public VideoNotFoundException(Long id) {
            super("Video not found with id: " + id);
        }
    }

    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    static class CannotDeleteFileException extends RuntimeException {
        CannotDeleteFileException() {
            super("Error during deleting the file");
        }
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    static class NotSupportedVideoFormatException extends RuntimeException {
        NotSupportedVideoFormatException() {
            super("Not supported video extension");
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class VideoDetails {
        private Long id;
        private ChannelIdentity channelIdentity;
        private String authorId;
        private String title;
        private String description;
        private Long upVoteCount;
        private Long downVoteCount;
        private Long viewCount;
        private Long shareCount;
        private Instant createdDate;
        private VideoRatingController.VideoRatingRepresentation currentUserVideoRating;
        private List<CommentController.CommentRepresentation> directCommentDtos;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VideoRepresentation {
        private Long id;
        private ChannelIdentity channelIdentity;
        private String title;
        private String description;
        private Instant createdDate;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VideoUpdate {
        private String description;
    }

    private final VideoService videoService;

    @GetMapping
    public List<VideoRepresentation> getAllVideos(HttpServletRequest request) {
        String userId = SecurityUtils.getUserIdFromRequest(request);
        log.info("GET ALL VIDEOS [userId = {}]", userId);
        return videoService.getAllVideos(userId);
    }

    @GetMapping("/{videoId}")
    public VideoDetails getVideoDetails(@PathVariable Long videoId, HttpServletRequest request) {
        String userId = SecurityUtils.getUserIdFromRequest(request);
        log.info("GET VIDEO DETAILS [userId = {}, videoId = {}]", userId, videoId);
        return videoService.getVideoDetails(videoId, userId);
    }

    @PostMapping
    public ResponseEntity<VideoRepresentation> createVideo(@RequestParam MultipartFile file, @RequestParam String title,
                @RequestParam String description, @RequestParam VideoVisibility visibility, HttpServletRequest request)
                    throws URISyntaxException, IOException, NotSupportedException {

        String userId = SecurityUtils.getUserIdFromRequest(request);
        log.info("CREATE VIDEO [userId = {}, title = {}, visibility = {}]", userId, title, visibility);
        VideoRepresentation videoRepresentation = videoService.createVideo(file, title, description, visibility, userId);
        URI uri = new URI("/api/v1/videos/" + videoRepresentation.getId());
        return ControllerUtils.getCreatedResponse(videoRepresentation, uri);
    }

    @PutMapping("/{videoId}")
    public VideoRepresentation updateVideo(@RequestBody VideoUpdate videoUpdate, @PathVariable Long videoId, HttpServletRequest request) {
        String userId = SecurityUtils.getUserIdFromRequest(request);
        log.info("UPDATE VIDEO [userId = {}, videoId = {}]", userId, videoId);
        return videoService.updateVideo(videoUpdate, videoId, userId);
    }

    @PutMapping("/{videoId}/up-view-count")
    public void upViewCount(@PathVariable Long videoId, HttpServletRequest request) {
        String userId = SecurityUtils.getUserIdFromRequest(request);
        log.info("UP VIEW COUNT [userId = {}, videoId = {}]", userId, videoId);
        videoService.upViewCount(videoId);
    }

    @DeleteMapping("/{videoId}")
    public void deleteVideoById(@PathVariable Long videoId, HttpServletRequest request) {
        String userId = SecurityUtils.getUserIdFromRequest(request);
        log.info("DELETE VIDEO [userId = {}, videoId = {}]", userId, videoId);
        videoService.deleteVideoById(videoId, userId);
    }

    @GetMapping("/{videoId}/download")
    public ResponseEntity<InputStreamResource> downloadVideoById(@PathVariable Long videoId,
            HttpServletRequest request) throws FileNotFoundException {

        String userId = SecurityUtils.getUserIdFromRequest(request);
        log.info("DOWNLOAD VIDEO [userId = {}, videoId = {}]", userId, videoId);

        Pair<File, String> fileAndFilename = videoService.getFileAndFilenameWithExtension(videoId);
        File file = fileAndFilename.getFirst();
        String filename = fileAndFilename.getSecond();

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
            .contentLength(file.length())
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .header("Content-Disposition", "attachment; filename=" + filename)
            .body(resource);
    }
}
