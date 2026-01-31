package com.molardev.deckbox.application.common.interfaces;

import java.util.UUID;

import com.molardev.deckbox.domain.entity.Deck;
import com.molardev.deckbox.domain.errors.CustomError;
import com.molardev.deckbox.domain.valueobject.DeckReference;

import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Option;

public interface IDeckRepository {
    Either<CustomError, Deck> save(Deck deck);
    Either<CustomError, Option<Deck>> findById(UUID id);
		Either<CustomError, Seq<DeckReference>> findAllDeckReferences();
		Either<CustomError, Void> deleteById(UUID id);
		Either<CustomError, Option<Deck>> findByIdWithCardEntries(UUID id);
}
