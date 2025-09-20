package com.molardev.deckbox.application.common.interfaces;

import com.molardev.deckbox.domain.valueobject.Card;

import io.vavr.collection.Seq;
import io.vavr.control.Option;
import io.vavr.control.Validation;

public interface ICardRepository {
	Validation<Seq<String>, Card> save(Card card);
  Validation<Seq<String>, Option<Card>> findById(String id);
	Validation<Seq<String>, Void> deleteById(String id);
}
