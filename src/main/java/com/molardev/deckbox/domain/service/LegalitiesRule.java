package com.molardev.deckbox.domain.service;

import com.molardev.deckbox.domain.entity.Deck;
import com.molardev.deckbox.domain.enums.Legality;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public class LegalitiesRule implements IDeckValidationRule {
	private final List<Legality> disallowedLegalities;

	public LegalitiesRule(List<Legality> disallowedLegalities) {
		this.disallowedLegalities = disallowedLegalities;
	}

	@Override
	public Validation<Seq<String>, Deck> validate(Deck deck) {
		List<String> errors = deck.getCardEntries()
			.filter(entry -> entry.getCard().getLegalities().exists(disallowedLegalities::contains))
			.map(entry -> "Card " + entry.getCard() + " has disallowed legality.")
			.toList();

		return errors.isEmpty() ? Validation.valid(deck) : Validation.invalid(errors);
	}
}
