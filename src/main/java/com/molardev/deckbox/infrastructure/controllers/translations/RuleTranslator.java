package com.molardev.deckbox.infrastructure.controllers.translations;

import java.util.Map;

import com.molardev.deckbox.domain.entity.Format;
import com.molardev.deckbox.domain.enums.ElementalType;
import com.molardev.deckbox.domain.enums.Legality;
import com.molardev.deckbox.domain.service.DeckSizeRule;
import com.molardev.deckbox.domain.service.ElementalTypeRule;
import com.molardev.deckbox.domain.service.IDeckValidationRule;
import com.molardev.deckbox.domain.service.LegalitiesRule;
import com.molardev.deckbox.domain.service.MaxCopiesRule;
import com.molardev.deckbox.domain.valueobject.FormatReference;
import com.molardev.deckbox.infrastructure.controllers.dtos.FormatDto;
import com.molardev.deckbox.infrastructure.controllers.dtos.RuleDto;
import com.molardev.deckbox.infrastructure.persistence.enums.RuleType;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public class RuleTranslator {
  private RuleTranslator() {}

  public static RuleDto toDto(DeckSizeRule rule) {
    return new RuleDto(rule.getId(), RuleType.DECK_SIZE.toString(), Map.of("maxCount", String.valueOf(rule.getMaxCount())));
  }

  public static RuleDto toDto(ElementalTypeRule rule) {
    return new RuleDto(rule.getId(), 
      RuleType.ELEMENTAL_TYPE.toString(), 
      Map.of(
        "disallowedTypes", String.join(",", rule.getDisallowedTypes().map(ElementalType::toString)),
        "maxElementalTypes", String.valueOf(rule.getMaxElementalTypes())  
      ));
  }

  public static RuleDto toDto(LegalitiesRule rule) {
    return new RuleDto(rule.getId(),
    RuleType.LEGALITY.toString(),
    Map.of(
      "disallowedLegalities", String.join(",", rule.getDisallowedLegalities().map(Legality::toString))
    ));
  }

  public static RuleDto toDto(MaxCopiesRule rule) {
    return new RuleDto(rule.getId(),
      RuleType.MAX_COPIES.toString(),
      Map.of(
        "maxCopies", String.valueOf(rule.getMaxCopies()),
        "cardType", rule.getCardType().toString()
      )
    );
  } 

  public static RuleDto toDto(IDeckValidationRule rule) {
    if (rule instanceof DeckSizeRule) {
      return toDto((DeckSizeRule) rule);
    } else if(rule instanceof ElementalTypeRule) {
      return toDto((ElementalTypeRule) rule);
    } else if(rule instanceof LegalitiesRule) {
      return toDto((LegalitiesRule) rule);
    } else if(rule instanceof MaxCopiesRule) {
      return toDto((MaxCopiesRule) rule);
    } else {
        throw new IllegalArgumentException("Unknown rule type: " + rule.getClass());
    }
  }

  public static Validation<Seq<String>, IDeckValidationRule> toValidationRule(RuleDto rule) {
    var ruleTypeValidation = RuleType.fromString(rule.ruleType);
    if(ruleTypeValidation.isInvalid()) {
      return Validation.invalid(ruleTypeValidation.getError());
    }
    return switch(ruleTypeValidation.get()) {
      case DECK_SIZE -> DeckSizeRule.create(rule.id, rule.parameters).map(IDeckValidationRule.class::cast);
      case ELEMENTAL_TYPE -> ElementalTypeRule.create(rule.id, rule.parameters).map(IDeckValidationRule.class::cast);
      case LEGALITY -> LegalitiesRule.create(rule.id, rule.parameters).map(IDeckValidationRule.class::cast);
      case MAX_COPIES -> MaxCopiesRule.create(rule.id, rule.parameters).map(IDeckValidationRule.class::cast);
    };
  }

  public static FormatDto toDto(Format format) {
    return new FormatDto(
      format.getFormatReference().getId().toString(),
      format.getFormatReference().getName(),
      format.getFormatReference().getDescription(),
      format.getRules().map(rule -> RuleTranslator.toDto(rule)).asJava()
    );
  }

  public static FormatDto toDto(FormatReference formatRef) {
    return new FormatDto(
      formatRef.getId().toString(),
      formatRef.getName(),
      formatRef.getDescription(),
      java.util.List.of()
    );
  }
}
