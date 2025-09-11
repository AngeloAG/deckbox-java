package com.molardev.deckbox.domain.valueobject;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public class DeckName {
	private final String name;

	private DeckName(String name) {
		this.name = name;
	}	

	public static Validation<Seq<String>, DeckName> create(String name) {
		return Validation.combine(
			validateNotEmpty(name),
			validateLength(name)
		).ap((v1, v2) -> new DeckName(name));
	}

	private static Validation<String, String> validateNotEmpty(String value) {
		return (value == null || value.trim().isEmpty())
			? Validation.invalid("Deck name must not be empty")
			: Validation.valid(value);
	}

	private static Validation<String, String> validateLength(String value) {
		return (value != null && value.length() > 50)
			? Validation.invalid("Deck name must be 50 characters or less")
			: Validation.valid(value);
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DeckName deckName = (DeckName) o;
		return name.equals(deckName.name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toString() {
		return name;
	}
}
