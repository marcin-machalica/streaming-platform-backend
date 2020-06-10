package com.mmsm.streamingplatform.channel;

import com.mmsm.streamingplatform.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@AllArgsConstructor
@RestController
public class ChannelGuardController {

    private final ChannelService channelService;

    @GetMapping("/is-channel-created")
    public Boolean isChannelCreated(HttpServletRequest request) {
        String userId = SecurityUtils.getUserIdFromRequest(request);
        log.info("GET IS CHANNEL CREATED [userId = {}]", userId);
        return channelService.isChannelCreated(userId);
    }
}
