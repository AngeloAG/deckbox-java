package com.molardev.deckbox.domain.valueobject;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import java.util.Objects;

/**
 * Represents a reference to a Pokémon TCG card by its ID (from an external API or database).
 */
public class CardReference {
    private final String cardId;
		private final String name;

    private CardReference(String cardId, String name) {
        this.cardId = cardId;
        this.name = name;
    }

    public static Validation<Seq<String>, CardReference> create(String cardId, String name) {
        return Validation.combine(
                validateNotEmpty(cardId),
                validateNotEmpty(name),
                validateLength(cardId),
                validateLength(name)
        ).ap((v1, v2, v3, v4) -> new CardReference(cardId, name));
    }

    private static Validation<String, String> validateNotEmpty(String cardId) {
        return (cardId == null || cardId.trim().isEmpty())
                ? Validation.invalid("Card ID must not be empty")
                : Validation.valid(cardId);
    }

    private static Validation<String, String> validateLength(String cardId) {
        return (cardId != null && cardId.length() > 40)
                ? Validation.invalid("Card ID must be 40 characters or less")
                : Validation.valid(cardId);
    }

    public String getCardId() {
        return cardId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardReference that = (CardReference) o;
        return cardId.equals(that.cardId) && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardId, name);
    }

    @Override
    public String toString() {
        return "CardReference{" +
                "cardId='" + cardId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
