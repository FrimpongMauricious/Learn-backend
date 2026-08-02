package com.learn.backend.service;

import com.learn.backend.dto.request.BulkQuestionRequest;
import com.learn.backend.dto.request.QuestionRequest;
import com.learn.backend.dto.response.QuestionResponse;
import com.learn.backend.entity.Deck;
import com.learn.backend.entity.Question;
import com.learn.backend.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final DeckService deckService;

    @Transactional
    public List<QuestionResponse> saveBulk(UUID deckId, BulkQuestionRequest request) {
        Deck deck = deckService.getEntityById(deckId);

        List<Question> questions = request.getQuestions().stream()
                .map(q -> toEntity(q, deck))
                .toList();

        return questionRepository.saveAll(questions).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<QuestionResponse> getByDeck(UUID deckId) {
        deckService.getEntityById(deckId);
        return questionRepository.findByDeckId(deckId).stream()
                .map(this::toResponse)
                .toList();
    }

    private Question toEntity(QuestionRequest request, Deck deck) {
        return Question.builder()
                .deck(deck)
                .questionText(request.getQuestionText())
                .options(request.getOptions())
                .correctAnswer(request.getCorrectAnswer())
                .difficulty(request.getDifficulty())
                .topicTag(request.getTopicTag())
                .build();
    }

    private QuestionResponse toResponse(Question question) {
        return QuestionResponse.builder()
                .id(question.getId())
                .deckId(question.getDeck().getId())
                .questionText(question.getQuestionText())
                .options(question.getOptions())
                .correctAnswer(question.getCorrectAnswer())
                .difficulty(question.getDifficulty())
                .topicTag(question.getTopicTag())
                .build();
    }
}
