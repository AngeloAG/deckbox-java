package com.molardev.deckbox.domain.valueobject;

import io.vavr.collection.Seq;
import io.vavr.collection.List;
import io.vavr.control.Validation;
import java.util.Objects;

import com.molardev.deckbox.domain.enums.CardType;
import com.molardev.deckbox.domain.enums.ElementalType;
import com.molardev.deckbox.domain.enums.Legality;

public class Card {
    private final CardReference cardReference;
    private final CardClassification cardClassification;
		private final List<Legality> legalities;
    private final List<ElementalType> elementalTypes;
		private final CardImageUrl imageUrl;

    private Card(CardReference cardReference, CardClassification cardClassification, List<Legality> legalities, List<ElementalType> elementalTypes, CardImageUrl imageUrl) {
        this.cardReference = cardReference;
        this.cardClassification = cardClassification;
        this.legalities = legalities;
        this.elementalTypes = elementalTypes;
        this.imageUrl = imageUrl;
    }

    public static Validation<Seq<String>, Card> create(CardReference cardReference, CardClassification cardClassification, List<Legality> legalities, List<ElementalType> elementalTypes, CardImageUrl imageUrl) {
        return validateElementalTypes(cardClassification.getSuperType(), elementalTypes)
                .map(validTypes -> new Card(cardReference, cardClassification, legalities, validTypes, imageUrl));
    }

    private static Validation<Seq<String>, List<ElementalType>> validateElementalTypes(CardType cardType, List<ElementalType> elementalTypes) {
        io.vavr.collection.List<String> errors = io.vavr.collection.List.empty();
        if (cardType == CardType.TRAINER && (elementalTypes != null && !elementalTypes.isEmpty())) {
            errors = errors.append("Trainer cards must not have elemental types");
        }
        if ((cardType == CardType.POKEMON || cardType == CardType.ENERGY) && (elementalTypes == null || elementalTypes.isEmpty())) {
            errors = errors.append("Pokemon and Energy cards must have at least one elemental type");
        }
        return errors.isEmpty() ? Validation.valid(elementalTypes) : Validation.invalid(errors);
    }

    public CardReference getCardReference() {
        return cardReference;
    }

    public CardClassification getCardClassification() {
        return cardClassification;
    }

    public List<ElementalType> getElementalTypes() {
        return elementalTypes;
    }

		public List<Legality> getLegalities() {
			return legalities;
		}

		public CardImageUrl getImageUrl() {
			return imageUrl;
		}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(cardReference, card.cardReference) &&
                Objects.equals(cardClassification, card.cardClassification) &&
                Objects.equals(elementalTypes, card.elementalTypes) &&
                Objects.equals(imageUrl, card.imageUrl) &&
                Objects.equals(legalities, card.legalities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardReference, cardClassification, elementalTypes, imageUrl, legalities);
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardReference=" + cardReference +
                ", cardClassification=" + cardClassification +
                ", elementalTypes=" + elementalTypes +
                ", imageUrl=" + imageUrl +
                ", legalities=" + legalities +
                '}';
    }
}
