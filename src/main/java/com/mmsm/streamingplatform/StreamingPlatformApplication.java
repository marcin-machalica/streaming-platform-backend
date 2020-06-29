package com.mmsm.streamingplatform;

import com.mmsm.streamingplatform.channel.Channel;
import com.mmsm.streamingplatform.channel.ChannelRepository;
import com.mmsm.streamingplatform.comment.Comment;
import com.mmsm.streamingplatform.video.Video;
import com.mmsm.streamingplatform.video.VideoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class StreamingPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(StreamingPlatformApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData(ChannelRepository channelRepository, VideoRepository videoRepository) {
        return (args) -> {
            Channel channel1 = Channel.of("test", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras tristique venenatis condimentum. Nam varius orci sed erat porttitor mollis. Etiam quam nibh, feugiat nec lectus mattis, porttitor blandit lectus. Nam vitae euismod mi, consectetur laoreet justo. In facilisis at metus sit amet posuere. Integer laoreet fermentum lorem, nec iaculis odio scelerisque ac. Donec feugiat et lectus vitae euismod. Sed ultricies non sem vel aliquet. Morbi ac sagittis elit, quis scelerisque ligula. Etiam diam magna, mattis a consequat semper, pretium ut risus. Nam vitae orci auctor, interdum libero eu, lobortis nisl. Curabitur ac nulla vel nunc vestibulum fringilla eget ut nulla. Ut sit amet tristique libero. Fusce dapibus erat at facilisis aliquam.");
            channel1.getAuditor().setCreatedById("ffe31293-6faf-4084-bce4-0ab3045e7339");

            Video video1 = Video.of("test1.mp4", "test1", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras tristique venenatis condimentum. Nam varius orci sed erat porttitor mollis. Etiam quam nibh, feugiat nec lectus mattis, porttitor blandit lectus. Nam vitae euismod mi, consectetur laoreet justo. In facilisis at metus sit amet posuere. Integer laoreet fermentum lorem, nec iaculis odio scelerisque ac. Donec feugiat et lectus vitae euismod. Sed ultricies non sem vel aliquet. Morbi ac sagittis elit, quis scelerisque ligula. Etiam diam magna, mattis a consequat semper, pretium ut risus. Nam vitae orci auctor, interdum libero eu, lobortis nisl. Curabitur ac nulla vel nunc vestibulum fringilla eget ut nulla. Ut sit amet tristique libero. Fusce dapibus erat at facilisis aliquam.", channel1);
            video1.setCreatedById("ffe31293-6faf-4084-bce4-0ab3045e7339");

            Video video2 = Video.of("test2.mp4", "test2", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras tristique venenatis condimentum. Nam varius orci sed erat porttitor mollis. Etiam quam nibh, feugiat nec lectus mattis, porttitor blandit lectus. Nam vitae euismod mi, consectetur laoreet justo. In facilisis at metus sit amet posuere. Integer laoreet fermentum lorem, nec iaculis odio scelerisque ac. Donec feugiat et lectus vitae euismod. Sed ultricies non sem vel aliquet. Morbi ac sagittis elit, quis scelerisque ligula. Etiam diam magna, mattis a consequat semper, pretium ut risus. Nam vitae orci auctor, interdum libero eu, lobortis nisl. Curabitur ac nulla vel nunc vestibulum fringilla eget ut nulla. Ut sit amet tristique libero. Fusce dapibus erat at facilisis aliquam.", channel1);
            video2.setCreatedById("ffe31293-6faf-4084-bce4-0ab3045e7339");

            Video video3 = Video.of("test3.mp4", "test3", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras tristique venenatis condimentum. Nam varius orci sed erat porttitor mollis. Etiam quam nibh, feugiat nec lectus mattis, porttitor blandit lectus. Nam vitae euismod mi, consectetur laoreet justo. In facilisis at metus sit amet posuere. Integer laoreet fermentum lorem, nec iaculis odio scelerisque ac. Donec feugiat et lectus vitae euismod. Sed ultricies non sem vel aliquet. Morbi ac sagittis elit, quis scelerisque ligula. Etiam diam magna, mattis a consequat semper, pretium ut risus. Nam vitae orci auctor, interdum libero eu, lobortis nisl. Curabitur ac nulla vel nunc vestibulum fringilla eget ut nulla. Ut sit amet tristique libero. Fusce dapibus erat at facilisis aliquam.", channel1);
            video3.setCreatedById("ffe31293-6faf-4084-bce4-0ab3045e7339");

            Comment comment1 = Comment.of("test comment 1", channel1);
            comment1.setCreatedById("ffe31293-6faf-4084-bce4-0ab3045e7339");

            Comment comment2 = Comment.of("test comment 2", channel1);
            comment2.setCreatedById("ffe31293-6faf-4084-bce4-0ab3045e7339");
            comment1.addChildrenComment(comment2);
            video1.addComment(comment1);

            channelRepository.save(channel1);
            videoRepository.saveAll(Arrays.asList(video1, video2, video3));
        };
    }
}
