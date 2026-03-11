package com.molardev.deckbox.infrastructure.authentication;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.molardev.deckbox.application.common.interfaces.ITokenService;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.vavr.control.Option;

@Service
public class TokenService implements ITokenService {
  private final SecretKey key = Jwts.SIG.HS256.key().build();

  public TokenService() {

  }

  @Override
  public String generateToken(String userId) {
    String jws = Jwts.builder().subject(userId).signWith(key).compact();
    return jws;
  }

  @Override
  public Option<String> getUserIdFromToken(String token) {
    try {
      var jwt = Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
      return Option.some(jwt.getPayload().getSubject());
    } catch (JwtException e) {
      return Option.none();
    }
  }
  
}
