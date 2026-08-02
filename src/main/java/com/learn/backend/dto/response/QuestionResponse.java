package com.learn.backend.dto.response;

import com.learn.backend.enums.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionResponse {

    private UUID id;
    private UUID deckId;
    private String questionText;
    private List<String> options;
    private String correctAnswer;
    private Difficulty difficulty;
    private String topicTag;
}
