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

import io.vavr.collection.List;
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

  public static Validation<Seq<String>, DeckSizeRule> toDeckSizeRule(RuleDto rule) {
    Seq<String> errors = io.vavr.collection.List.of();
    if(!rule.parameters.containsKey("maxCount")) {
      errors = errors.append("Rule is missing parameter maxCount");
    }
    if(!rule.parameters.get("maxCount").matches("-?\\d+")) {
      errors = errors.append("maxCount paramenter must be a valid integer");
    }
    return errors.isEmpty() ? DeckSizeRule.create(rule.id, Integer.parseInt(rule.parameters.get("maxCount"))) : Validation.invalid(errors);
  }

  public static Validation<Seq<String>, ElementalTypeRule> toElementalTypeRule(RuleDto rule) {
    Seq<String> errors = io.vavr.collection.List.of();
    if(!rule.parameters.containsKey("disallowedTypes")) {
      errors = errors.append("Parameter disallowedTypes is missing");
    }
    if(!rule.parameters.containsKey("maxElementalTypes")) {
      errors = errors.append("Parameter maxElementalTypes is missing");
    }
    if(!rule.parameters.get("maxElementalTypes").matches("-?\\d+")) {
      errors = errors.append("Paramater maxElementalTypes must be a valid integer");
    }
    List<String> disallowedTypes = io.vavr.collection.List.of(rule.parameters.get("disallowedTypes").split(","));
    return errors.isEmpty() ? ElementalTypeRule.create(rule.id, disallowedTypes, Integer.parseInt(rule.parameters.get("maxElementalTypes"))) : Validation.invalid(errors);
  }

  public static Validation<Seq<String>, LegalitiesRule> toLegalitiesRule(RuleDto rule) {
    Seq<String> errors = io.vavr.collection.List.of();
    if(!rule.parameters.containsKey("disallowedLegalities")) {
      errors = errors.append("The parameter dissalowedLegalities is missing");
    }
    List<String> disallowedLegalities = io.vavr.collection.List.of(rule.parameters.get("disallowedLegalities").split(","));
    return errors.isEmpty() ? LegalitiesRule.create(rule.id, disallowedLegalities) : Validation.invalid(errors);
  }

  public static Validation<Seq<String>, MaxCopiesRule> toMaxCopiesRule(RuleDto rule) {
    Seq<String> errors = io.vavr.collection.List.of();
    if(!rule.parameters.containsKey("maxCopies")) {
      errors = errors.append("The parameter maxCopies is missing");
    }
    if(!rule.parameters.get("maxCopies").matches("-?\\\\d+")) {
      errors = errors.append("The parameters maxCopies must be a valid integer");
    }
    if(!rule.parameters.containsKey("cardType")) {
      errors = errors.append("The parameter cardType is missing");
    }
    return errors.isEmpty() ? MaxCopiesRule.create(rule.id, Integer.parseInt(rule.parameters.get("maxCopies")), rule.parameters.get("cardType")) : Validation.invalid(errors);
  }

  public static Validation<Seq<String>, IDeckValidationRule> toValidationRule(RuleDto rule) {
    var ruleTypeValidation = RuleType.fromString(rule.ruleType);
    if(ruleTypeValidation.isInvalid()) {
      return Validation.invalid(ruleTypeValidation.getError());
    }
    return switch(ruleTypeValidation.get()) {
      case DECK_SIZE -> toDeckSizeRule(rule).map(IDeckValidationRule.class::cast);
      case ELEMENTAL_TYPE -> toElementalTypeRule(rule).map(IDeckValidationRule.class::cast);
      case LEGALITY -> toLegalitiesRule(rule).map(IDeckValidationRule.class::cast);
      case MAX_COPIES -> toMaxCopiesRule(rule).map(IDeckValidationRule.class::cast);
    };
  }
}
