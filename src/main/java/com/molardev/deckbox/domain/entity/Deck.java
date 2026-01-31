package com.molardev.deckbox.domain.entity;

import java.util.Objects;
import java.util.UUID;

import com.molardev.deckbox.domain.valueobject.Card;
import com.molardev.deckbox.domain.valueobject.CardCount;
import com.molardev.deckbox.domain.valueobject.CardEntry;
import com.molardev.deckbox.domain.valueobject.DeckName;
import com.molardev.deckbox.domain.valueobject.DeckReference;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public class Deck {
	private final DeckReference deckReference;
	private final List<CardEntry> cardEntries;

	private Deck(DeckReference deckReference, List<CardEntry> cardEntries) {
		this.deckReference = deckReference;
		this.cardEntries = cardEntries;
	}

	public static Validation<Seq<String>, Deck> create(DeckReference deckReference, List<CardEntry> cardEntries) {
		if(deckReference == null) {
			return Validation.invalid(io.vavr.collection.List.of("Deck reference must not be null"));
		}
		if(cardEntries == null) {
			return Validation.invalid(io.vavr.collection.List.of("Card entries must not be null"));
		}

		return Validation.valid(new Deck(deckReference, cardEntries));
	}

	public static Validation<Seq<String>, Deck> create(String name) {
		return DeckName.create(name)
				.flatMap(DeckReference::create)
        .map(deckRef -> new Deck(deckRef, List.of()));
	}

	public DeckReference getDeckReference() {
		return deckReference;
	}

	public List<CardEntry> getCardEntries() {
		return cardEntries;
	}

	// Add a card entry to the deck (returns new Deck)
	public Deck addCard(CardEntry entry) {
		return new Deck(deckReference, cardEntries.append(entry));
	}

	public Deck addCard(Card card, CardCount count) {
		CardEntry entry = CardEntry.create(card, count).get();
		return new Deck(deckReference, cardEntries.append(entry));
	}

	// Remove a card entry from the deck (returns new Deck)
	public Deck removeCard(CardEntry entry) {
		return new Deck(deckReference, cardEntries.remove(entry));
	}

	// Update a card entry in the deck (returns new Deck)
	public Deck updateCard(CardEntry oldEntry, CardEntry newEntry) {
		return new Deck(deckReference, cardEntries.remove(oldEntry).append(newEntry));
	}

	// Find a card entry by card
	public io.vavr.control.Option<CardEntry> findCard(String cardId) {
		return cardEntries.find(entry -> entry.getCard().getCardReference().getCardId().equals(cardId));
	}

	// Check if the deck contains a card
	public boolean contains(String cardId) {
		return cardEntries.exists(entry -> entry.getCard().getCardReference().getCardId().equals(cardId));
	}

	// Get the total number of cards in the deck (sum of counts)
	public int count() {
		return cardEntries.map(entry -> entry.getCount().getValue()).sum().intValue();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Deck deck = (Deck) o;
		return Objects.equals(deckReference, deck.deckReference);
	}

	@Override
	public int hashCode() {
		return Objects.hash(deckReference);
	}

	@Override
	public String toString() {
		return "Deck(" +
				"deckReference=" + deckReference +
				", cardEntries=" + cardEntries +
				')';
	}
}
