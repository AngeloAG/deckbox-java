package com.molardev.deckbox.infrastructure.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.molardev.deckbox.application.common.interfaces.ICardRepository;
import com.molardev.deckbox.domain.errors.CustomError;
import com.molardev.deckbox.domain.valueobject.Card;
import com.molardev.deckbox.infrastructure.persistence.entity.CardEntity;
import com.molardev.deckbox.infrastructure.persistence.jpa.CardJpaRepository;
import com.molardev.deckbox.infrastructure.persistence.translations.DeckTranslator;

import io.vavr.collection.List;
import io.vavr.control.Either;
import io.vavr.control.Option;

@Repository
public class JpaCardRepository implements ICardRepository {
	private final CardJpaRepository cardJpaRepository;

	public JpaCardRepository(CardJpaRepository cardJpaRepository) {
		this.cardJpaRepository = cardJpaRepository;
	}

	@Override
	public Either<CustomError, Card> save(Card card) {
		CardEntity entity = DeckTranslator.toEntity(card);
		CardEntity saved = cardJpaRepository.save(entity);
		return DeckTranslator.toDomain(saved)
      .toEither()
      .mapLeft(errors -> (CustomError) new CustomError.RehydrationError(errors));
	}

	@Override
	public Either<CustomError, Option<Card>> findById(String id) {
		try {
      Option<CardEntity> cardOption = Option.ofOptional(cardJpaRepository.findById(id));

      if(cardOption.isEmpty()) {
        return Either.right(Option.none());
      }

      return DeckTranslator.toDomain(cardOption.get())
        .toEither()
        .mapLeft(errors -> (CustomError) new CustomError.RehydrationError(errors))
        .map(Option::some);
    }	catch (IllegalArgumentException e) {
      return Either.left(new CustomError.RepositoryError("Error retrieving card, ID is null", e));
    }
	}

	@Override
	public Either<CustomError, Void> deleteById(String id) {
    try {
      Optional<CardEntity> cardOptional = cardJpaRepository.findById(id);
      if(cardOptional.isEmpty()) {
        return Either.left((CustomError) new CustomError.ValidationError(List.of("Card not found")));
      }

      cardJpaRepository.deleteById(id);
      return Either.right(null);
    } catch (IllegalArgumentException e) {
      return Either.left((CustomError) new CustomError.RepositoryError("An error ocurred while deleting the deck", e));
    }
	}
}
