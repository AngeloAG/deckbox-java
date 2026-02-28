package com.molardev.deckbox.application.common.interfaces;

import java.util.UUID;

import com.molardev.deckbox.domain.entity.Format;
import com.molardev.deckbox.domain.errors.CustomError;
import com.molardev.deckbox.domain.valueobject.FormatReference;

import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Option;

public interface IFormatRepository {
	public Either<CustomError, Format> save(Format deckFormat);
	public Either<CustomError, Option<Format>> findById(UUID id);
  public Either<CustomError, Option<Format>> findByIdWithRules(UUID id);
	public Either<CustomError, Seq<FormatReference>> findAllFormats();
	public Either<CustomError, Void> deleteById(UUID id);
} 
