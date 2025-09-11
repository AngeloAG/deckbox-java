package com.molardev.deckbox.application.service;

import com.molardev.deckbox.application.common.commands.CreateFormatCommand;
import com.molardev.deckbox.application.common.interfaces.IDeckFormatRepository;
import com.molardev.deckbox.domain.entity.DeckFormat;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public class DeckFormatService {
	private final IDeckFormatRepository deckFormatRepository;

	public DeckFormatService(IDeckFormatRepository deckFormatRepository) {
		this.deckFormatRepository = deckFormatRepository;
	}

	public Validation<Seq<String>, DeckFormat> createDeckFormat(CreateFormatCommand command) {
		return DeckFormat.create(command.name(), command.description(), List.of())
			.flatMap(deckFormatRepository::save);
	}

	
}
