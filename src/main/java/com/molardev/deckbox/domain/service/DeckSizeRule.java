package com.molardev.deckbox.domain.service;

import com.molardev.deckbox.domain.entity.Deck;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public class DeckSizeRule implements IDeckValidationRule {
	private final Long id;
	private final int maxCount;

	public DeckSizeRule(Long id, int maxCount) {
		this.id = id;
		this.maxCount = maxCount;
	}

	@Override
	public Validation<Seq<String>, Deck> validate(Deck deck) {
		if(deck == null || deck.getCardEntries().length() > this.maxCount) {
			return Validation.invalid(List.of("Deck size exceeds maximum allowed limit of " + maxCount));
		}
		
		return Validation.valid(deck);
	}

	public Long getId() {
		return id;
	}

	public int getMaxCount() {
		return maxCount;
	}
}
