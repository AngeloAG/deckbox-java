package com.molardev.deckbox.application.common.interfaces;

import io.vavr.control.Option;

public interface ITokenService {
  public String generateToken(String userId);
  public Option<String> getUserIdFromToken(String token);
}
