package com.molardev.deckbox.domain.service;

import java.util.Map;
import java.util.UUID;

import org.springframework.validation.Errors;

import com.molardev.deckbox.domain.entity.Deck;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public class DeckSizeRule implements IDeckValidationRule {
	private final UUID id;
	private final int maxCount;

	public DeckSizeRule(UUID id, int maxCount) {
		this.id = id;
		this.maxCount = maxCount;
	}

	@Override
	public Validation<Seq<String>, Deck> validate(Deck deck) {
		if(deck == null || deck.getCardEntries().length() > this.maxCount) {
			return Validation.invalid(List.of("Deck size exceeds maximum allowed limit of " + maxCount));
		}
		
		return Validation.valid(deck);
	}

	public static Validation<Seq<String>, DeckSizeRule> create(UUID id, int maxCount) {
		List<String> errors = io.vavr.collection.List.of();
		if(id == null) {
			errors = errors.append("Id of rule cannot be null");
		}
		if(maxCount < 0) {
			errors = errors.append("The mac count cannot be less than zero");
		}

		return errors.isEmpty() ? Validation.valid(new DeckSizeRule(id, maxCount)) : Validation.invalid(errors);
	}

	public static Validation<Seq<String>, DeckSizeRule> create(int maxCount) {
		List<String> errors = io.vavr.collection.List.of();
		if(maxCount < 0) {
			errors = errors.append("The max count cannot be less than zero");
		}
		return errors.isEmpty() ? Validation.valid(new DeckSizeRule(UUID.randomUUID(), maxCount)) : Validation.invalid(errors);
	}

  public static Validation<Seq<String>, DeckSizeRule> create(Map<String, String> params) {
    Seq<String> errors = validateParams(params);
    return errors.isEmpty() ? DeckSizeRule.create(Integer.parseInt(params.get("maxCount"))) : Validation.invalid(errors); 
  }

  public static Validation<Seq<String>, DeckSizeRule> create(UUID id, Map<String, String> params) {
    Seq<String> errors = validateParams(params);
    if(id == null) {
      errors = errors.append("Id of the rule cannot be null");
    }
    return errors.isEmpty() ? DeckSizeRule.create(id, Integer.parseInt(params.get("maxCount"))) : Validation.invalid(errors);
  }

  private static Seq<String> validateParams(Map<String, String> params) {
    Seq<String> errors = io.vavr.collection.List.of();
    if(!params.containsKey("maxCount")) {
      errors = errors.append("Rule is missing parameter maxCount");
    }
    if(!params.get("maxCount").matches("-?\\d+")) {
      errors = errors.append("maxCount paramenter must be a valid integer");
    }
    return errors;
  }

	public UUID getId() {
		return id;
	}

	public int getMaxCount() {
		return maxCount;
	}
}
