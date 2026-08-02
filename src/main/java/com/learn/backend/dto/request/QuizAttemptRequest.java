package com.learn.backend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizAttemptRequest {

    @NotNull
    private UUID userId;

    @NotNull
    private UUID deckId;

    @Min(0)
    private int score;

    @NotBlank
    private String answersJson;
}
