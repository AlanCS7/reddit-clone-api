package io.github.alancs7.redditclone;

import org.testcontainers.containers.MySQLContainer;

public class BaseTest {
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:latest")
            .withDatabaseName("reddit_clone_test_db")
            .withUsername("test")
            .withPassword("test");

    static {
        mySQLContainer.start();
    }
}
