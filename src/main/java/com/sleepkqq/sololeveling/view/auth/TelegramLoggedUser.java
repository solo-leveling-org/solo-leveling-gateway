package com.sleepkqq.sololeveling.view.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TelegramLoggedUser(
    long id,

    String username,

    @JsonProperty("first_name")
    String firstName,

    @JsonProperty("last_name")
    String lastName,

    @JsonProperty("photo_url")
    String photoUrl,

    @JsonProperty("auth_date")
    long authDate,

    String hash
) {

}
