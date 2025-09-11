package com.molardev.deckbox.domain.valueobject;

import java.util.UUID;

public class DeckFormatReference {
		private final UUID id;
		private final String name;
		private final String description;

		public DeckFormatReference(String name, String description) {
				this.name = name;
				this.description = description;
		}

		public String getName() {
				return name;
		}

		public String getDescription() {
				return description;
		}
}
