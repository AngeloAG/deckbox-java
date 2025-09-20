package com.molardev.deckbox.infrastructure.controllers.dtos;

import java.util.List;

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
}
