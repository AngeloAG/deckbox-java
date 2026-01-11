package com.molardev.deckbox.infrastructure.persistence.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

@Entity
public class FormatEntity {
  
  @Id
  private UUID id;
  private String name;
  private String description;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "format_id")
  private List<RuleEntity> rules = new ArrayList<>();

  protected FormatEntity() {}

  public FormatEntity(UUID id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public List<RuleEntity> getRuleEntities() {
    return rules;
  }

  public void setRules(List<RuleEntity> rules) {
    this.rules = rules;
  }
}
