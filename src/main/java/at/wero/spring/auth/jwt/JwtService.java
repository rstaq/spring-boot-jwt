package at.wero.spring.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

@Service
public class JwtService {
    private final JwtConfiguration jwtConfiguration;
    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    @Autowired
    public JwtService(JwtConfiguration jwtConfiguration) {
        Assert.notNull(jwtConfiguration, "Argument jwtConfiguration must not be null");
        this.jwtConfiguration = jwtConfiguration;
        algorithm = Algorithm.HMAC512(jwtConfiguration.getSecret());
        verifier = JWT.require(algorithm)
                .withIssuer(jwtConfiguration.getIssuer())
                .build();
    }

    public String generateToken(String username) {
        LocalDateTime expirationDate = LocalDateTime.now().plusSeconds(jwtConfiguration.getExpirationTime() * 60);
        return JWT.create()
                .withSubject(username)
                .withIssuer(jwtConfiguration.getIssuer())
                .withClaim(JwtConfiguration.EXPIRES_AT, expirationDate.toString())
                .sign(algorithm);
    }

    public String verifyToken(String token) throws JWTVerificationException {
        DecodedJWT decodedJWT = verifier.verify(token);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationTime = LocalDateTime.parse(decodedJWT.getClaim(JwtConfiguration.EXPIRES_AT).asString());
        if (now.isAfter(expirationTime)) {
            throw new JWTVerificationException("Token expired");
        }
        return decodedJWT.getSubject();
    }
}
