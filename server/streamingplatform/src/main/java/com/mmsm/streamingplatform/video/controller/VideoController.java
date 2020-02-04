package com.mmsm.streamingplatform.video.controller;

import com.mmsm.streamingplatform.video.model.Video;
import com.mmsm.streamingplatform.video.model.VideoDto;
import com.mmsm.streamingplatform.video.service.VideoRepository;
import com.mmsm.streamingplatform.video.service.VideoService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
public class VideoController {

    private final VideoRepository videoRepository;
    private final VideoService videoService;

    @GetMapping("/videos")
    public List<VideoDto> getAllVideos() {
        return videoService.getAllVideoDtos();
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<InputStreamResource> getMeasurement(@PathVariable("id") Long id) throws FileNotFoundException {
        Optional<Video> video = videoRepository.findById(id);
        if (video.isEmpty()) {
            return null;
        }

        File file = new File(video.get().getPath());
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("video/mp4"))
                .body(resource);
    }
}
