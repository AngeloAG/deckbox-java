package com.molardev.deckbox.infrastructure.controllers.translations;

import java.util.function.Function;

import com.molardev.deckbox.domain.entity.Deck;
import com.molardev.deckbox.domain.enums.CardSubtype;
import com.molardev.deckbox.domain.enums.CardType;
import com.molardev.deckbox.domain.enums.ElementalType;
import com.molardev.deckbox.domain.enums.Legality;
import com.molardev.deckbox.domain.valueobject.Card;
import com.molardev.deckbox.domain.valueobject.CardClassification;
import com.molardev.deckbox.domain.valueobject.CardCount;
import com.molardev.deckbox.domain.valueobject.CardEntry;
import com.molardev.deckbox.domain.valueobject.CardImageUrl;
import com.molardev.deckbox.domain.valueobject.CardReference;
import com.molardev.deckbox.infrastructure.controllers.dtos.CardDto;
import com.molardev.deckbox.infrastructure.controllers.dtos.CardEntryDto;
import com.molardev.deckbox.infrastructure.controllers.dtos.DeckDto;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public class DeckTranslator {
	private DeckTranslator() {
		// Private constructor to prevent instantiation
	}

	public static Validation<Seq<String>, Card> toDomain(CardDto dto) {
		Validation<Seq<String>, CardReference> cardReference = CardReference.create(dto.id, dto.name);
		Validation<Seq<String>, CardClassification> cardClassification = toCardClassification(dto);
		Validation<Seq<String>, Seq<Legality>> legalities = Validation.sequence(io.vavr.collection.List.ofAll(dto.legalities).map(Legality::fromString));
		Validation<Seq<String>, Seq<ElementalType>> elementalTypes = Validation.sequence(io.vavr.collection.List.ofAll(dto.types).map(ElementalType::fromString));
		Validation<Seq<String>, CardImageUrl> imageUrl = CardImageUrl.create(dto.image);

		return Validation.combine(cardReference, cardClassification, legalities, elementalTypes, imageUrl)
			.ap((cardRef, classif, legal, elemTypes, img) -> Card.create(cardRef, classif, legal.toList(), elemTypes.toList(), img))
			.mapError(seqOfSeq -> seqOfSeq.flatMap(Function.identity()))
			.flatMap(Function.identity());
	}

	private static Validation<Seq<String>, CardClassification> toCardClassification(CardDto dto) {
		return CardType.fromString(dto.supertype).flatMap(type ->
				Validation.sequence(
						io.vavr.collection.List.ofAll(dto.subtypes).map(CardSubtype::fromString)
				).flatMap(subtypes -> CardClassification.create(type, subtypes.toList()))
		);
	}

	public static Validation<Seq<String>, CardEntry> toDomain(CardEntryDto dto) {
		return toDomain(dto.card)
				.flatMap(card -> CardCount.create(dto.count)
						.flatMap(cardCount -> CardEntry.create(card, cardCount))
				);
	}

	public static CardDto toDto(Card card) {
		return new CardDto(
			card.getCardReference().getCardId(), 
			card.getCardReference().getName(), 
			card.getCardClassification().getSuperType().toString(), 
			card.getCardClassification().getSubType().map(Enum::name).asJava(), 
			card.getLegalities().map(Enum::name).asJava(), 
			card.getElementalTypes().map(Enum::name).asJava(), 
			card.getImageUrl().getValue()
		);
	}

	public static CardEntryDto toDto(CardEntry cardEntry) {
		return new CardEntryDto(toDto(cardEntry.getCard()), cardEntry.getCount().getValue());
	}

	public static DeckDto toDto(Deck deck) {
		return new DeckDto(
			deck.getDeckReference().getId().toString(),
			deck.getDeckReference().getName().getName(),
			deck.getCardEntries().map(DeckTranslator::toDto).asJava()
		);
	}
}
