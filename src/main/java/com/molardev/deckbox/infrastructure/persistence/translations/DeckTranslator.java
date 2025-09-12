package com.molardev.deckbox.infrastructure.persistence.translations;



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
import com.molardev.deckbox.domain.valueobject.DeckName;
import com.molardev.deckbox.domain.valueobject.DeckReference;
import com.molardev.deckbox.infrastructure.persistence.entity.CardEntity;
import com.molardev.deckbox.infrastructure.persistence.entity.CardEntryEntity;
import com.molardev.deckbox.infrastructure.persistence.entity.DeckEntity;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public class DeckTranslator {

	private DeckTranslator() {
		// Private constructor to prevent instantiation
	}

	public static DeckEntity toEntity(Deck deck) {
		DeckEntity entity = new DeckEntity();
		entity.setId(deck.getDeckReference().getId());
		entity.setName(deck.getDeckReference().getName().getName());
		entity.setCardEntries(deck.getCardEntries().map(DeckTranslator::toEntity).toJavaList());
		return entity;
	}

	public static Validation<Seq<String>, Deck> toDomain(DeckEntity entity) {
		return DeckName.create(entity.getName()).flatMap(name -> DeckReference.create(name, entity.getId()))
				.flatMap(deckRef -> Deck.create(deckRef, io.vavr.collection.List.empty()));
	}

	public static Validation<Seq<String>, Card> toDomain(CardEntity entity) {
		Validation<Seq<String>, CardReference> cardRefVal = CardReference.create(entity.getId(), entity.getCardName());
		CardType cardType = CardType.valueOf(entity.getType());
		io.vavr.collection.List<CardSubtype> subTypes = io.vavr.collection.List.ofAll(entity.getSubTypes()).map(CardSubtype::valueOf);
		Validation<Seq<String>, CardClassification> classVal = CardClassification.create(cardType, subTypes);
		io.vavr.collection.List<ElementalType> elementalTypes = io.vavr.collection.List.ofAll(entity.getElementalTypes()).map(ElementalType::valueOf);
		io.vavr.collection.List<Legality> legalities = io.vavr.collection.List.ofAll(entity.getLegalities()).map(Legality::valueOf);
		Validation<Seq<String>, CardImageUrl> imageUrlVal = CardImageUrl.create(entity.getImageUrl());

		// Combine all validations in the correct order for Card.create
		return cardRefVal.combine(classVal)
			.combine(Validation.valid(legalities))
			.combine(Validation.valid(elementalTypes))
			.combine(imageUrlVal)
			.ap(Card::create)
			.mapError(errors -> errors.flatMap(x -> x))
			.flatMap(v -> v); // flatten nested Validation
	}

	public static CardEntity toEntity(Card card) {
		CardEntity entity = new CardEntity();
		entity.setId(card.getCardReference().getCardId());
		entity.setCardName(card.getCardReference().getName());
		entity.setType(card.getCardClassification().getSuperType().name());
		entity.setSubTypes(card.getCardClassification().getSubType().map(Enum::name).toJavaList());
		entity.setElementalTypes(card.getElementalTypes().map(Enum::name).toJavaList());
		entity.setLegalities(card.getLegalities().map(Enum::name).toJavaList());
		entity.setImageUrl(card.getImageUrl() != null ? card.getImageUrl().getValue() : null);
		return entity;
	}

	public static CardEntryEntity toEntity(CardEntry entry) {
		CardEntryEntity entity = new CardEntryEntity();
		entity.setCard(toEntity(entry.getCard()));
		entity.setCount(entry.getCount().getValue());
		return entity;
	}

	public static Validation<Seq<String>, CardEntry> toDomain(CardEntryEntity entity) {
		return toDomain(entity.getCard())
			.flatMap(card -> CardCount.create(entity.getCount())
				.flatMap(count -> CardEntry.create(card, count))
			);
	}
}
