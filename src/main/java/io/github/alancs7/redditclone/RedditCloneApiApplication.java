package io.github.alancs7.redditclone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class RedditCloneApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedditCloneApiApplication.class, args);
    }

}
