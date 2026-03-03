package com.molardev.deckbox.domain.service;

import java.util.Map;

import com.molardev.deckbox.infrastructure.persistence.enums.RuleType;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public class RuleGenerator {
  public static Validation<Seq<String>, IDeckValidationRule> generate(String ruleType, Map<String, String> params) {
    var ruleTypeValidation = RuleType.fromString(ruleType);
    if(ruleTypeValidation.isInvalid()) {
      return Validation.invalid(ruleTypeValidation.getError());
    }
    return switch(ruleTypeValidation.get()) {
      case DECK_SIZE -> DeckSizeRule.create(params).map(IDeckValidationRule.class::cast);
      case ELEMENTAL_TYPE -> ElementalTypeRule.create(params).map(IDeckValidationRule.class::cast);
      case LEGALITY -> LegalitiesRule.create(params).map(IDeckValidationRule.class::cast);
      case MAX_COPIES -> MaxCopiesRule.create(params).map(IDeckValidationRule.class::cast);
    };
  }
}
