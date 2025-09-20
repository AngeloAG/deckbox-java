package com.molardev.deckbox.infrastructure.controllers.dtos;

public class DeckDto {
	public final String id;
	public final String name;
	public final java.util.List<CardEntryDto> cardEntries;

	public DeckDto(String id, String name, java.util.List<CardEntryDto> cardEntries) {
		this.id = id;
		this.name = name;
		this.cardEntries = cardEntries;
	}
}
