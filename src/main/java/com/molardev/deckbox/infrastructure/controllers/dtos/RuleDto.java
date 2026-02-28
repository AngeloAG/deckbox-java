package com.molardev.deckbox.infrastructure.controllers.dtos;

import java.util.Map;
import java.util.UUID;

public class RuleDto {
  public UUID id;
  public String ruleType;
  public Map<String, String> parameters;

  public RuleDto(UUID id, String ruleType, Map<String, String> params) {
    this.id = id;
    this.ruleType = ruleType;
    this.parameters = params;
  }
}
