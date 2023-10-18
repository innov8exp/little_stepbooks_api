package net.stepbooks.infrastructure.security.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.infrastructure.enums.AuthType;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.infrastructure.model.JwtUserDetails;
import net.stepbooks.interfaces.client.dto.TokenDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class UserJwtTokenProvider {

    @Value("${jwt.token-type}")
    private String tokenType;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.access-token.secret}")
    private String accessTokenSecret;

    @Value("${jwt.access-token.expiration}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh-token.secret}")
    private String refreshTokenSecret;

    @Value("${jwt.refresh-token.expiration}")
    private Long refreshTokenExpiration;


    public String getSubjectFromToken(String token) {
        if (ObjectUtils.isEmpty(token)) {
            throw new BusinessException(ErrorCode.AUTH_ERROR, "Cannot found the authorization token");
        }
        var claims = parseToken(token);
        return claims.getSubject();
    }

    public Map<String, Claim> getClaimsFromToken(String token) {
        var claims = parseToken(token);
        return claims.getClaims();
    }


    public LocalDateTime getCreatedDateFromToken(String token) {
        var claims = parseToken(token);
        ZoneId utc = ZoneId.of(ZoneOffset.UTC.getId());
        return claims.getIssuedAt().toInstant().atZone(utc).toLocalDateTime();
    }

    public Date getExpirationDateFromToken(String token) {
        var claims = parseToken(token);
        return claims.getExpiresAt();
    }

    public TokenDto generateToken(JwtUserDetails jwtUserDetails, AuthType authType) {
        var accessToken = generateAccessToken(jwtUserDetails, authType);
        var refreshToken = generateRefreshToken();
        return TokenDto.builder()
                .tokenType(tokenType)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(accessTokenExpiration)
                .refreshExpiresIn(refreshTokenExpiration)
                .build();
    }

    public Boolean verifyToken(String token, UserDetails userDetails) {
        if (token.startsWith(tokenType)) {
            token = token.substring(tokenType.length()).trim();
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(accessTokenSecret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .withSubject(userDetails.getUsername())
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            return jwt != null;
        } catch (JWTVerificationException exception) {
            //Invalid signature/claims
            log.info("verification token info: {}", exception.getMessage());
            return false;
        }
    }

    public TokenDto refreshToken(JwtUserDetails userDetails, String refreshToken, AuthType authType, LocalDateTime lastPasswordReset) {
        Boolean canTokenBeRefreshed = canTokenBeRefreshed(refreshToken, lastPasswordReset);
        if (!canTokenBeRefreshed) {
            throw new BusinessException(ErrorCode.AUTH_ERROR);
        }
        String accessToken = generateAccessToken(userDetails, authType);
        refreshToken = generateRefreshToken();
        return TokenDto.builder()
                .tokenType(tokenType)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(accessTokenExpiration)
                .refreshExpiresIn(refreshTokenExpiration)
                .build();
    }

    private String generateAccessToken(JwtUserDetails userDetails, AuthType authType) {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime expire = LocalDateTime.now(ZoneOffset.UTC).plusSeconds(accessTokenExpiration);

        final Date createdDate = Date.from(now.toInstant(ZoneOffset.UTC));
        final Date expirationDate = Date.from(expire.toInstant(ZoneOffset.UTC));

        String socialId = null;
        if (authType.equals(AuthType.FACEBOOK)) {
            socialId = userDetails.getFacebookId();
        } else if (authType.equals(AuthType.GOOGLE)) {
            socialId = userDetails.getGoogleId();
        }

        return JWT.create().withIssuer(issuer)
                .withIssuedAt(createdDate)
                .withSubject(userDetails.getUsername())
                .withExpiresAt(expirationDate)
                .withClaim("email", userDetails.getEmail())
                .withClaim("socialId", socialId)
                .withClaim("authType", authType.getValue())
                .withClaim("role", Collections.singletonList(userDetails.getAuthorities()))
                .sign(Algorithm.HMAC256(accessTokenSecret));

    }

    private String generateRefreshToken() {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime expire = LocalDateTime.now(ZoneOffset.UTC).plusSeconds(refreshTokenExpiration);

        final Date createdDate = Date.from(now.toInstant(ZoneOffset.UTC));
        final Date expirationDate = Date.from(expire.toInstant(ZoneOffset.UTC));

        Algorithm algorithmHS = Algorithm.HMAC256(refreshTokenSecret);
        return JWT.create().withIssuer(issuer)
                .withIssuedAt(createdDate)
                .withExpiresAt(expirationDate)
                .sign(algorithmHS);
    }

    private DecodedJWT parseToken(String token) {
        if (token.startsWith(tokenType)) {
            token = token.substring(tokenType.length()).trim();
        }
        return JWT.decode(token);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        Date now = new Date();
        return expiration.before(now);
    }

    private Boolean isCreatedBeforeLastPasswordReset(LocalDateTime created, LocalDateTime lastPasswordReset) {
        return (lastPasswordReset != null && created.isBefore(lastPasswordReset));
    }

    private Boolean canTokenBeRefreshed(String refreshToken, LocalDateTime lastPasswordReset) {
        final LocalDateTime created = getCreatedDateFromToken(refreshToken);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && (!isTokenExpired(refreshToken));
    }


}


