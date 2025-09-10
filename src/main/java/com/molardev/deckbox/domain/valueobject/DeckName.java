package com.molardev.deckbox.domain.valueobject;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import java.util.Objects;

public class DeckName {
    private final String value;

    private DeckName(String value) {
        this.value = value;
    }

    public static Validation<Seq<String>, DeckName> create(String value) {
        return Validation.combine(
                validateNotEmpty(value),
                validateLength(value)
        ).ap((v1, v2) -> new DeckName(value));
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

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeckName deckName = (DeckName) o;
        return value.equals(deckName.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
