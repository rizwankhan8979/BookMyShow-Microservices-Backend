package movieBookingapplication.AuthenticationPackege;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import movieBookingapplication.Entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    public String generateToken(User user) {

        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignKey())
                .compact();

    }


    public String extractUsername(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    // Extract Specific Claim
    public <T> T extractClaim(
            String jwtToken,
            Function<Claims, T> claimsResolver) {

        Claims claims = extractAllClaims(jwtToken);

        return claimsResolver.apply(claims);
    }

    // Extract All Claims From JWT
    private Claims extractAllClaims(String jwtToken) {

        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }

    // Create Signing Key
    private SecretKey getSignKey() {

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }


    public boolean validateToken(String jwtToken, User userDetails) {
        String username = extractUsername(jwtToken);

        return username.equals(userDetails.getUsername())
                && !isTokenExpired(jwtToken);

    }

    private boolean isTokenExpired(String jwtToken) {

        return extractExpiration(jwtToken)
                .before(new Date());
    }

    // Extract Expiration Date
    private Date extractExpiration(String jwtToken) {

        return extractClaim(
                jwtToken,
                Claims::getExpiration
        );
    }



}
