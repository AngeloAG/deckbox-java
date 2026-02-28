package com.molardev.deckbox.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.molardev.deckbox.application.common.interfaces.IFormatRepository;
import com.molardev.deckbox.domain.entity.Format;
import com.molardev.deckbox.domain.errors.CustomError;
import com.molardev.deckbox.domain.valueobject.FormatReference;
import com.molardev.deckbox.infrastructure.persistence.entity.FormatEntity;
import com.molardev.deckbox.infrastructure.persistence.jpa.FormatJpaRepository;
import com.molardev.deckbox.infrastructure.persistence.translations.RuleTranslator;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Option;

@Repository
public class JpaFormatRepository implements IFormatRepository {
  private final FormatJpaRepository formatRepository;

  public JpaFormatRepository(FormatJpaRepository formatRepo) {
    this.formatRepository = formatRepo;
  }

  @Override
  public Either<CustomError, Format> save(Format deckFormat) {
    var formatEntity = RuleTranslator.toEntity(deckFormat);
    var saved = formatRepository.save(formatEntity);
    return Either.right(RuleTranslator.rehydrateFormat(saved));
  }

  @Override
  public Either<CustomError, Option<Format>> findById(UUID id) {
    try {
      return formatRepository.findById(id)
        .<Either<CustomError, Option<Format>>>map(format -> Either.right(Option.some(RuleTranslator.rehydrateFormat(format))))
        .orElseGet(() -> Either.right(Option.none()));
    } catch (IllegalArgumentException e) {
      return Either.left((CustomError) new CustomError.RepositoryError("Failed to retrieve format. Id is null", e));
    }
  }

  @Override
  public Either<CustomError, Seq<FormatReference>> findAllFormats() {
    var formatEntities = formatRepository.findAll();
    var formatReferences = io.vavr.collection.List.ofAll(formatEntities)
      .map(RuleTranslator::rehydrateFormat)
      .map(Format::getFormatReference);
    return Either.right(formatReferences);
  }

  @Override
  public Either<CustomError, Void> deleteById(UUID id) {
    try {
      Optional<FormatEntity> formatOptional = formatRepository.findById(id);
      if(formatOptional.isEmpty()) {
        return Either.left((CustomError) new CustomError.ValidationError(List.of("Format id not found")));
      }

      formatRepository.deleteById(id);
      return Either.right(null);
    } catch (Exception e) {
      return Either.left((CustomError) new CustomError.RepositoryError("Failed to delete format", e));
    }
  }

  @Override
  public Either<CustomError, Option<Format>> findByIdWithRules(UUID id) {
    try {
      Optional<FormatEntity> formatOptional = formatRepository.findByIdWithRules(id);
      if(formatOptional.isEmpty()) {
        return Either.right(Option.none());
      }
      return Either.right(Option.some(RuleTranslator.rehydrateFormat(formatOptional.get())));

    } catch (Exception e) {
      return Either.left((CustomError) new CustomError.RepositoryError("Failed to retrieve format. Id is null", e));
    }
  }
}
