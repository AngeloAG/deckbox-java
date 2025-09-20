package com.molardev.deckbox.domain.enums;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public enum CardSubtype {
		// Pokemon Subtypes
		NO_RULE_BOX,
		BASIC,
		STAGE_1,
		STAGE_2,
		EX,
		LEGEND,
		MEGA,
		GX,
		V,
		VMAX,
		VSTAR,
		RADIANT,

		// Trainer Subtypes
		SUPPORTER,
		ITEM,
		STADIUM,
		POKEMON_TOOL,

		// Energy Subtypes
		SPECIAL_ENERGY,
		PRISMATIC_ENERGY,
		BASIC_ENERGY;

		public static Validation<Seq<String>, CardSubtype> fromString(String value) {
			try {
				return Validation.valid(CardSubtype.valueOf(value.toUpperCase()));
			} catch (IllegalArgumentException e) {
				return Validation.invalid(io.vavr.collection.List.of("Invalid CardSubtype: " + value));
			}
		}
}
