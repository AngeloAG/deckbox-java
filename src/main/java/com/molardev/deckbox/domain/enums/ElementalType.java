package com.molardev.deckbox.domain.enums;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public enum ElementalType {
    COLORLESS,
    DARKNESS,
    DRAGON,
    FAIRY,
    FIGHTING,
    FIRE,
    GRASS,
    LIGHTNING,
    METAL,
    PSYCHIC,
    WATER;

		public static Validation<Seq<String>, ElementalType> fromString(String value) {
			try {
				return Validation.valid(ElementalType.valueOf(value.toUpperCase()));
			} catch (IllegalArgumentException e) {
				return Validation.invalid(io.vavr.collection.List.of("Invalid elemental type: " + value));
			}
		}
}
