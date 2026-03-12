package com.molardev.deckbox.infrastructure.authentication;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.molardev.deckbox.application.common.interfaces.ITokenService;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.vavr.control.Option;

@Service
public class TokenService implements ITokenService {
  @Value("${app.jwt.secret}")
  private String secret;

  public TokenService() {

  }

  private SecretKey getSigningKey() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
  }

  @Override
  public String generateToken(String userId) {
    String jws = Jwts.builder()
        .subject(userId)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + 60000 * 60))
        .signWith(getSigningKey())
        .compact();
    return jws;
  }

  @Override
  public Option<String> getUserIdFromToken(String token) {
    try {
      var jwt = Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
      return Option.some(jwt.getPayload().getSubject());
    } catch (JwtException e) {
      System.out.println("Exception parsin token: " + e.getMessage());
      return Option.none();
    }
  }

}
