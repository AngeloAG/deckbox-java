package com.molardev.deckbox.infrastructure.persistence;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.molardev.deckbox.application.common.interfaces.IFormatRepository;
import com.molardev.deckbox.domain.entity.Format;
import com.molardev.deckbox.domain.valueobject.FormatReference;
import com.molardev.deckbox.infrastructure.persistence.jpa.FormatJpaRepository;
import com.molardev.deckbox.infrastructure.persistence.jpa.RuleJpaRepository;
import com.molardev.deckbox.infrastructure.persistence.translations.RuleTranslator;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;

@Repository
public class JpaFormatRepository implements IFormatRepository {
  private final FormatJpaRepository formatRepository;
  private final RuleJpaRepository ruleRepository;

  public JpaFormatRepository(FormatJpaRepository formatRepo, RuleJpaRepository ruleRepo) {
    this.formatRepository = formatRepo;
    this.ruleRepository = ruleRepo;
  }

  @Override
  public Validation<Seq<String>, Format> save(Format deckFormat) {
    var formatEntity = RuleTranslator.toEntity(deckFormat);
    var saved = formatRepository.save(formatEntity);
    return Validation.valid(RuleTranslator.rehydrateFormat(saved));
  }

  @Override
  public Validation<Seq<String>, Format> findById(UUID id) {
    return formatRepository.findById(id)
      .<Validation<Seq<String>, Format>>map(format -> Validation.valid(RuleTranslator.rehydrateFormat(format)))
      .orElseGet(() -> Validation.invalid(io.vavr.collection.List.of("Format not found")));
  }

  @Override
  public Validation<Seq<String>, Seq<FormatReference>> findAllFormats() {
    var formatEntities = formatRepository.findAll();
    var formatReferences = io.vavr.collection.List.ofAll(formatEntities)
      .map(RuleTranslator::rehydrateFormat)
      .map(Format::getFormatReference);
    return Validation.valid(formatReferences);
  }

  @Override
  public Validation<Seq<String>, Void> deleteById(UUID id) {
    return formatRepository.findById(id).map(format -> {
      formatRepository.deleteById(id);
      return Validation.<Seq<String>, Void>valid(null);
    }).orElseGet(() -> Validation.invalid(io.vavr.collection.List.of("Format not found with id " + id)));
  }
}
