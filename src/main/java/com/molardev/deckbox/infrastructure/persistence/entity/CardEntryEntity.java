package com.molardev.deckbox.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "card_entries")
public class CardEntryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

		@ManyToOne
		@JoinColumn(name = "card_id", referencedColumnName = "id")
    private CardEntity card;
		private int count;

		// Constructors
		public CardEntryEntity() {
		}

		public CardEntryEntity(Long id, CardEntity card, int count) {
				this.id = id;
				this.card = card;
				this.count = count;
		}

		public Long getId() {
				return id;
		}

		public void setId(Long id) {
				this.id = id;
		}

		public CardEntity getCard() {
				return card;
		}

		public void setCard(CardEntity card) {
				this.card = card;
		}

		public int getCount() {
				return count;
		}

		public void setCount(int count) {
				this.count = count;
		}
}
