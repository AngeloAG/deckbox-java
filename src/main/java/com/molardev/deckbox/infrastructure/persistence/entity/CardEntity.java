package com.molardev.deckbox.infrastructure.persistence.entity;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "cards")
public class CardEntity {
    
	  @Id
    private String id;
    private String cardName;
		private String type;
		private List<String> subTypes;
		private List<String> elementalTypes;
		private List<String> legalities;
		private String imageUrl;

    public CardEntity() {}

    public CardEntity(String id, String cardName) {
        this.id = id;
        this.cardName = cardName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

		public String getType() {
			return type;
		}

		public List<String> getSubTypes() {
			return subTypes;
		}

		public List<String> getElementalTypes() {
			return elementalTypes;
		}

		public List<String> getLegalities() {
			return legalities;
		}

		public String getImageUrl() {
			return imageUrl;
		}

		public void setType(String type) {
			this.type = type;
		}

		public void setSubTypes(List<String> subTypes) {
			this.subTypes = subTypes;
		}

		public void setElementalTypes(List<String> elementalTypes) {
			this.elementalTypes = elementalTypes;
		}

		public void setLegalities(List<String> legalities) {
			this.legalities = legalities;
		}

		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}
}
