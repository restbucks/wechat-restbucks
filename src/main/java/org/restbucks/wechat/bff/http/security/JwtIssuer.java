package org.restbucks.wechat.bff.http.security;

import static io.jsonwebtoken.SignatureAlgorithm.HS512;
import static java.lang.String.format;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.restbucks.wechat.bff.time.Clock;
import org.restbucks.wechat.bff.wechat.oauth.OpenId;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("security.jwt")
@RequiredArgsConstructor
@Data
@ToString(exclude = "signingKey")//to exclude credentials from logs
public class JwtIssuer {

    @NonNull
    private final Clock clock;

    private String signingKey = "shouldBeARandomString";

    private int expiresInSeconds = 3600;


    public String buildUserJwt(OpenId openId, String csrfToken) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("csrfToken", csrfToken);
        return buildUserJwt(openId, claims);
    }

    public String buildUserJwt(OpenId openId, Map<String, Object> claims) {
        try {
            JwtBuilder builder = Jwts.builder()
                .setSubject(openId.getValue())
                .setIssuer("wechat.restbucks.org")
                .setExpiration(
                    Date.from(clock.now().plusSeconds(getExpiresInSeconds()).toInstant()))
                .signWith(HS512, getSigningKey());
            claims.forEach(builder::claim);
            return builder.compact();
        } catch (Exception exception) {
            throw new CannotIssueJwtException(format("Cannot issue jwt with %s and %s due to %s",
                openId, claims, exception.getMessage()), exception);
        }
    }

    public OpenId verified(String userJwt, String csrfToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(userJwt);
            String expectCsrfToken = (String) claims.getBody().get("csrfToken");
            if (expectCsrfToken.equals(csrfToken)) {
                return OpenId.valueOf(claims.getBody().getSubject());
            } else {
                throw new CannotVerifyJwtException(
                    format("Cannot verify csrf token, expect [%s], got [%s]", expectCsrfToken,
                        csrfToken));// should I return expect and actual?
            }
            //OK, we can trust this JWT
        } catch (Exception exception) {
            throw new CannotVerifyJwtException(
                format("Cannot verify jwt token due to %s", exception.getMessage()), exception);
        }
    }
}
