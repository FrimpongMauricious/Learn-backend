package com.learn.backend.dto.response;

import com.learn.backend.enums.Level;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeckResponse {

    private UUID id;
    private String title;
    private String subject;
    private Level level;
    private String sourceFileRef;
    private UUID ownerId;
    private Instant createdAt;
}
