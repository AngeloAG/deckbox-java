package com.molardev.deckbox.domain.enums;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public enum Legality {
	UNLIMITED,
	STANDARD,
	EXPANDED;

	public static Validation<Seq<String>, Legality> fromString(String value) {
		try {
			return Validation.valid(Legality.valueOf(value.toUpperCase()));
		} catch (IllegalArgumentException e) {
			return Validation.invalid(io.vavr.collection.List.of("Invalid legality: " + value));
		}
	}
}
