package com.learn.backend.service;

import com.learn.backend.dto.request.DeckCreateRequest;
import com.learn.backend.dto.response.DeckResponse;
import com.learn.backend.entity.Deck;
import com.learn.backend.entity.User;
import com.learn.backend.exception.ResourceNotFoundException;
import com.learn.backend.repository.DeckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeckService {

    private final DeckRepository deckRepository;
    private final UserService userService;

    @Transactional
    public DeckResponse create(DeckCreateRequest request) {
        User owner = userService.getEntityById(request.getOwnerId());

        Deck deck = Deck.builder()
                .owner(owner)
                .title(request.getTitle())
                .subject(request.getSubject())
                .level(request.getLevel())
                .sourceFileRef(request.getSourceFileRef())
                .build();

        return toResponse(deckRepository.save(deck));
    }

    @Transactional(readOnly = true)
    public DeckResponse getById(UUID id) {
        return toResponse(getEntityById(id));
    }

    @Transactional(readOnly = true)
    public List<DeckResponse> getByOwner(UUID ownerId) {
        return deckRepository.findByOwnerId(ownerId).stream()
                .map(this::toResponse)
                .toList();
    }

    Deck getEntityById(UUID id) {
        return deckRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deck not found: " + id));
    }

    private DeckResponse toResponse(Deck deck) {
        return DeckResponse.builder()
                .id(deck.getId())
                .title(deck.getTitle())
                .subject(deck.getSubject())
                .level(deck.getLevel())
                .sourceFileRef(deck.getSourceFileRef())
                .ownerId(deck.getOwner().getId())
                .createdAt(deck.getCreatedAt())
                .build();
    }
}
