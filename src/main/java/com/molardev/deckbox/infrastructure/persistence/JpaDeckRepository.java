package com.molardev.deckbox.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.molardev.deckbox.application.common.interfaces.IDeckRepository;
import com.molardev.deckbox.domain.entity.Deck;
import com.molardev.deckbox.domain.errors.CustomError;
import com.molardev.deckbox.domain.valueobject.DeckReference;
import com.molardev.deckbox.infrastructure.persistence.entity.DeckEntity;
import com.molardev.deckbox.infrastructure.persistence.jpa.DeckJpaRepository;
import com.molardev.deckbox.infrastructure.persistence.translations.DeckTranslator;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Validation;

@Repository
public class JpaDeckRepository implements IDeckRepository {
	private final DeckJpaRepository deckJpaRepository;

	public JpaDeckRepository(DeckJpaRepository deckJpaRepository) {
		this.deckJpaRepository = deckJpaRepository;
	}

	@Override
	public Either<CustomError, Deck> save(Deck deck) {
		DeckEntity entity = DeckTranslator.toEntity(deck);
    DeckEntity saved = deckJpaRepository.save(entity);
    return DeckTranslator.toDomain(saved)
			.toEither()
			.mapLeft(errors -> (CustomError) new CustomError.RehydrationError(errors));
	}

	@Override
	public Either<CustomError, Option<Deck>> findById(UUID id) {
		try {
			Option<DeckEntity> option = Option.ofOptional(deckJpaRepository.findById(id));
      if(option.isEmpty()) {
        return Either.right(Option.none());
      }

      return DeckTranslator.toDomain(option.get())
        .toEither()
        .mapLeft(errors -> (CustomError) new CustomError.RehydrationError(errors))
        .map(Option::some);

		} catch(IllegalArgumentException e) {
			return Either.left(new CustomError.RepositoryError("Error retrieving deck. Id is null", e));
		}
	}

	@Override
	public Either<CustomError, Seq<DeckReference>> findAllDeckReferences() {
    java.util.List<DeckEntity> entities = deckJpaRepository.findAll();
    io.vavr.collection.List<DeckReference> references = io.vavr.collection.List
        .ofAll(entities)
        .map(DeckTranslator::toDomain)
        .filter(Validation::isValid)
        .map(Validation::get)
        .map(Deck::getDeckReference);

    return Either.right(references);
	}

	@Override
	public Either<CustomError, Void> deleteById(UUID id) {
    try {
      Optional<DeckEntity> entityOption = deckJpaRepository.findById(id);
      if(entityOption.isEmpty()) {
        return Either.left(new CustomError.ValidationError(List.of("Deck not found")));
      }

      deckJpaRepository.deleteById(id);
      return Either.right(null);
      
    } catch (IllegalArgumentException e) {
      return Either.left(new CustomError.RepositoryError("An error ocurred while deleting the deck", e));
    }
	}

	@Override
	public Either<CustomError, Option<Deck>> findByIdWithCardEntries(UUID id) {
    try {
      Option<DeckEntity> deckEntityOption = Option.ofOptional(
        deckJpaRepository.findByIdWithCardEntries(id)
      );

      if(deckEntityOption.isEmpty()) {
        return Either.right(Option.none());
      }

      return DeckTranslator.toDomain(deckEntityOption.get())
        .toEither()
        .mapLeft(errors -> (CustomError) new CustomError.RehydrationError(errors))
        .map(Option::some);

    } catch (Exception e) {
      return Either.left(new CustomError.RepositoryError("Failed to find deck by ID", e));
    }
	}
}