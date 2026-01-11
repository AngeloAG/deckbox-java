package com.molardev.deckbox.infrastructure.controllers.dtos;

import java.util.Map;

public class RuleDto {
  public Long id;
  public String ruleType;
  public Map<String, String> parameters;

  public RuleDto(Long id, String ruleType, Map<String, String> params) {
    this.id = id;
    this.ruleType = ruleType;
    this.parameters = params;
  }
}
