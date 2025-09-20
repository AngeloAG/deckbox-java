package com.molardev.deckbox.infrastructure.persistence;

import org.springframework.stereotype.Repository;

import com.molardev.deckbox.application.common.interfaces.ICardRepository;
import com.molardev.deckbox.domain.valueobject.Card;
import com.molardev.deckbox.infrastructure.persistence.entity.CardEntity;
import com.molardev.deckbox.infrastructure.persistence.jpa.CardJpaRepository;
import com.molardev.deckbox.infrastructure.persistence.translations.DeckTranslator;

import io.vavr.collection.Seq;
import io.vavr.control.Option;
import io.vavr.control.Validation;

@Repository
public class JpaCardRepository implements ICardRepository {
	private final CardJpaRepository cardJpaRepository;

	public JpaCardRepository(CardJpaRepository cardJpaRepository) {
		this.cardJpaRepository = cardJpaRepository;
	}

	@Override
	public Validation<Seq<String>, Card> save(Card card) {
		CardEntity entity = DeckTranslator.toEntity(card);
		CardEntity saved = cardJpaRepository.save(entity);
		return DeckTranslator.toDomain(saved);
	}

	@Override
	public Validation<Seq<String>, Option<Card>> findById(String id) {
		return  cardJpaRepository.findById(id)
			.map(entity -> DeckTranslator.toDomain(entity).map(Option::of))
			.orElseGet(() -> Validation.valid(Option.none()));
	}

	@Override
	public Validation<Seq<String>, Void> deleteById(String id) {
		return cardJpaRepository.findById(id).map(entity -> {
			cardJpaRepository.deleteById(id);
			return Validation.<Seq<String>, Void>valid(null);
		}).orElseGet(() -> Validation.invalid(io.vavr.collection.List.of("Card not found with ID: " + id)));
	}
	
}
