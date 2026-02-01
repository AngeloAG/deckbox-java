package com.molardev.deckbox.application.service;

import com.molardev.deckbox.application.common.commands.AddCardToDeckCommand;
import com.molardev.deckbox.application.common.commands.CreateDeckCommand;
import com.molardev.deckbox.application.common.commands.DeleteDeckCommand;
import com.molardev.deckbox.application.common.commands.GetDeckByIdCommand;
import com.molardev.deckbox.application.common.commands.RemoveCardFromDeckCommand;
import com.molardev.deckbox.application.common.commands.UpdateCardCountCommand;
import com.molardev.deckbox.application.common.interfaces.ICardRepository;
import com.molardev.deckbox.application.common.interfaces.IDeckRepository;
import com.molardev.deckbox.domain.entity.Deck;
import com.molardev.deckbox.domain.errors.CustomError;
import com.molardev.deckbox.domain.valueobject.Card;
import com.molardev.deckbox.domain.valueobject.CardCount;
import com.molardev.deckbox.domain.valueobject.CardEntry;
import com.molardev.deckbox.domain.valueobject.DeckReference;

import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Option;
import org.springframework.stereotype.Service;

@Service
public class DeckService {
    private final IDeckRepository deckRepository;
		private final ICardRepository cardRepository;
		// private final IFormatRepository formatRepository;

    public DeckService(IDeckRepository deckRepository, ICardRepository cardRepository) { //, IFormatRepository formatRepository) {
        this.deckRepository = deckRepository;
        this.cardRepository = cardRepository;
        // this.formatRepository = formatRepository;
    }

    public Either<CustomError, Deck> createDeck(CreateDeckCommand command) {
			return Deck.create(command.name())
				.toEither()
				.mapLeft(errors -> (CustomError) new CustomError.ValidationError(errors))
				.flatMap(deck -> deckRepository.save(deck));
		}

		public Either<CustomError, Option<Deck>> addCardToDeck(AddCardToDeckCommand command) {
			return deckRepository.findById(command.deckId()).flatMap(deckOption -> {
					Card card = command.cardEntry().getCard();
					CardCount count = command.cardEntry().getCount();
					String cardId = card.getCardReference().getCardId();

          if(deckOption.isEmpty()) {
            return Either.right(Option.none());
          }

					return cardRepository.findById(cardId)
							.flatMap(maybeCard -> {
									if (maybeCard.isDefined()) {
											// Card exists, use the existing card
											Card existingCard = maybeCard.get();
											return deckRepository.save(deckOption.get().addCard(existingCard, count)).map(Option::some);
									} else {
											// Card does not exist, save the new card first
											return cardRepository.save(card)
											  .flatMap(savedCard -> deckRepository.save(deckOption.get().addCard(savedCard, count)).map(Option::some));
									}
							});
			});
		}

		public Either<CustomError, Option<Deck>> removeCardFromDeck(RemoveCardFromDeckCommand command) {
			return deckRepository.findById(command.deckId()).flatMap(deckOption -> 
				{
          if(deckOption.isEmpty()) {
            return Either.right(Option.none());
          }
          Option<CardEntry> cardEntryOption = deckOption.get().findCard(command.cardId());
          
          if(cardEntryOption.isEmpty()) {
            return Either.right(deckOption);
          }

          return deckRepository.save(deckOption.get().removeCard(cardEntryOption.get())).map(Option::some);
        }
			);
		}

		public Either<CustomError, Option<Deck>> updateCardCountInDeck(UpdateCardCountCommand command) {
			return CardCount.create(command.count())
        .toEither()
        .mapLeft(errors -> (CustomError) new CustomError.ValidationError(errors))
				.flatMap(count -> deckRepository.findById(command.deckId())
					.flatMap(deckOption -> {
            if(deckOption.isEmpty()) {
              return Either.right(Option.none());
            }
            Option<CardEntry> cardEntryOption = deckOption.get().findCard(command.cardId());

            if(cardEntryOption.isEmpty()) {
              return Either.right(deckOption);
            }

            return deckRepository.save(deckOption.get().updateCard(cardEntryOption.get(), cardEntryOption.get().withCount(count))).map(Option::some);
          }));
		}

		public Either<CustomError, Option<Deck>> getDeckById(GetDeckByIdCommand command) {
			return deckRepository.findById(command.id());
		}

		public Either<CustomError, Option<Deck>> getDeckByIdWithCardEntries(GetDeckByIdCommand command) {
			return deckRepository.findByIdWithCardEntries(command.id());
		}

		public Either<CustomError, Seq<DeckReference>> getAllDeckReferences() {
			return deckRepository.findAllDeckReferences();
		}
		
		public Either<CustomError, Void> deleteDeckById(DeleteDeckCommand command) {
			return deckRepository.deleteById(command.id());
		}

		// public Validation<Seq<String>, Deck> validateDeckAgainstFormat(ValidateDeckCommand command) {
		// 	return deckRepository.findById(command.deckId())
		// 		.flatMap(deck -> formatRepository.findById(command.formatId())
		// 			.flatMap(format -> {
		// 				DeckValidator validator = new DeckValidator(format.getRules());
		// 				return validator.validate(deck);
		// 			})
		// 		);
		// }
}
