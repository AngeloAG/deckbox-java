package com.molardev.deckbox.domain.enums;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public enum Role {
  ADMIN,
  TRAINER;

  public static Validation<Seq<String>, Role> fromString(String value) {
    try {
        return Validation.valid(Role.valueOf(value.toUpperCase()));
    } catch (IllegalArgumentException e) {
        return Validation.invalid(io.vavr.collection.List.of("Invalid role: " + value));
    }
}

public static Validation<Seq<String>, List<Role>> fromStrings(List<String> values) {
    return Validation.traverse(values, Role::fromString).map(Seq::toList);
}
}
