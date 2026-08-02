package com.learn.backend.controller;

import com.learn.backend.dto.request.QuizAttemptRequest;
import com.learn.backend.dto.response.QuizAttemptResponse;
import com.learn.backend.service.QuizAttemptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/quiz-attempts")
@RequiredArgsConstructor
public class QuizAttemptController {

    private final QuizAttemptService quizAttemptService;

    @PostMapping
    public ResponseEntity<QuizAttemptResponse> submit(@Valid @RequestBody QuizAttemptRequest request) {
        QuizAttemptResponse response = quizAttemptService.submit(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<QuizAttemptResponse>> getByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(quizAttemptService.getByUser(userId));
    }
}
