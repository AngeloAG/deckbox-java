package com.molardev.deckbox.infrastructure.controllers.dtos;

import java.util.List;

public class FormatDto {
  public String id;
  public String name;
  public String description;
  public List<RuleDto> rules;

  public FormatDto(String id, String name, String description, List<RuleDto> rules) {
    this.id = id;
    this.name = name; 
    this.description = description;
    this.rules = rules;
  }
}
