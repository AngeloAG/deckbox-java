package com.molardev.deckbox.infrastructure.controllers.dtos;

import com.molardev.deckbox.domain.valueobject.CardEntry;

public class CardEntryDto {
	public final CardDto card;
	public final int count;

	public CardEntryDto(CardDto card, int count) {
		this.card = card;
		this.count = count;
	}

	public static CardEntryDto fromEntity(CardEntry entity) {
		return new CardEntryDto(
			CardDto.fromEntity(entity.getCard()),
			entity.getCount().getValue()
		);
	}
}
