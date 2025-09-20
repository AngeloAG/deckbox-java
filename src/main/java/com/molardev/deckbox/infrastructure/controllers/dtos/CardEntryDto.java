package com.molardev.deckbox.infrastructure.controllers.dtos;

public class CardEntryDto {
	public final CardDto card;
	public final int count;

	public CardEntryDto(CardDto card, int count) {
		this.card = card;
		this.count = count;
	}
}
