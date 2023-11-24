package net.stepbooks.infrastructure.security.admin;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Component
public class AdminJwtTokenProvider {

    @Value("${admin.jwt.token-type}")
    private String tokenType;

    @Value("${admin.jwt.issuer}")
    private String issuer;

    @Value("${admin.jwt.access-token.secret}")
    private String accessTokenSecret;

    @Value("${admin.jwt.access-token.expiration}")
    private Long accessTokenExpiration;

    @Value("${admin.jwt.refresh-token.secret}")
    private String refreshTokenSecret;

    @Value("${admin.jwt.refresh-token.expiration}")
    private Long refreshTokenExpiration;


    public String getSubjectFromToken(String token) {
        if (ObjectUtils.isEmpty(token)) {
            throw new BusinessException(ErrorCode.AUTH_ERROR, "Cannot found the authorization token");
        }
        var claims = parseToken(token);
        return claims.getSubject();
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

    public TokenDto generateToken(JwtUserDetails jwtUserDetails) {
        var accessToken = generateAccessToken(jwtUserDetails);
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

    public Boolean verifyRefreshToken(String token, UserDetails userDetails) {
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

    public TokenDto refreshToken(JwtUserDetails userDetails, String refreshToken, LocalDateTime lastPasswordReset) {
        Boolean verified = verifyRefreshToken(refreshToken, userDetails);
        if (!verified) {
            throw new BusinessException(ErrorCode.AUTH_ERROR);
        }
        Boolean canTokenBeRefreshed = canTokenBeRefreshed(refreshToken, lastPasswordReset);
        if (!canTokenBeRefreshed) {
            throw new BusinessException(ErrorCode.AUTH_ERROR);
        }
        String accessToken = generateAccessToken(userDetails);
        refreshToken = generateRefreshToken();
        return TokenDto.builder()
                .tokenType(tokenType)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(accessTokenExpiration)
                .refreshExpiresIn(refreshTokenExpiration)
                .build();
    }

    private String generateAccessToken(JwtUserDetails jwtUserDetails) {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime expire = LocalDateTime.now(ZoneOffset.UTC).plusSeconds(accessTokenExpiration);

        final Date createdDate = Date.from(now.toInstant(ZoneOffset.UTC));
        final Date expirationDate = Date.from(expire.toInstant(ZoneOffset.UTC));

        return JWT.create().withIssuer(issuer)
                .withIssuedAt(createdDate)
                .withSubject(jwtUserDetails.getUsername())
                .withExpiresAt(expirationDate)
                .withClaim("email", jwtUserDetails.getEmail())
                .withClaim("role", Collections.singletonList(jwtUserDetails.getAuthorities()))
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


