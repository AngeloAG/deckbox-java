package com.molardev.deckbox.domain.valueobject;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import java.util.Objects;

public class CardEntry {
    private final Card card;
    private final CardCount count;

    private CardEntry(Card card, CardCount count) {
        this.card = card;
        this.count = count;
    }

    public static Validation<Seq<String>, CardEntry> create(Card card, CardCount count) {
        if (card == null) {
            return Validation.invalid(io.vavr.collection.List.of("Card must not be null"));
        }
        if (count == null) {
            return Validation.invalid(io.vavr.collection.List.of("Card count must not be null"));
        }
        return Validation.valid(new CardEntry(card, count));
    }

    public Card getCard() {
        return card;
    }

    public CardCount getCount() {
        return count;
    }

    public CardEntry withCount(CardCount newCount) {
        return new CardEntry(this.card, newCount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardEntry cardEntry = (CardEntry) o;
        return Objects.equals(card, cardEntry.card) && Objects.equals(count, cardEntry.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(card, count);
    }

    @Override
    public String toString() {
        return "CardEntry{" +
                "card=" + card +
                ", count=" + count +
                '}';
    }
}
