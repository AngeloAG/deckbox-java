package com.molardev.deckbox.infrastructure.persistence.translations;

import java.util.List;

import com.molardev.deckbox.domain.service.DeckSizeRule;
import com.molardev.deckbox.infrastructure.persistence.entity.RuleEntity;
import com.molardev.deckbox.infrastructure.persistence.enums.RuleType;

public class RuleTranslator {
	private RuleTranslator() {}

	public static RuleEntity toEntity(DeckSizeRule rule) {
		return new RuleEntity(rule.getId(), RuleType.DECK_SIZE.toString(), List.of(String.valueOf(rule.getMaxCount())));
	}
}
