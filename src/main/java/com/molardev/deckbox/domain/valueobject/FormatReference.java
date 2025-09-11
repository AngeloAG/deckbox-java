package com.molardev.deckbox.domain.valueobject;

import java.util.UUID;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public class FormatReference {
		private final UUID id;
		private final String name;
		private final String description;

		private FormatReference(UUID id, String name, String description) {
				this.id = id;
				this.name = name;
				this.description = description;
		}

		public static Validation<Seq<String>, FormatReference> create(UUID id, String name, String description) {
				if(id == null) {
						return Validation.invalid(io.vavr.collection.List.of("Deck format id must not be null"));
				}
				if (name == null || name.isBlank()) {
						return Validation.invalid(io.vavr.collection.List.of("Deck format name must not be null or blank"));
				}
				if (description == null) {
						return Validation.invalid(io.vavr.collection.List.of("Deck format description must not be null"));
				}

				return Validation.valid(new FormatReference(id, name, description));
		}

		public static Validation<Seq<String>, FormatReference> create(String name, String description) {
				UUID generatedId = UUID.randomUUID();
				return create(generatedId, name, description);
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
}
