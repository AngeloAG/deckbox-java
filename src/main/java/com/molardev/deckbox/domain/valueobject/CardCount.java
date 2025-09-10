package com.molardev.deckbox.domain.valueobject;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import java.util.Objects;

public class CardCount {
    private final int value;

    private CardCount(int value) {
        this.value = value;
    }

    public static Validation<Seq<String>, CardCount> create(int value) {
        if (value < 1) {
            return Validation.invalid(io.vavr.collection.List.of("Card count must be at least 1"));
        }
        return Validation.valid(new CardCount(value));
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardCount cardCount = (CardCount) o;
        return value == cardCount.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
