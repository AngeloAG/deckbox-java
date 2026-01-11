package com.molardev.deckbox.infrastructure.persistence.translations;

import java.util.List;

import com.molardev.deckbox.domain.entity.Format;
import com.molardev.deckbox.domain.enums.CardType;
import com.molardev.deckbox.domain.enums.ElementalType;
import com.molardev.deckbox.domain.enums.Legality;
import com.molardev.deckbox.domain.service.DeckSizeRule;
import com.molardev.deckbox.domain.service.ElementalTypeRule;
import com.molardev.deckbox.domain.service.IDeckValidationRule;
import com.molardev.deckbox.domain.service.LegalitiesRule;
import com.molardev.deckbox.domain.service.MaxCopiesRule;
import com.molardev.deckbox.domain.valueobject.FormatReference;
import com.molardev.deckbox.infrastructure.persistence.entity.FormatEntity;
import com.molardev.deckbox.infrastructure.persistence.entity.RuleEntity;
import com.molardev.deckbox.infrastructure.persistence.enums.RuleType;

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

	public static FormatEntity toEntity(Format format) {
		var entity = new FormatEntity(format.getFormatReference().getId(), format.getFormatReference().getName(), format.getFormatReference().getDescription());
		var rules = format.getRules().map(rule -> toEntity(rule)).asJava();
		entity.setRules(rules);
		return entity;
	}

	public static DeckSizeRule toDomainDeckSizeRule(RuleEntity rule) {
		return new DeckSizeRule(rule.getId(), Integer.parseInt(rule.getParameters().get(0)));
	}

	public static ElementalTypeRule toDomainElementalTypeRule(RuleEntity rule) {
		Long id = rule.getId();
		String[] typesArray = rule.getParameters().get(0).split(",");
		int maxElementalTypes = Integer.parseInt(rule.getParameters().get(1));
		return ElementalType.fromStrings(io.vavr.collection.List.of(typesArray))
			.map(types -> new ElementalTypeRule(id, io.vavr.collection.List.ofAll(types), maxElementalTypes)).get();
	}

	public static LegalitiesRule rehydrateLegalitiesRule(RuleEntity rule) {
		Long id = rule.getId();
		String[] disallowedLegalitiesString = rule.getParameters().get(0).split(",");
		return Legality.fromStrings(io.vavr.collection.List.of(disallowedLegalitiesString))
			.fold(ignore -> new LegalitiesRule(id, io.vavr.collection.List.empty()), 
						disallowedLegalities -> new LegalitiesRule(id, disallowedLegalities));
	}

	public static MaxCopiesRule rehydrateMaxCopiesRule(RuleEntity rule) {
		Long id = rule.getId();
		CardType type = CardType.fromString(rule.getParameters().get(1)).get();
		return new MaxCopiesRule(id, Integer.parseInt(rule.getParameters().get(0)), type);
	}

	public static Format rehydrateFormat(FormatEntity formatEntity) {
		FormatReference reference = FormatReference.create(formatEntity.getId(), formatEntity.getName(), formatEntity.getDescription()).get();
		var rules = io.vavr.collection.List.ofAll(formatEntity.getRuleEntities()).map(rule -> rehydrateRule(rule));
		return Format.create(reference, rules).get();
	}

	public static IDeckValidationRule rehydrateRule(RuleEntity entity) {
		var ruleType = RuleType.fromString(entity.getRuleType()).get();
		return switch (ruleType) {
			case DECK_SIZE -> toDomainDeckSizeRule(entity);
			case ELEMENTAL_TYPE -> toDomainElementalTypeRule(entity);
			case LEGALITY -> rehydrateLegalitiesRule(entity);
			case MAX_COPIES -> rehydrateMaxCopiesRule(entity);
		};
	}

	public static RuleEntity toEntity(IDeckValidationRule rule) {
		if (rule instanceof DeckSizeRule) {
        return toEntity((DeckSizeRule) rule);
    } else if (rule instanceof ElementalTypeRule) {
        return toEntity((ElementalTypeRule) rule);
    } else if (rule instanceof LegalitiesRule) {
        return toEntity((LegalitiesRule) rule);
		} else if (rule instanceof MaxCopiesRule) {
			return toEntity((MaxCopiesRule) rule);
		} else {
        throw new IllegalArgumentException("Unknown rule type: " + rule.getClass());
    }
	}
}

