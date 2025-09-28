package com.molardev.deckbox.infrastructure.persistence.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class RuleEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String ruleType;
	private List<String> parameters;

	protected RuleEntity() {}

	public RuleEntity(Long id, String ruleType, List<String> parameters) {
		this.id = id;
		this.ruleType = ruleType;
		this.parameters = parameters;
	}

	public Long getId() {
		return id;
	}

	public String getRuleType() {
		return ruleType;
	}

	public List<String> getParameters() {
		return parameters;
	}
}
