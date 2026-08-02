package com.learn.backend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String passwordHash;

    /**
     * Lombok generates isPremium()/setPremium(boolean) for this field (it treats the
     * "is" prefix as already satisfying the boolean-getter convention, so it strips it
     * from the setter and Jackson strips it again from the getter), which serializes as
     * "premium" instead of "isPremium". onMethod_ pins @JsonProperty directly on those
     * generated accessors so every "is"-prefixed boolean keeps its explicit JSON name.
     */
    @Getter(onMethod_ = @__(@JsonProperty("isPremium")))
    @Setter(onMethod_ = @__(@JsonProperty("isPremium")))
    @Column(nullable = false)
    @Builder.Default
    private boolean isPremium = false;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }
}
