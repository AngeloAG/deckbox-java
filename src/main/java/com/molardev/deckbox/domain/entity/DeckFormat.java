package com.molardev.deckbox.domain.entity;

import com.molardev.deckbox.domain.service.IDeckValidationRule;
import com.molardev.deckbox.domain.valueobject.DeckFormatReference;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;

import java.util.Objects;
import java.util.UUID;

public class DeckFormat {
    private final DeckFormatReference formatReference;
    private final List<IDeckValidationRule> rules;

    private DeckFormat(UUID id, DeckFormatReference formatReference, List<IDeckValidationRule> rules) {
        this.id = id;
        this.formatReference = formatReference;
        this.rules = rules;
    }

		public static Validation<Seq<String>, DeckFormat> create(UUID id, DeckFormatReference formatReference, List<IDeckValidationRule> rules) {
			if(id == null) {
				return Validation.invalid(io.vavr.collection.List.of("Deck format id must not be null"));
			}
			if(formatReference == null) {
				return Validation.invalid(io.vavr.collection.List.of("Deck format reference must not be null"));
			}
			if(rules == null) {
				return Validation.invalid(io.vavr.collection.List.of("Deck format rules must not be null"));
			}

			return Validation.valid(new DeckFormat(id, formatReference, rules));
		}

		public static Validation<Seq<String>, DeckFormat> create(DeckFormatReference formatReference, List<IDeckValidationRule> rules) {
			UUID generatedId = UUID.randomUUID();
			return create(generatedId, formatReference, rules);
			if(rules == null) {
				return Validation.invalid(io.vavr.collection.List.of("Deck format rules must not be null"));
			}

			return Validation.valid(new DeckFormat(id, formatReference, rules));
		}

		public static Validation<Seq<String>, DeckFormat> create(String name, String description, List<IDeckValidationRule> rules) {
			UUID generatedId = UUID.randomUUID();
			return create(generatedId, new DeckFormatReference(name, description), rules);
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

    public List<IDeckValidationRule> getRules() {
        return rules;
    }

    public DeckFormat addRule(IDeckValidationRule rule) {
        return new DeckFormat(id, name, description, rules.append(rule));
    }

    public DeckFormat removeRule(IDeckValidationRule rule) {
        return new DeckFormat(id, name, description, rules.remove(rule));
    }

    public DeckFormat updateRule(IDeckValidationRule oldRule, IDeckValidationRule newRule) {
        return new DeckFormat(id, name, description, rules.remove(oldRule).append(newRule));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeckFormat that = (DeckFormat) o;
        return Objects.equals(name, that.name) &&
               Objects.equals(description, that.description) &&
               Objects.equals(rules, that.rules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, rules);
    }

    @Override
    public String toString() {
        return "DeckFormat{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", rules=" + rules +
                '}';
    }
}
