package com.mmsm.streamingplatform;

import com.mmsm.streamingplatform.video.model.Movie;
import com.mmsm.streamingplatform.video.service.MovieRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.transaction.Transactional;

@SpringBootApplication
public class StreamingPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(StreamingPlatformApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(MovieRepository movieRepository) {
        return (args) -> {

            {
                loadData(movieRepository);
            }

        };

    }

    @Transactional
    public void loadData(MovieRepository movieRepository) {
        Movie movie1 = new Movie();
        Movie movie2 = new Movie();
        Movie movie3 = new Movie();
        movie1.setPath("aaaa");
        movie2.setPath("bbb");
        movie3.setPath("ccc");
        movieRepository.save(movie2);
        movieRepository.save(movie1);
        movieRepository.save(movie3);
    }

}
