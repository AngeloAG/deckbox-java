package com.molardev.deckbox.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.molardev.deckbox.application.common.interfaces.IRuleRepository;
import com.molardev.deckbox.domain.errors.CustomError;
import com.molardev.deckbox.domain.service.IDeckValidationRule;
import com.molardev.deckbox.infrastructure.persistence.entity.RuleEntity;
import com.molardev.deckbox.infrastructure.persistence.jpa.RuleJpaRepository;
import com.molardev.deckbox.infrastructure.persistence.translations.RuleTranslator;

import io.vavr.collection.List;
import io.vavr.control.Either;
import io.vavr.control.Option;

@Repository
public class JpaRuleRepository implements IRuleRepository {
  private final RuleJpaRepository ruleRepository;

  public JpaRuleRepository(RuleJpaRepository ruleRepository) {
    this.ruleRepository = ruleRepository;
  }

  @Override
  public Either<CustomError, IDeckValidationRule> save(IDeckValidationRule rule) {
    RuleEntity ruleEntity = RuleTranslator.toEntity(rule);
    RuleEntity savedEntity = ruleRepository.save(ruleEntity);
    return Either.right(RuleTranslator.rehydrateRule(savedEntity));
  }

  @Override
  public Either<CustomError, Option<IDeckValidationRule>> findById(UUID id) {
    try {
      return ruleRepository.findById(id)
        .<Either<CustomError, Option<IDeckValidationRule>>>map(ruleEntity -> Either.right(Option.some(RuleTranslator.rehydrateRule(ruleEntity))))
        .orElseGet(() -> Either.right(Option.none()));
    } catch (IllegalArgumentException e) {
      return Either.left((CustomError) new CustomError.RepositoryError("Failed to retrieve rule. Id is null", e));
    }
  }

  @Override
  public Either<CustomError, Void> deleteById(UUID id) {
    try {
      Optional<RuleEntity> ruleOptional = ruleRepository.findById(id);
      if(ruleOptional.isEmpty()) {
        return Either.left((CustomError) new CustomError.ValidationError(List.of("Failed to delete rule. Id not found")));
      }
      ruleRepository.deleteById(id);
      return Either.right(null);
    } catch (Exception e) {
      return Either.left((CustomError) new CustomError.RepositoryError("Failed to delete rule", e));
    }
  }
}
