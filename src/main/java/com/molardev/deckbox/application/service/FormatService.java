package com.molardev.deckbox.application.service;

import org.springframework.stereotype.Service;

import com.molardev.deckbox.application.common.commands.AddRuleToFormatCommand;
import com.molardev.deckbox.application.common.commands.CreateFormatCommand;
import com.molardev.deckbox.application.common.commands.DeleteFormatCommand;
import com.molardev.deckbox.application.common.commands.GetFormatByIdCommand;
import com.molardev.deckbox.application.common.commands.RemoveRuleFromFormatCommand;
import com.molardev.deckbox.application.common.commands.UpdateRuleFromFormatCommand;
import com.molardev.deckbox.application.common.interfaces.IFormatRepository;
import com.molardev.deckbox.application.common.interfaces.IRuleRepository;
import com.molardev.deckbox.domain.entity.Format;
import com.molardev.deckbox.domain.errors.CustomError;
import com.molardev.deckbox.domain.service.RuleGenerator;
import com.molardev.deckbox.domain.valueobject.FormatReference;
import com.molardev.deckbox.infrastructure.persistence.jpa.RuleJpaRepository;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Option;

@Service
public class FormatService {

  private final IFormatRepository deckFormatRepository;
  private final IRuleRepository ruleRepository;

	public FormatService(IFormatRepository deckFormatRepository, IRuleRepository ruleRepository, RuleJpaRepository ruleJpaRepository) {
		this.deckFormatRepository = deckFormatRepository;
    this.ruleRepository = ruleRepository;
	}

	public Either<CustomError, Format> createDeckFormat(CreateFormatCommand command) {
		return Format.create(command.name(), command.description())
      .toEither()
      .mapLeft(errors -> (CustomError) new CustomError.ValidationError(errors))
			.flatMap(deckFormatRepository::save);
	}	

	public Either<CustomError, Option<Format>> addRuleToFormat(AddRuleToFormatCommand command) {
		return deckFormatRepository.findById(command.id())
      .flatMap(formatOption -> formatOption
          .map(format -> RuleGenerator.generate(command.ruleType(), command.params())
            .toEither()
            .mapLeft(errors -> (CustomError) new CustomError.ValidationError(errors))
            .flatMap(newRule -> ruleRepository.save(newRule)
              .flatMap(savedRule -> deckFormatRepository.save(format.updateRule(savedRule.getId(), savedRule)).map(Option::some))))
          .getOrElse(() -> Either.right(Option.none())));
	}

  public Either<CustomError, Option<Format>> updateRuleFromFormat(UpdateRuleFromFormatCommand command) {
    return deckFormatRepository.findById(command.formatId())
      .flatMap(formatOption -> formatOption
        .map(format -> ruleRepository.findById(command.ruleId())
          .flatMap(ruleOption -> {
            if(ruleOption.isDefined()) {
              return deckFormatRepository.save(format.updateRule(ruleOption.get().getId(), command.rule())).map(Option::some);
            }

            return Either.right(Option.some(format));
          }))
          .getOrElse(() -> Either.right(Option.none()))
        );
  }

	public Either<CustomError, Option<Format>> removeRuleFromFormat(RemoveRuleFromFormatCommand command) {
		return deckFormatRepository.findById(command.id())
      .flatMap(formatOption -> formatOption
        .map(format -> ruleRepository.findById(command.rule())
          .flatMap(ruleOption -> {
            if(ruleOption.isDefined()) {
              return deckFormatRepository.save(format.removeRule(ruleOption.get().getId())).map(Option::some);
            }
            return Either.right(Option.some(format));
          }))
          .getOrElse(() -> Either.right(Option.none()))
        );
	}

	public Either<CustomError, Option<Format>> getFormatById(GetFormatByIdCommand command) {
		return deckFormatRepository.findById(command.id());
	}

  public Either<CustomError, Option<Format>> getFormatWithRules(GetFormatByIdCommand command) {
    return deckFormatRepository.findByIdWithRules(command.id());
  }

	public Either<CustomError, Seq<FormatReference>> getAllFormatReferences() {
		return deckFormatRepository.findAllFormats();
	}

	public Either<CustomError, Void> deleteFormatById(DeleteFormatCommand command) {
		return deckFormatRepository.deleteById(command.id());
	}
}
