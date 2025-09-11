package com.molardev.deckbox.application.service;

import com.molardev.deckbox.application.common.commands.AddCardToDeckCommand;
import com.molardev.deckbox.application.common.commands.CreateDeckCommand;
import com.molardev.deckbox.application.common.commands.GetDeckByIdCommand;
import com.molardev.deckbox.application.common.commands.RemoveCardFromDeckCommand;
import com.molardev.deckbox.application.common.commands.UpdateCardCountCommand;
import com.molardev.deckbox.application.common.interfaces.IDeckRepository;
import com.molardev.deckbox.domain.entity.Deck;
import com.molardev.deckbox.domain.valueobject.CardCount;
import com.molardev.deckbox.domain.valueobject.DeckName;
import com.molardev.deckbox.domain.valueobject.DeckReference;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;

import org.springframework.stereotype.Service;

@Service
public class DeckService {
    private final IDeckRepository deckRepository;

    public DeckService(IDeckRepository deckRepository) {
        this.deckRepository = deckRepository;
    }

    public Validation<Seq<String>, Deck> createDeck(CreateDeckCommand command) {
			return DeckName.create(command.name())
				.flatMap(DeckReference::create)
        .flatMap(deckRef -> Deck.create(deckRef, List.of()))
				.flatMap(deckRepository::save);
		}

		public Validation<Seq<String>, Deck> addCardToDeck(AddCardToDeckCommand command) {
			return deckRepository.findById(command.deckId()).flatMap(deck -> 
				deckRepository.save(deck.addCard(command.cardEntry()))
			);
		}

		public Validation<Seq<String>, Deck> removeCardFromDeck(RemoveCardFromDeckCommand command) {
			return deckRepository.findById(command.deckId()).flatMap(deck -> 
				deck.findCard(command.cardId())
					.map(cardEntry -> deckRepository.save(deck.removeCard(cardEntry)))
					.getOrElse(Validation.invalid(io.vavr.collection.List.of("Card not found in deck")))
			);
		}

		public Validation<Seq<String>, Deck> updateCardCountInDeck(UpdateCardCountCommand command) {
			return CardCount.create(command.count())
				.flatMap(count -> deckRepository.findById(command.deckId())
					.flatMap(deck -> deck.findCard(command.cardId())
						.map(oldEntry -> deckRepository.save(deck.updateCard(oldEntry, oldEntry.withCount(count))))
						.getOrElse(Validation.invalid(io.vavr.collection.List.of("Card not found in deck")))));
		}

		public Validation<Seq<String>, Deck> getDeckById(GetDeckByIdCommand command) {
			return deckRepository.findById(command.id());
		}

		public Validation<Seq<String>, Seq<DeckReference>> getAllDeckReferences() {
			return deckRepository.findAllDeckReferences();
		}
}
