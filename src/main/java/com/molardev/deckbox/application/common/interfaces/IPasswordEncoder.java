package com.molardev.deckbox.application.common.interfaces;

public interface IPasswordEncoder {
  public String hash(String password);
  public boolean matches(String plainPassword, String storedPassword);
}
