package com.molardev.deckbox.domain.valueobject;

import com.molardev.deckbox.domain.enums.CardSubtype;
import com.molardev.deckbox.domain.enums.CardType;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;

import java.util.Objects;

public class CardClassification {
    private static final java.util.Set<CardSubtype> POKEMON_SUBTYPES = java.util.EnumSet.of(
        CardSubtype.NO_RULE_BOX, CardSubtype.LEGEND, CardSubtype.MEGA, CardSubtype.GX,
        CardSubtype.V, CardSubtype.VMAX, CardSubtype.VSTAR, CardSubtype.RADIANT
    );
    private static final java.util.Set<CardSubtype> TRAINER_SUBTYPES = java.util.EnumSet.of(
        CardSubtype.SUPPORTER, CardSubtype.ITEM, CardSubtype.STADIUM, CardSubtype.POKEMON_TOOL
    );
    private static final java.util.Set<CardSubtype> ENERGY_SUBTYPES = java.util.EnumSet.of(
        CardSubtype.SPECIAL_ENERGY, CardSubtype.PRISMATIC_ENERGY
    );
    private final CardType superType;
    private final List<CardSubtype> subTypes;


    private CardClassification(CardType superType, List<CardSubtype> subTypes) {
        this.superType = superType;
        this.subTypes = subTypes;
    }

		public static Validation<Seq<String>, CardClassification> create(CardType superType, List<CardSubtype> subTypes) {
			return isValidSubtypeForSuperType(superType, subTypes) ?
				Validation.valid(new CardClassification(superType, subTypes)) :
				Validation.invalid(io.vavr.collection.List.of("Invalid subtype " + subTypes + " for super type " + superType));
		}

    public static boolean isValidSubtypeForSuperType(CardType superType, List<CardSubtype> subTypes) {
        if (superType == null || subTypes == null || subTypes.isEmpty()) {
            return false;
        }
        java.util.Set<CardSubtype> validSet;
        switch (superType) {
            case POKEMON: validSet = POKEMON_SUBTYPES; break;
            case TRAINER: validSet = TRAINER_SUBTYPES; break;
            case ENERGY:  validSet = ENERGY_SUBTYPES;  break;
            default:      return false;
        }
        return subTypes.exists(validSet::contains);
    }

    public CardType getSuperType() {
        return superType;
    }

    public List<CardSubtype> getSubType() {
        return subTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardClassification that = (CardClassification) o;
        return superType == that.superType && subTypes == that.subTypes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(superType, subTypes);
    }

    @Override
    public String toString() {
        return "CardClassification{" +
                "superType=" + superType +
                ", subTypes=" + subTypes +
                '}';
    }
}
