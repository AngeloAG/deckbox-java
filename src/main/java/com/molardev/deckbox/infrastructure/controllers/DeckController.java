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
import com.molardev.deckbox.domain.errors.CustomError;
import com.molardev.deckbox.infrastructure.controllers.dtos.CardEntryDto;
import com.molardev.deckbox.infrastructure.controllers.dtos.CreateDeckRequest;
import com.molardev.deckbox.infrastructure.controllers.translations.DeckTranslator;

import java.util.Map;
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
		return deckService.createDeck(new CreateDeckCommand(entity.name))
				.fold(
						error -> switch (error) {
							case CustomError.ValidationError(var errors) -> ResponseEntity.badRequest().body(Map.of("error", errors.asJava()));
							case CustomError.RepositoryError(var message, var e) -> 
                    ResponseEntity.internalServerError().body(Map.of("error", "An error occurred. Try again later"));
              case CustomError.RehydrationError(var errors) -> 
                    ResponseEntity.internalServerError().body(Map.of("error", "An error occurred. Try again later"));
						},
						deck -> ResponseEntity.ok(DeckTranslator.toDto(deck))
				);
	}
	
	@GetMapping("/{deckId}")
	public ResponseEntity<Object> getDeck(@PathVariable UUID deckId) {
		return deckService.getDeckById(new GetDeckByIdCommand(deckId))
				.fold(
						error -> switch (error) {
              case CustomError.ValidationError(var errors) -> ResponseEntity.badRequest().body(Map.of("error", errors.asJava()));
							case CustomError.RepositoryError(var message, var e) -> 
                    ResponseEntity.internalServerError().body(Map.of("error", "An error occurred. Try again later"));
              case CustomError.RehydrationError(var errors) -> 
                    ResponseEntity.internalServerError().body(Map.of("error", "An error occurred. Try again later"));
            },
						deckOption -> {
              return deckOption.isEmpty() ? ResponseEntity.notFound().build() 
                : ResponseEntity.ok(DeckTranslator.toDto(deckOption.get()));
            });
	}

	@GetMapping("/{deckId}/cards")
	public ResponseEntity<Object> getDeckCards(@PathVariable UUID deckId) {
		return deckService.getDeckByIdWithCardEntries(new GetDeckByIdCommand(deckId))
				.fold(
						error -> switch (error) {
              case CustomError.ValidationError(var errors) -> ResponseEntity.badRequest().body(Map.of("error", errors.asJava()));
							case CustomError.RepositoryError(var message, var e) -> 
                    ResponseEntity.internalServerError().body(Map.of("error", "An error occurred. Try again later"));
              case CustomError.RehydrationError(var errors) -> 
                    ResponseEntity.internalServerError().body(Map.of("error", "An error occurred. Try again later"));
            },
						deckOption -> deckOption.isEmpty() ? ResponseEntity.notFound().build() 
                : ResponseEntity.ok(DeckTranslator.toDto(deckOption.get()))
				);
	}

	@GetMapping()
	public ResponseEntity<Object> getAllDecks() {
		return deckService.getAllDeckReferences()
				.fold(
						error -> switch (error) {
              case CustomError.ValidationError(var errors) -> ResponseEntity.badRequest().body(Map.of("error", errors.asJava()));
							case CustomError.RepositoryError(var message, var e) -> 
                    ResponseEntity.internalServerError().body(Map.of("error", "An error occurred. Try again later"));
              case CustomError.RehydrationError(var errors) -> 
                    ResponseEntity.internalServerError().body(Map.of("error", "An error occurred. Try again later"));
            },
						decksReferences -> ResponseEntity.ok(decksReferences
								.map(DeckTranslator::toDto).asJava()
						)
				);
	}
	
	@DeleteMapping("/{deckId}")
	public ResponseEntity<Object> deleteDeck(@PathVariable UUID deckId) {
		return deckService.deleteDeckById(new DeleteDeckCommand(deckId))
				.fold(
						error -> switch (error) {
              case CustomError.ValidationError(var errors) -> ResponseEntity.badRequest().body(Map.of("error", errors.asJava()));
							case CustomError.RepositoryError(var message, var e) -> 
                    ResponseEntity.internalServerError().body(Map.of("error", "An error occurred. Try again later"));
              case CustomError.RehydrationError(var errors) -> 
                    ResponseEntity.internalServerError().body(Map.of("error", "An error occurred. Try again later"));
            },
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
					error -> switch (error) {
              case CustomError.ValidationError(var errors) -> ResponseEntity.badRequest().body(Map.of("error", errors.asJava()));
							case CustomError.RepositoryError(var message, var e) -> 
                    ResponseEntity.internalServerError().body(Map.of("error", "An error occurred. Try again later"));
              case CustomError.RehydrationError(var errors) -> 
                    ResponseEntity.internalServerError().body(Map.of("error", "An error occurred. Try again later"));
            },
					updatedDeckOption -> updatedDeckOption.isEmpty() ? ResponseEntity.notFound().build()
            : ResponseEntity.ok(DeckTranslator.toDto(updatedDeckOption.get()))
				)
		);
	}

	@DeleteMapping("/{deckId}/cards/{cardId}")
	public ResponseEntity<Object> removeCard(@PathVariable UUID deckId, @PathVariable String cardId) {
		return deckService.removeCardFromDeck(new RemoveCardFromDeckCommand(deckId, cardId))
			.fold(
				error -> switch (error) {
              case CustomError.ValidationError(var errors) -> ResponseEntity.badRequest().body(Map.of("error", errors.asJava()));
							case CustomError.RepositoryError(var message, var e) -> 
                    ResponseEntity.internalServerError().body(Map.of("error", "An error occurred. Try again later"));
              case CustomError.RehydrationError(var errors) -> 
                    ResponseEntity.internalServerError().body(Map.of("error", "An error occurred. Try again later"));
            },
				deckOption -> deckOption.isEmpty() ? ResponseEntity.notFound().build() 
                : ResponseEntity.ok(DeckTranslator.toDto(deckOption.get()))
			);
	}

	@PutMapping("/{deckId}/cards/{cardId}")
	public ResponseEntity<Object> updateCard(@PathVariable UUID deckId, @PathVariable String cardId, @RequestBody int count) {
		return deckService.updateCardCountInDeck(new UpdateCardCountCommand(deckId, cardId, count))
			.fold(
				error -> switch (error) {
              case CustomError.ValidationError(var errors) -> ResponseEntity.badRequest().body(Map.of("error", errors.asJava()));
							case CustomError.RepositoryError(var message, var e) -> 
                    ResponseEntity.internalServerError().body(Map.of("error", "An error occurred. Try again later"));
              case CustomError.RehydrationError(var errors) -> 
                    ResponseEntity.internalServerError().body(Map.of("error", "An error occurred. Try again later"));
            },
				deckOption -> deckOption.isEmpty() ? ResponseEntity.notFound().build() 
                : ResponseEntity.ok(DeckTranslator.toDto(deckOption.get()))
			);
	}
}
