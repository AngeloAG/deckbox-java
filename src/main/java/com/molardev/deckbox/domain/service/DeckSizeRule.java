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

	public static Validation<Seq<String>, DeckSizeRule> create(Long id, int maxCount) {
		List<String> errors = io.vavr.collection.List.of();
		if(id == null) {
			errors = errors.append("Id of rule cannot be null");
		}
		if(maxCount < 0) {
			errors = errors.append("The mac count cannot be less than zero");
		}

		return errors.isEmpty() ? Validation.valid(new DeckSizeRule(id, maxCount)) : Validation.invalid(errors);
	}

	public static Validation<Seq<String>, DeckSizeRule> create(int maxCount) {
		List<String> errors = io.vavr.collection.List.of();
		if(maxCount < 0) {
			errors = errors.append("The max count cannot be less than zero");
		}
		return errors.isEmpty() ? Validation.valid(new DeckSizeRule(null, maxCount)) : Validation.invalid(errors);
	}

	public Long getId() {
		return id;
	}

	public int getMaxCount() {
		return maxCount;
	}
}
