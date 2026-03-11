package com.molardev.deckbox.infrastructure.authentication;

import org.springframework.stereotype.Service;

import com.molardev.deckbox.application.common.interfaces.IPasswordEncoder;

@Service
public class PasswordEncoderService implements IPasswordEncoder {
  org.springframework.security.crypto.password.PasswordEncoder encoder
   = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();

  public PasswordEncoderService() {}

  @Override
  public String hash(String password) {
    return encoder.encode(password);
  }

  @Override
  public boolean matches(String plainPassword, String storedPassword) {
    return encoder.matches(plainPassword, storedPassword);
  }  
}
