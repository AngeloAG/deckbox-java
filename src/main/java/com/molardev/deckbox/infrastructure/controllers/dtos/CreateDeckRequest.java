package com.molardev.deckbox.infrastructure.controllers.dtos;

public class CreateDeckRequest {
		private String name;

		public CreateDeckRequest() {}

		public CreateDeckRequest(String name) {
				this.name = name;
		}

		public String getName() {
				return name;
		}

		public void setName(String name) {
				this.name = name;
		}
}
