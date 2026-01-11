package com.molardev.deckbox.infrastructure.controllers.translations;

import java.util.Map;

import com.molardev.deckbox.domain.enums.ElementalType;
import com.molardev.deckbox.domain.enums.Legality;
import com.molardev.deckbox.domain.service.DeckSizeRule;
import com.molardev.deckbox.domain.service.ElementalTypeRule;
import com.molardev.deckbox.domain.service.IDeckValidationRule;
import com.molardev.deckbox.domain.service.LegalitiesRule;
import com.molardev.deckbox.domain.service.MaxCopiesRule;
import com.molardev.deckbox.infrastructure.controllers.dtos.RuleDto;
import com.molardev.deckbox.infrastructure.persistence.enums.RuleType;

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
}
