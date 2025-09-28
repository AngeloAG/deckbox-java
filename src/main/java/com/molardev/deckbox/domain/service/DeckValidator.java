package com.molardev.deckbox.domain.service;

import com.molardev.deckbox.domain.entity.Deck;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public class DeckValidator {
	private final Long id;
	private final List<IDeckValidationRule> rules;

	public DeckValidator(Long id, List<IDeckValidationRule> rules) {
		this.id = id;
		this.rules = rules;
	}

	public Validation<Seq<String>, Deck> validate(Deck deck) {
		List<Seq<String>> errorSeqs = rules.map(rule -> rule.validate(deck))
			.filter(Validation::isInvalid)
			.map(Validation::getError);

		Seq<String> allErrors = errorSeqs.flatMap(seq -> seq);
		return allErrors.isEmpty() ? Validation.valid(deck) : Validation.invalid(allErrors);
	}

	public Long getId() {
		return id;
	}
}
