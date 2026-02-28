package com.molardev.deckbox.domain.service;

import java.util.UUID;

import com.molardev.deckbox.domain.entity.Deck;
import com.molardev.deckbox.domain.enums.ElementalType;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public class ElementalTypeRule implements IDeckValidationRule {
	private final UUID id;
	private final List<ElementalType> disallowedTypes;
	private final int maxElementalTypes;

	public ElementalTypeRule(UUID id, List<ElementalType> disallowedTypes, int maxElementalTypes) {
		this.id = id;
		this.disallowedTypes = disallowedTypes;
		this.maxElementalTypes = maxElementalTypes;
	}

	@Override
	public Validation<Seq<String>, Deck> validate(Deck deck) {
		List<ElementalType> elementalTypes = deck.getCardEntries().filter(entry -> entry.getCard().getCardClassification().getSuperType() == com.molardev.deckbox.domain.enums.CardType.POKEMON).flatMap(entry -> entry.getCard().getElementalTypes());

		// Check for disallowed types on each Pokémon card
		List<String> errors = elementalTypes
			.filter(disallowedTypes::contains)
			.map(type -> "Card has disallowed elemental type: " + type)
			.toList();

		// Collect all elemental types from all Pokémon cards in the deck
		List<ElementalType> allTypes = elementalTypes.distinct();

		if (allTypes.size() > maxElementalTypes) {
			errors = errors.append(
				"Deck has too many different elemental types among Pokémon: " + allTypes.size() + " (max allowed: " + maxElementalTypes + ") Types: " + allTypes.mkString(", ")
			);
		}

		return errors.isEmpty() ? Validation.valid(deck) : Validation.invalid(errors);
	}

	public static Validation<Seq<String>, ElementalTypeRule> create(UUID id, List<String> disallowedTypes, int maxElementalTypes) {
		Seq<String> errors = io.vavr.collection.List.of();
		if(id == null) {
			errors = errors.append("The Id of the elemental type rule cannot be null");
		}
		if(maxElementalTypes < 0) {
			errors = errors.append("The max elemental types cannot be less than zero");
		}
		var disallowedTypesValidation = ElementalType.fromStrings(disallowedTypes);
		if(disallowedTypesValidation.isInvalid()) {
			errors = errors.appendAll(disallowedTypesValidation.getError());
		}
		return errors.isEmpty() ? Validation.valid(new ElementalTypeRule(id, disallowedTypesValidation.get(), maxElementalTypes)) : Validation.invalid(errors);
	}

	public static Validation<Seq<String>, ElementalTypeRule> create(List<String> disallowedTypes, int maxElementalTypes) {
		Seq<String> errors = io.vavr.collection.List.of();
		if(maxElementalTypes < 0) {
			errors = errors.append("The max elemental types cannot be less than zero");
		}
		var disallowedTypesValidation = ElementalType.fromStrings(disallowedTypes);
		if(disallowedTypesValidation.isInvalid()) {
			errors = errors.appendAll(disallowedTypesValidation.getError());
		}
		return errors.isEmpty() ? Validation.valid(new ElementalTypeRule(UUID.randomUUID(), disallowedTypesValidation.get(), maxElementalTypes)) : Validation.invalid(errors);
	}

	public UUID getId() {
		return id;
	}

	public List<ElementalType> getDisallowedTypes() {
		return disallowedTypes;
	}

	public int getMaxElementalTypes() {
		return maxElementalTypes;
	}
}
