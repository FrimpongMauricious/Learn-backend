package com.learn.backend.service;

import com.learn.backend.dto.request.QuizAttemptRequest;
import com.learn.backend.dto.response.QuizAttemptResponse;
import com.learn.backend.entity.Deck;
import com.learn.backend.entity.QuizAttempt;
import com.learn.backend.entity.User;
import com.learn.backend.repository.QuizAttemptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuizAttemptService {

    private final QuizAttemptRepository quizAttemptRepository;
    private final UserService userService;
    private final DeckService deckService;

    @Transactional
    public QuizAttemptResponse submit(QuizAttemptRequest request) {
        User user = userService.getEntityById(request.getUserId());
        Deck deck = deckService.getEntityById(request.getDeckId());

        QuizAttempt attempt = QuizAttempt.builder()
                .user(user)
                .deck(deck)
                .score(request.getScore())
                .answersJson(request.getAnswersJson())
                .completedAt(Instant.now())
                .build();

        return toResponse(quizAttemptRepository.save(attempt));
    }

    @Transactional(readOnly = true)
    public List<QuizAttemptResponse> getByUser(UUID userId) {
        userService.getEntityById(userId);
        return quizAttemptRepository.findByUserIdOrderByCompletedAtDesc(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    private QuizAttemptResponse toResponse(QuizAttempt attempt) {
        return QuizAttemptResponse.builder()
                .id(attempt.getId())
                .userId(attempt.getUser().getId())
                .deckId(attempt.getDeck().getId())
                .score(attempt.getScore())
                .answersJson(attempt.getAnswersJson())
                .completedAt(attempt.getCompletedAt())
                .build();
    }
}
