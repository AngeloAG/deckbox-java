package com.molardev.deckbox.domain.service;

import java.util.Map;
import java.util.UUID;

import com.molardev.deckbox.domain.entity.Deck;
import com.molardev.deckbox.domain.enums.Legality;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public class LegalitiesRule implements IDeckValidationRule {
	private final UUID id;
	private final List<Legality> disallowedLegalities;

	public LegalitiesRule(UUID id, List<Legality> disallowedLegalities) {
		this.id = id;
		this.disallowedLegalities = disallowedLegalities;
	}

	@Override
	public Validation<Seq<String>, Deck> validate(Deck deck) {
		List<String> errors = deck.getCardEntries()
			.filter(entry -> entry.getCard().getLegalities().exists(disallowedLegalities::contains))
			.map(entry -> "Card " + entry.getCard() + " has disallowed legality.")
			.toList();

		return errors.isEmpty() ? Validation.valid(deck) : Validation.invalid(errors);
	}


	public static Validation<Seq<String>, LegalitiesRule> create(UUID id, List<String> disallowedLegalities) {
		Seq<String> errors = io.vavr.collection.List.of();
		if(id == null) {
			errors = errors.append("Id of the legalities rule cannot be null");
		}
		var legalitiesValidation = Legality.fromStrings(disallowedLegalities);
		if(legalitiesValidation.isInvalid()) {
			errors = errors.appendAll(legalitiesValidation.getError());
		}

		return errors.isEmpty() ? Validation.valid(new LegalitiesRule(id, legalitiesValidation.get())) : Validation.invalid(errors);
	}

	public static Validation<Seq<String>, LegalitiesRule> create(List<String> disallowedLegalities) {
		Seq<String> errors = io.vavr.collection.List.of();
		var legalitiesValidation = Legality.fromStrings(disallowedLegalities);
		if(legalitiesValidation.isInvalid()) {
			errors = errors.appendAll(legalitiesValidation.getError());
		}

		return errors.isEmpty() ? Validation.valid(new LegalitiesRule(UUID.randomUUID(), legalitiesValidation.get())) : Validation.invalid(errors);
	}

  public static Validation<Seq<String>, LegalitiesRule> create(Map<String, String> params) {
    Seq<String> errors = validateParams(params);
    List<String> disallowedLegalities = io.vavr.collection.List.of(params.get("disallowedLegalities").split(","));
    return errors.isEmpty() ? LegalitiesRule.create(disallowedLegalities) : Validation.invalid(errors);
  }

  public static Validation<Seq<String>, LegalitiesRule> create(UUID id, Map<String, String> params) {
    Seq<String> errors = validateParams(params);
    if(id == null) {
      errors = errors.append("The id of the rule cannot be null");
    }
    List<String> disallowedLegalities = io.vavr.collection.List.of(params.get("disallowedLegalities").split(","));
    return errors.isEmpty() ? LegalitiesRule.create(id, disallowedLegalities) : Validation.invalid(errors);
  }

  private static Seq<String> validateParams(Map<String, String> params) {
    Seq<String> errors = io.vavr.collection.List.of();
    if(!params.containsKey("disallowedLegalities")) {
      errors = errors.append("The parameter dissalowedLegalities is missing");
    }
    return errors;
  }

	public UUID getId() {
		return id;
	}

	public List<Legality> getDisallowedLegalities() {
		return disallowedLegalities;
	}
}
