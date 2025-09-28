package com.molardev.deckbox.infrastructure.persistence.enums;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public enum RuleType {
	DECK_SIZE,
	ELEMENTAL_TYPE,
	LEGALITY,
	MAX_COPIES;

	public static Validation<Seq<String>, RuleType> fromString(String value) {
		try {
			return io.vavr.control.Validation.valid(RuleType.valueOf(value.toUpperCase()));
		} catch (IllegalArgumentException e) {
			return io.vavr.control.Validation.invalid(io.vavr.collection.List.of("Invalid RuleType: " + value));
		}
	}
}
