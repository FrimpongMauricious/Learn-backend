package com.learn.backend.controller;

import com.learn.backend.dto.request.BulkQuestionRequest;
import com.learn.backend.dto.response.QuestionResponse;
import com.learn.backend.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/decks/{deckId}/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/bulk")
    public ResponseEntity<List<QuestionResponse>> saveBulk(@PathVariable UUID deckId,
                                                             @Valid @RequestBody BulkQuestionRequest request) {
        List<QuestionResponse> response = questionService.saveBulk(deckId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<QuestionResponse>> getByDeck(@PathVariable UUID deckId) {
        return ResponseEntity.ok(questionService.getByDeck(deckId));
    }
}
