package com.learn.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class UserResponse {

    private UUID id;
    private String email;
    private String username;

    /**
     * Lombok generates isPremium()/setPremium(boolean) for this field (it treats the
     * "is" prefix as already satisfying the boolean-getter convention, so it strips it
     * from the setter and Jackson strips it again from the getter), which serializes as
     * "premium" instead of "isPremium". onMethod_ pins @JsonProperty directly on those
     * generated accessors so every "is"-prefixed boolean keeps its explicit JSON name.
     */
    @Getter(onMethod_ = @__(@JsonProperty("isPremium")))
    @Setter(onMethod_ = @__(@JsonProperty("isPremium")))
    private boolean isPremium;

    private Instant createdAt;
}
