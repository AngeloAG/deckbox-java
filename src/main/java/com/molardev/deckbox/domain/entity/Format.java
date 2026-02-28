package com.molardev.deckbox.domain.entity;

import com.molardev.deckbox.domain.service.IDeckValidationRule;
import com.molardev.deckbox.domain.valueobject.FormatReference;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;

import java.util.Objects;
import java.util.UUID;

public class Format {
    private final FormatReference formatReference;
    private final List<IDeckValidationRule> rules;

    private Format(FormatReference formatReference, List<IDeckValidationRule> rules) {
        this.formatReference = formatReference;
        this.rules = rules;
    }

		public static Validation<Seq<String>, Format> create(FormatReference formatReference, List<IDeckValidationRule> rules) {

			if(formatReference == null) {
				return Validation.invalid(io.vavr.collection.List.of("Deck format reference must not be null"));
			}
			if(rules == null) {
				return Validation.invalid(io.vavr.collection.List.of("Deck format rules must not be null"));
			}

			return Validation.valid(new Format(formatReference, rules));
		}

    public static Validation<Seq<String>, Format> create(String name, String description) {
      return FormatReference.create(name, description)
        .map(formatReference -> new Format(formatReference, List.empty()));
    }

		public FormatReference getFormatReference() {
				return formatReference;
		}

    public List<IDeckValidationRule> getRules() {
        return rules;
    }

    public Format removeRule(UUID ruleId) {
        return new Format(formatReference, rules.filter(rule -> !rule.getId().equals(ruleId)));
    }

    public Format updateRule(UUID ruleId, IDeckValidationRule newRule) {
        return new Format(formatReference, 
            rules.filter(rule -> !rule.getId().equals(ruleId)).append(newRule)
        );
    }

    public boolean hasRule(UUID ruleId) {
        return rules.exists(rule -> rule.getId().equals(ruleId));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Format that = (Format) o;
        return Objects.equals(formatReference, that.formatReference) &&
               Objects.equals(rules, that.rules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(formatReference, rules);
    }

    @Override
    public String toString() {
        return "DeckFormat{" +
                "formatReference=" + formatReference +
                ", rules=" + rules +
                '}';
    }
}
