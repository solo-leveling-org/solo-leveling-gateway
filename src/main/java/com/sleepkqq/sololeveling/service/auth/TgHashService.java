package com.sleepkqq.sololeveling.service.auth;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.sleepkqq.sololeveling.view.auth.TgAuthData;
import java.net.URLDecoder;
import java.security.GeneralSecurityException;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TgHashService {

  private static final String TG_SECRET_KEY = "WebAppData";

  private static final String HMAC_SHA256 = "HmacSHA256";

  private static final String HASH_FIELD = "hash";

  @Value("${telegram.bot.token}")
  private String tgBotToken;

  public boolean checkHash(TgAuthData tgAuthData) {
    var parsedQueryString = parseQueryString(tgAuthData.initData());
    try {
      return validateTelegramAuth(parsedQueryString, parsedQueryString.get(HASH_FIELD));
    } catch (GeneralSecurityException e) {
      log.error("Hash check algorithm error", e);
      return false;
    }
  }

  private boolean validateTelegramAuth(
      Map<String, String> paramMap,
      String receivedHash
  ) throws GeneralSecurityException {
    var dataString = EntryStream.of(paramMap)
        .filterKeys(k -> !HASH_FIELD.equals(k))
        .sorted(Map.Entry.comparingByKey())
        .mapKeyValue((k, v) -> k + "=" + v)
        .joining("\n");

    var sha256HMAC = Mac.getInstance(HMAC_SHA256);
    var secretKeySpec = new SecretKeySpec(getSecretHashByInitData(), HMAC_SHA256);
    sha256HMAC.init(secretKeySpec);

    var hash2 = sha256HMAC.doFinal(dataString.getBytes());

    var calculatedHash = Hex.toHexString(hash2);

    return calculatedHash.equals(receivedHash);
  }

  private byte[] getSecretHashByInitData() throws GeneralSecurityException {
    var sha256HMAC = Mac.getInstance(HMAC_SHA256);
    var secretKeySpec = new SecretKeySpec(TG_SECRET_KEY.getBytes(), HMAC_SHA256);
    sha256HMAC.init(secretKeySpec);

    return sha256HMAC.doFinal(tgBotToken.getBytes());
  }

  private Map<String, String> parseQueryString(String queryString) {
    return StreamEx.of(queryString.split("&"))
        .map(pair -> pair.split("=", 2))
        .mapToEntry(
            keyValue -> URLDecoder.decode(keyValue[0], UTF_8),
            keyValue -> URLDecoder.decode(
                keyValue.length > 1 ? keyValue[1] : "", UTF_8
            )
        )
        .toMap();
  }
}
