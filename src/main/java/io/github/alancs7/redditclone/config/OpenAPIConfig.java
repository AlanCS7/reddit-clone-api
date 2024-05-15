package io.github.alancs7.redditclone.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Reddit Clone API")
                        .description("API for Reddit Clone Application")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Alan Silva")
                                .url("https://github.com/AlanCS7")
                                .email("alancss.contact@gmail.com")))
                .servers(List.of(new Server()
                        .url("http://localhost:8080")))
                .externalDocs(new ExternalDocumentation()
                        .description("Reddit Clone API on GitHub")
                        .url("https://github.com/AlanCS7/reddit-clone-api"));
    }
}
