package com.molardev.deckbox.application.common.interfaces;

import com.molardev.deckbox.domain.errors.CustomError;
import com.molardev.deckbox.domain.valueobject.Card;

import io.vavr.control.Either;
import io.vavr.control.Option;

public interface ICardRepository {
	Either<CustomError, Card> save(Card card);
  Either<CustomError, Option<Card>> findById(String id);
	Either<CustomError, Void> deleteById(String id);
}
