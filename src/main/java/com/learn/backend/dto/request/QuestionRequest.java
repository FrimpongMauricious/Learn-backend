package com.learn.backend.dto.request;

import com.learn.backend.enums.Difficulty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Shape of a single question as produced by the Python question-generation
 * microservice's /generate-questions endpoint.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRequest {

    @NotBlank
    private String questionText;

    @NotNull
    @Size(min = 4, max = 4, message = "options must contain exactly 4 choices")
    private List<@NotBlank String> options;

    @NotBlank
    private String correctAnswer;

    @NotNull
    private Difficulty difficulty;

    private String topicTag;
}
