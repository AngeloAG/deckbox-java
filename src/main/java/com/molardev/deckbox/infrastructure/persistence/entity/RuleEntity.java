package com.molardev.deckbox.infrastructure.persistence.entity;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class RuleEntity {
	
	@Id
	private UUID id;
	private String ruleType;
	private List<String> parameters;

	protected RuleEntity() {}

	public RuleEntity(UUID id, String ruleType, List<String> parameters) {
		this.id = id;
		this.ruleType = ruleType;
		this.parameters = parameters;
	}

	public UUID getId() {
		return id;
	}

	public String getRuleType() {
		return ruleType;
	}

	public List<String> getParameters() {
		return parameters;
	}
}
