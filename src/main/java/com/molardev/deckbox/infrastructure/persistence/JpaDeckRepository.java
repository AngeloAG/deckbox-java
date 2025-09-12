package com.molardev.deckbox.infrastructure.persistence;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.molardev.deckbox.application.common.interfaces.IDeckRepository;
import com.molardev.deckbox.domain.entity.Deck;
import com.molardev.deckbox.domain.valueobject.DeckReference;
import com.molardev.deckbox.infrastructure.persistence.entity.DeckEntity;
import com.molardev.deckbox.infrastructure.persistence.jpa.DeckJpaRepository;
import com.molardev.deckbox.infrastructure.persistence.translations.DeckTranslator;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;

@Repository
public class JpaDeckRepository implements IDeckRepository {
	private final DeckJpaRepository deckJpaRepository;

	public JpaDeckRepository(DeckJpaRepository deckJpaRepository) {
		this.deckJpaRepository = deckJpaRepository;
	}

	@Override
	public Validation<Seq<String>, Deck> save(Deck deck) {
		DeckEntity entity = DeckTranslator.toEntity(deck);
        DeckEntity saved = deckJpaRepository.save(entity);
        return DeckTranslator.toDomain(saved);
	}

	@Override
	public Validation<Seq<String>, Deck> findById(UUID id) {
		return deckJpaRepository.findById(id)
			.map(DeckTranslator::toDomain)
			.orElseGet(() -> Validation.invalid(io.vavr.collection.List.of("Deck not found with ID: " + id)));
	}

	@Override
	public Validation<Seq<String>, Seq<DeckReference>> findAllDeckReferences() {
    java.util.List<DeckEntity> entities = deckJpaRepository.findAll();
    io.vavr.collection.List<DeckReference> references = io.vavr.collection.List
        .ofAll(entities)
        .map(DeckTranslator::toDomain)
        .filter(Validation::isValid)
        .map(Validation::get)
        .map(Deck::getDeckReference);

    return references.isEmpty()
        ? Validation.invalid(io.vavr.collection.List.of("No decks found"))
        : Validation.valid(references);
}

	@Override
	public Validation<Seq<String>, Void> deleteById(UUID id) {
		return deckJpaRepository.findById(id).map(entity -> {
			deckJpaRepository.deleteById(id);
			return Validation.<Seq<String>, Void>valid(null);
		}).orElseGet(() -> Validation.invalid(io.vavr.collection.List.of("Deck not found with ID: " + id)));
	}
}