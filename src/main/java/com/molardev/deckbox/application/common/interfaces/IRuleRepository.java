package com.molardev.deckbox.application.common.interfaces;

import java.util.UUID;

import com.molardev.deckbox.domain.errors.CustomError;
import com.molardev.deckbox.domain.service.IDeckValidationRule;

import io.vavr.control.Either;
import io.vavr.control.Option;

public interface IRuleRepository {
  Either<CustomError, IDeckValidationRule> save(IDeckValidationRule rule);
  Either<CustomError, Option<IDeckValidationRule>> findById(UUID id);
	Either<CustomError, Void> deleteById(UUID id);
}
