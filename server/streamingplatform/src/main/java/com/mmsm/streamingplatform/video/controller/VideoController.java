package com.mmsm.streamingplatform.video.controller;

import com.mmsm.streamingplatform.video.model.Video;
import com.mmsm.streamingplatform.video.model.VideoDto;
import com.mmsm.streamingplatform.video.service.VideoRepository;
import com.mmsm.streamingplatform.video.service.VideoService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/videos")
public class VideoController {

    private final VideoRepository videoRepository;
    private final VideoService videoService;

    @GetMapping("")
    public ResponseEntity<List<VideoDto>> getAllVideos() {
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(videoService.getAllVideoDtos());
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<InputStreamResource> downloadVideoById(@PathVariable("id") Long id) throws FileNotFoundException {
        Optional<Video> video = videoRepository.findById(id);
        if (video.isEmpty()) {
            return null;
        }

        File file = new File(video.get().getPath());
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
