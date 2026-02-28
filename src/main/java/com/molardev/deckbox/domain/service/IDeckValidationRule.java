package com.molardev.deckbox.domain.service;

import java.util.UUID;

import com.molardev.deckbox.domain.entity.Deck;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public interface IDeckValidationRule {
  UUID getId();
	Validation<Seq<String>, Deck> validate(Deck deck);
}
