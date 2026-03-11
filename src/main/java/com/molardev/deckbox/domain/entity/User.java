package com.molardev.deckbox.domain.entity;

import java.util.UUID;

import com.molardev.deckbox.domain.enums.Role;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public class User {
  private final UUID id;
  private final String email;
  private final List<Role> roles;

  private User(UUID id, String email, List<Role> roles) {
    this.id = id;
    this.email = email;
    this.roles = roles;
  }

  public static Validation<Seq<String>, User> create(UUID id, String email, List<String> roles) {
    List<String> errors = io.vavr.collection.List.of();
    if(id == null) {
      errors = errors.append("The id of the user cannot be null");
    }
    if(email == null) {
      errors = errors.append("The email of the user is missing");
    }
    Validation<Seq<String>, List<Role>> rolesValidation = Role.fromStrings(roles);
    if(rolesValidation.isInvalid()) {
      errors = errors.appendAll(rolesValidation.getError());
    }
    return errors.isEmpty() ? Validation.valid(new User(id, email, rolesValidation.get())) : Validation.invalid(errors);
  }

  public static Validation<Seq<String>, User> create(String email, List<String> roles) {
    UUID newId = UUID.randomUUID();
    return create(newId, email, roles);
  }

  public UUID getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public List<Role> getRoles() {
    return roles;
  }
}
