package com.molardev.deckbox.domain.enums;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public enum CardType {
    POKEMON,
    TRAINER,
    ENERGY;

		public static Validation<Seq<String>, CardType> fromString(String value) {
			try {
				return Validation.valid(CardType.valueOf(value.toUpperCase()));
			} catch (IllegalArgumentException e) {
				return Validation.invalid(io.vavr.collection.List.of("Invalid card type: " + value));
			}
		}
}
