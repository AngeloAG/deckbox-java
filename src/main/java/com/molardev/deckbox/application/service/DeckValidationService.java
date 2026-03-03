package com.molardev.deckbox.application.service;

import org.springframework.stereotype.Service;

import com.molardev.deckbox.application.common.commands.ValidateDeckCommand;
import com.molardev.deckbox.application.common.interfaces.IDeckRepository;
import com.molardev.deckbox.application.common.interfaces.IFormatRepository;
import com.molardev.deckbox.domain.errors.CustomError;
import com.molardev.deckbox.domain.service.DeckValidator;

import io.vavr.collection.Seq;
import io.vavr.control.Either;

@Service
public class DeckValidationService {
  private final IDeckRepository deckRepository;
  private final IFormatRepository formatRepository;

  public DeckValidationService(IDeckRepository deckRepository, IFormatRepository formatRepository) {
    this.deckRepository = deckRepository;
    this.formatRepository = formatRepository;
  }

  public Either<CustomError, Seq<String>> validateDeckAgainstFormat(ValidateDeckCommand command) {
  	return deckRepository.findById(command.deckId())
        .flatMap(deckOption -> deckOption
            .toEither((CustomError) new CustomError.NotFoundError("Deck not found")))
        .flatMap(deck -> formatRepository.findById(command.formatId())
            .flatMap(formatOption -> formatOption
                .toEither((CustomError) new CustomError.NotFoundError("Format not found")))
            .map(format -> DeckValidator.validate(deck, format)))
            .map(result -> result.isValid() ? io.vavr.collection.List.<String>empty() : result.getError());
  }
}
