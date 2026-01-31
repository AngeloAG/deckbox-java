package com.molardev.deckbox.domain.service;

import com.molardev.deckbox.domain.entity.Deck;
import com.molardev.deckbox.domain.enums.CardType;
import com.molardev.deckbox.domain.valueobject.CardEntry;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public class MaxCopiesRule implements IDeckValidationRule {
		private final Long id;
    private final int maxCopies;
		private final CardType cardType;

    public MaxCopiesRule(Long id, int maxCopies, CardType cardType) {
			this.id = id;
			this.maxCopies = maxCopies;
			this.cardType = cardType;
    }

    @Override
    public Validation<Seq<String>, Deck> validate(Deck deck) {
        List<CardEntry> entries = deck.getCardEntries();
        List<String> errors = entries
            .filter(entry -> entry.getCard().getCardClassification().getSuperType() == this.cardType || entry.getCount().getValue() > this.maxCopies)
            .map(entry -> "Card " + entry.getCard() + " exceeds max allowed copies: " + entry.getCount().getValue() + " > " + this.maxCopies)
            .toList();

        return errors.isEmpty() ? Validation.valid(deck) : Validation.invalid(errors);
    }

		public static Validation<Seq<String>, MaxCopiesRule> create(Long id, int maxCopies, String cardType) {
			Seq<String> errors = io.vavr.collection.List.of();
			if(id == null) {
				errors = errors.append("The id of the MaxCopiesRule cannot be null");
			}
			if(maxCopies < 0) {
				errors = errors.append("The amount of maxCopies cannot be less than zero");
			}
			var cardTypeValidation = CardType.fromString(cardType);
			if(cardTypeValidation.isInvalid()) {
				errors = errors.appendAll(cardTypeValidation.getError());
			}
			return errors.isEmpty() ? Validation.valid(new MaxCopiesRule(id, maxCopies, cardTypeValidation.get())) : Validation.invalid(errors); 
		}

		public static Validation<Seq<String>, MaxCopiesRule> create(int maxCopies, String cardType) {
			Seq<String> errors = io.vavr.collection.List.of();
			if(maxCopies < 0) {
				errors = errors.append("The amount of maxCopies cannot be less than zero");
			}
			var cardTypeValidation = CardType.fromString(cardType);
			if(cardTypeValidation.isInvalid()) {
				errors = errors.appendAll(cardTypeValidation.getError());
			}
			return errors.isEmpty() ? Validation.valid(new MaxCopiesRule(null, maxCopies, cardTypeValidation.get())) : Validation.invalid(errors); 
		}

		public Long getId() {
			return id;
		}

		public int getMaxCopies() {
			return maxCopies;
		}

		public CardType getCardType() {
			return cardType;
		}
}
