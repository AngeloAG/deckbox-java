package com.molardev.deckbox.application.common.interfaces;

import java.util.UUID;

import com.molardev.deckbox.domain.entity.Deck;
import com.molardev.deckbox.domain.valueobject.DeckReference;

import io.vavr.collection.Seq;
import io.vavr.control.Option;
import io.vavr.control.Validation;

public interface IDeckRepository {
    Validation<Seq<String>, Deck> save(Deck deck);
    Validation<Seq<String>, Deck> findById(UUID id);
		Validation<Seq<String>, Seq<DeckReference>> findAllDeckReferences();
		Validation<Seq<String>, Void> deleteById(UUID id);
		Validation<Seq<String>, Option<Deck>> findByIdWithCardEntries(UUID id);
}
