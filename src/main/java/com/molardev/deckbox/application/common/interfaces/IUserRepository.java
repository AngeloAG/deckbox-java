package com.molardev.deckbox.application.common.interfaces;

import com.molardev.deckbox.domain.entity.User;
import com.molardev.deckbox.domain.errors.CustomError;

import io.vavr.control.Either;
import io.vavr.control.Option;

public interface IUserRepository {
  public Either<CustomError, User> save(User newUser, String hashPassword);
  public Either<CustomError, Option<User>> findByEmail(String email);
}
