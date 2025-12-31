package com.molardev.deckbox.infrastructure.controllers.dtos;

import java.util.List;

import com.molardev.deckbox.domain.valueobject.Card;

public class CardDto {
	public final String id;
	public final String name;
	public final String supertype;
	public final List<String> subtypes;
	public final List<String> types;
	public final List<String> legalities;
	public final String image;

	public CardDto(String id, String name, String supertype, List<String> subtypes, List<String> types, List<String> legalities, String image) {
		this.id = id;
		this.name = name;
		this.supertype = supertype;
		this.subtypes = subtypes;
		this.types = types;
		this.legalities = legalities;
		this.image = image;
	}

	public static CardDto fromEntity(Card entity) {
		return new CardDto(
			entity.getCardReference().getCardId(),
			entity.getCardReference().getName(),
			entity.getCardClassification().getSuperType().toString(),
			entity.getCardClassification().getSubType().map(type -> type.toString()).asJava(),
			entity.getElementalTypes().map(type -> type.toString()).asJava(),
			entity.getLegalities().map(legality -> legality.toString()).asJava(),
			entity.getImageUrl().toString()
		);
	}
}
