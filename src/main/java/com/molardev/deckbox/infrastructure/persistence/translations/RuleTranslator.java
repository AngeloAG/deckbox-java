package com.molardev.deckbox.infrastructure.persistence.translations;

import java.util.Arrays;
import java.util.List;

import com.molardev.deckbox.domain.enums.ElementalType;
import com.molardev.deckbox.domain.service.DeckSizeRule;
import com.molardev.deckbox.domain.service.ElementalTypeRule;
import com.molardev.deckbox.domain.service.LegalitiesRule;
import com.molardev.deckbox.domain.service.MaxCopiesRule;
import com.molardev.deckbox.infrastructure.persistence.entity.RuleEntity;
import com.molardev.deckbox.infrastructure.persistence.enums.RuleType;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public class RuleTranslator {
	private RuleTranslator() {}

	public static RuleEntity toEntity(DeckSizeRule rule) {
		return new RuleEntity(rule.getId(), RuleType.DECK_SIZE.toString(), List.of(String.valueOf(rule.getMaxCount())));
	}

	public static RuleEntity toEntity(ElementalTypeRule rule) {
		return new RuleEntity(
			rule.getId(), 
			RuleType.ELEMENTAL_TYPE.toString(), 
			List.of(String.join(",", rule.getDisallowedTypes().map(Object::toString).asJava()), Integer.toString(rule.getMaxElementalTypes()))
		);
	}

	public static RuleEntity toEntity(LegalitiesRule rule) {
		return new RuleEntity(
			rule.getId(), 
			RuleType.LEGALITY.toString(), 
			List.of(String.join(",", rule.getDisallowedLegalities().map(Object::toString).asJava()))
		);
	}

	public static RuleEntity toEntity(MaxCopiesRule rule) {
		return new RuleEntity(
			rule.getId(), 
			RuleType.MAX_COPIES.toString(), 
			List.of(Integer.toString(rule.getMaxCopies()), rule.getCardType().toString())
		);
	}

	public static DeckSizeRule toDomainDeckSizeRule(RuleEntity rule) {
		return new DeckSizeRule(rule.getId(), Integer.parseInt(rule.getParameters().get(0)));
	}

	public static Validation<Seq<String>, ElementalTypeRule> toDomainElementalTypeRule(RuleEntity rule) {
		Long id = rule.getId();
		String[] typesArray = rule.getParameters().get(0).split(",");
		int maxElementalTypes = Integer.parseInt(rule.getParameters().get(1));
		return ElementalType.fromStrings(io.vavr.collection.List.of(typesArray))
			.map(types -> new ElementalTypeRule(id, io.vavr.collection.List.ofAll(types), maxElementalTypes));
	}
}

