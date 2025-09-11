package com.molardev.deckbox.domain.service;

import com.molardev.deckbox.domain.entity.Deck;
import com.molardev.deckbox.domain.enums.CardType;
import com.molardev.deckbox.domain.valueobject.CardEntry;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public class MaxCopiesRule implements IDeckValidationRule {
    private final int maxCopies;
		private final CardType cardType;

    public MaxCopiesRule(int maxCopies, CardType cardType) {
        this.maxCopies = maxCopies;
				this.cardType = cardType;
    }

    @Override
    public Validation<Seq<String>, Deck> validate(Deck deck) {
        List<CardEntry> entries = deck.getCardEntries();
        List<String> errors = entries
            .filter(entry -> entry.getCard().getCardClassification().getSuperType() == this.cardType || entry.getCount().getValue() > maxCopies)
            .map(entry -> "Card " + entry.getCard() + " exceeds max allowed copies: " + entry.getCount().getValue() + " > " + maxCopies)
            .toList();

        return errors.isEmpty() ? Validation.valid(deck) : Validation.invalid(errors);
    }
}
