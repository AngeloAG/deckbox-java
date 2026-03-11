package com.molardev.deckbox.application.common.interfaces;

import java.util.UUID;

import com.molardev.deckbox.application.common.models.UserSecurityDetails;
import com.molardev.deckbox.domain.errors.CustomError;

import io.vavr.control.Either;
import io.vavr.control.Option;

public interface IIdentityRepository {
  public Either<CustomError, Option<UserSecurityDetails>> getSecurityDetailsByEmail(String email);
  public Either<CustomError, Option<UUID>> getCurrentUserId();
}
