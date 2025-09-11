package com.molardev.deckbox.domain.service;

import com.molardev.deckbox.domain.entity.Deck;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public interface IDeckValidationRule {
	Validation<Seq<String>, Deck> validate(Deck deck);
}
