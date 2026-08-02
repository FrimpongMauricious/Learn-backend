package com.learn.backend.controller;

import com.learn.backend.dto.request.DeckCreateRequest;
import com.learn.backend.dto.response.DeckResponse;
import com.learn.backend.service.DeckService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/decks")
@RequiredArgsConstructor
public class DeckController {

    private final DeckService deckService;

    @PostMapping
    public ResponseEntity<DeckResponse> create(@Valid @RequestBody DeckCreateRequest request) {
        DeckResponse response = deckService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeckResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(deckService.getById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DeckResponse>> getByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(deckService.getByOwner(userId));
    }
}
