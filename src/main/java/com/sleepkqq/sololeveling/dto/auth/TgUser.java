package com.sleepkqq.sololeveling.dto.auth;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TgUser(
    long id,
    String username,
    String firstName,
    String lastName,
    String photoUrl,
    String languageCode,
    Boolean addedToAttachmentMenu,
    Boolean allowsWriteToPm,
    Boolean isBot,
    Boolean isPremium
) {

}
