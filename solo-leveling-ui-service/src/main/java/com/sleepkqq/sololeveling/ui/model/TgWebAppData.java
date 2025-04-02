package com.sleepkqq.sololeveling.ui.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TgWebAppData(
    LocalDateTime authDate,
    String chatType,
    String chatInstance,
    String hash,
    String signature,
    TgUserData user
) {

}
