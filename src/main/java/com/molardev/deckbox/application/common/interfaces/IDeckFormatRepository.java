package com.molardev.deckbox.application.common.interfaces;

import java.util.UUID;

import com.molardev.deckbox.domain.entity.DeckFormat;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public interface IDeckFormatRepository {
	public Validation<Seq<String>, DeckFormat> save(DeckFormat deckFormat);
	public Validation<Seq<String>, DeckFormat> findById(UUID id);
	public Validation<Seq<String>, 
} 
