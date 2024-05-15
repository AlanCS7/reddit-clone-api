package io.github.alancs7.redditclone.controller.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

@JsonInclude(Include.NON_NULL)
@Getter
@Builder
public class ProblemDetails {

    private Integer status;

    private OffsetDateTime timestamp;

    private String title;

    private String detail;

    private String userMessage;

    private List<Field> fields;

    @Getter
    @Builder
    public static class Field {

        private String name;

        private String userMessage;
    }
}