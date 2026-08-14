package airlineReservation.global.security;

import airlineReservation.global.config.JwtProperties;
import airlineReservation.global.constant.Const;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.secretKey = Keys.hmacShaKeyFor(
                jwtProperties.secret().getBytes(StandardCharsets.UTF_8)
        );
    }

    /**
     * ログイン成功時に JWT を発行する。
     * subject = email（ログインIDと同じ。Spring Security の username として使用）
     */
    public String createToken(String email, Integer userId, Integer roleCode, String userName) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtProperties.expiration());

        return Jwts.builder()
                .subject(email)
                .claim(Const.JWT.CLAIM_USER_ID, userId)
                .claim(Const.JWT.CLAIM_ROLE, roleCode)
                .claim(Const.JWT.CLAIM_USER_NAME, userName)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey)
                .compact();
    }

    public String getEmailFromToken(String token) {
        return getClaims(token).getSubject();
    }

    public Integer getUserIdFromToken(String token) {
        return getClaims(token).get(Const.JWT.CLAIM_USER_ID, Integer.class);
    }

    public Integer getRoleCodeFromToken(String token) {
        return getClaims(token).get(Const.JWT.CLAIM_ROLE, Integer.class);
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
