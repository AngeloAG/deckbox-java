package com.molardev.deckbox.infrastructure.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.molardev.deckbox.application.common.commands.AddCardToDeckCommand;
import com.molardev.deckbox.application.common.commands.CreateDeckCommand;
import com.molardev.deckbox.application.common.commands.DeleteDeckCommand;
import com.molardev.deckbox.application.common.commands.GetDeckByIdCommand;
import com.molardev.deckbox.application.common.commands.RemoveCardFromDeckCommand;
import com.molardev.deckbox.application.common.commands.UpdateCardCountCommand;
import com.molardev.deckbox.application.service.DeckService;
import com.molardev.deckbox.infrastructure.controllers.dtos.CardEntryDto;
import com.molardev.deckbox.infrastructure.controllers.dtos.CreateDeckRequest;
import com.molardev.deckbox.infrastructure.controllers.dtos.DeckDto;
import com.molardev.deckbox.infrastructure.controllers.translations.DeckTranslator;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/decks")
public class DeckController {
	private final DeckService deckService;

	public DeckController(DeckService deckService) {
		this.deckService = deckService;
	}

	@PostMapping()
	public ResponseEntity<Object> createDeck(@RequestBody CreateDeckRequest entity) {
		return deckService.createDeck(new CreateDeckCommand(entity.getName()))
				.fold(
						errors -> ResponseEntity.badRequest().body(errors.asJava()),
						deck -> ResponseEntity.ok(new DeckDto(
								deck.getDeckReference().getId().toString(),
								deck.getDeckReference().getName().getName(),
								new ArrayList<>()
						))
				);
	}
	
	@GetMapping("/{deckId}")
	public ResponseEntity<Object> getDeck(@PathVariable UUID deckId) {
		return deckService.getDeckById(new GetDeckByIdCommand(deckId))
				.fold(
						errors -> ResponseEntity.badRequest().body(errors.asJava()),
						deck -> ResponseEntity.ok(new DeckDto(
								deck.getDeckReference().getId().toString(),
								deck.getDeckReference().getName().getName(),
								new ArrayList<>()
						))
				);
	}

	@GetMapping("/{deckId}/cards")
	public ResponseEntity<Object> getDeckCards(@PathVariable UUID deckId) {
		return deckService.getDeckByIdWithCardEntries(new GetDeckByIdCommand(deckId))
				.fold(
						errors -> ResponseEntity.badRequest().body(errors.asJava()),
						deckMaybe -> {
							if(deckMaybe.isDefined()) {
								var deck = deckMaybe.get();
								return ResponseEntity.ok(new DeckDto(
									deck.getDeckReference().getId().toString(),
									deck.getDeckReference().getName().getName(),
									deck.getCardEntries().map(DeckTranslator::toDto).asJava()
								));
							} else {
								return ResponseEntity.notFound().build();
							}
						}
				);
	}

	@GetMapping()
	public ResponseEntity<Object> getAllDecks() {
		return deckService.getAllDeckReferences()
				.fold(
						errors -> ResponseEntity.badRequest().body(errors.asJava()),
						decksReferences -> ResponseEntity.ok(decksReferences
								.map(decksReference -> new DeckDto(
										decksReference.getId().toString(),
										decksReference.getName().getName(),
										new ArrayList<>()
								)).asJava()
						)
				);
	}
	
	@DeleteMapping("/{deckId}")
	public ResponseEntity<Object> deleteDeck(@PathVariable UUID deckId) {
		return deckService.deleteDeckById(new DeleteDeckCommand(deckId))
				.fold(
						errors -> ResponseEntity.badRequest().body(errors.asJava()),
						v -> ResponseEntity.noContent().build()
				);
	}

	@PostMapping("/{deckId}/cards")
	public ResponseEntity<Object> addCard(@PathVariable UUID deckId, @RequestBody CardEntryDto cardEntryDto) {
		var cardEntryValidation = DeckTranslator.toDomain(cardEntryDto);
		return cardEntryValidation.fold(
			errors -> ResponseEntity.badRequest().body(errors.asJava()),
			cardEntry -> deckService.addCardToDeck(new AddCardToDeckCommand(deckId, cardEntry))
				.fold(
					serviceErrors -> ResponseEntity.badRequest().body(serviceErrors.asJava()),
					updatedDeck -> ResponseEntity.ok(new DeckDto(
						updatedDeck.getDeckReference().getId().toString(),
						updatedDeck.getDeckReference().getName().getName(),
						updatedDeck.getCardEntries().map(DeckTranslator::toDto).asJava()
					))
				)
		);
	}

	@DeleteMapping("/{deckId}/cards/{cardId}")
	public ResponseEntity<Object> removeCard(@PathVariable UUID deckId, @PathVariable String cardId) {
		return deckService.removeCardFromDeck(new RemoveCardFromDeckCommand(deckId, cardId))
			.fold(
				errors -> ResponseEntity.badRequest().body(errors.asJava()),
				deck -> ResponseEntity.ok(DeckTranslator.toDto(deck))
			);
	}

	@PutMapping("/{deckId}/cards/{cardId}")
	public ResponseEntity<Object> updateCard(@PathVariable UUID deckId, @PathVariable String cardId, @RequestBody int count) {
		return deckService.updateCardCountInDeck(new UpdateCardCountCommand(deckId, cardId, count))
			.fold(
				errors -> ResponseEntity.badRequest().body(errors.asJava()),
				deck -> ResponseEntity.ok(DeckTranslator.toDto(deck))
			);
	}
}
