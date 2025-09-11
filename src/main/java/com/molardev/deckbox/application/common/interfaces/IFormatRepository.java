package com.molardev.deckbox.application.common.interfaces;

import java.util.UUID;

import com.molardev.deckbox.domain.entity.Format;
import com.molardev.deckbox.domain.valueobject.FormatReference;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public interface IFormatRepository {
	public Validation<Seq<String>, Format> save(Format deckFormat);
	public Validation<Seq<String>, Format> findById(UUID id);
	public Validation<Seq<String>, Seq<FormatReference>> findAllFormats();
	public Validation<Seq<String>, Void> deleteById(UUID id);
} 
