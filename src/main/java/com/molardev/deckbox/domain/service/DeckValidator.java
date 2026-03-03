package com.molardev.deckbox.domain.service;

import com.molardev.deckbox.domain.entity.Deck;
import com.molardev.deckbox.domain.entity.Format;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public class DeckValidator {

	public DeckValidator() {}

	public static Validation<Seq<String>, Deck> validate(Deck deck, Format format) {
		List<Seq<String>> errorSeqs = format.getRules().map(rule -> rule.validate(deck))
			.filter(Validation::isInvalid)
			.map(Validation::getError);

		Seq<String> allErrors = errorSeqs.flatMap(seq -> seq);
		return allErrors.isEmpty() ? Validation.valid(deck) : Validation.invalid(allErrors);
	}
}
