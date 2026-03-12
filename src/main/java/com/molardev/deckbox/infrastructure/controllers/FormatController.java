package com.molardev.deckbox.infrastructure.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.molardev.deckbox.application.common.commands.AddRuleToFormatCommand;
import com.molardev.deckbox.application.common.commands.CreateFormatCommand;
import com.molardev.deckbox.application.common.commands.DeleteFormatCommand;
import com.molardev.deckbox.application.common.commands.GetFormatByIdCommand;
import com.molardev.deckbox.application.common.commands.RemoveRuleFromFormatCommand;
import com.molardev.deckbox.application.common.commands.UpdateRuleFromFormatCommand;
import com.molardev.deckbox.application.service.FormatService;
import com.molardev.deckbox.domain.errors.CustomError;
import com.molardev.deckbox.domain.service.IDeckValidationRule;
import com.molardev.deckbox.infrastructure.controllers.dtos.AddRuleToFormatRequest;
import com.molardev.deckbox.infrastructure.controllers.dtos.CreateFormatRequest;
import com.molardev.deckbox.infrastructure.controllers.dtos.UpdateFormatRuleRequest;
import com.molardev.deckbox.infrastructure.controllers.translations.RuleTranslator;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/formats")
public class FormatController {
  private FormatService formatService;

  public FormatController(FormatService formatService) {
    this.formatService = formatService;
  }

  @PostMapping()
  public ResponseEntity<?> createFormat(@RequestBody CreateFormatRequest request) {
    return formatService.createDeckFormat(new CreateFormatCommand(request.getName(), request.getDescription()))
        .fold(error -> mapErrors(error),
            format -> ResponseEntity.ok(
                RuleTranslator.toDto(format)));
  }

  @GetMapping("/{formatId}")
  public ResponseEntity<?> getFormat(@PathVariable UUID formatId) {
    return formatService.getFormatById(new GetFormatByIdCommand(formatId))
        .fold(error -> mapErrors(error),
            formatOption -> formatOption.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(RuleTranslator.toDto(formatOption.get())));
  }

  @GetMapping("/{formatId}/rules")
  public ResponseEntity<?> getFormatRules(@PathVariable UUID formatId) {
    return formatService.getFormatWithRules(new GetFormatByIdCommand(formatId))
        .fold(
            error -> mapErrors(error),
            formatOption -> formatOption.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(RuleTranslator.toDto(formatOption.get())));
  }

  @GetMapping("/")
  public ResponseEntity<?> getAllFormats() {
    return formatService.getAllFormatReferences()
        .fold(
            error -> mapErrors(error),
            formats -> ResponseEntity.ok(formats.map(format -> RuleTranslator.toDto(format))));
  }

  @DeleteMapping("/{formatId}")
  public ResponseEntity<?> deleteFormat(@PathVariable UUID formatId) {
    return formatService.deleteFormatById(new DeleteFormatCommand(formatId))
        .fold(error -> mapErrors(error),
            v -> ResponseEntity.noContent().build());
  }

  @PostMapping("/{formatId}/rules")
  public ResponseEntity<?> addRuleToFormat(@PathVariable UUID formatId, @RequestBody AddRuleToFormatRequest request) {
    return formatService.addRuleToFormat(new AddRuleToFormatCommand(formatId, request.ruleType(), request.params()))
        .fold(error -> mapErrors(error),
            formatOption -> formatOption.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(RuleTranslator.toDto(formatOption.get())));
  }

  @DeleteMapping("/{formatId}/rules/{ruleId}")
  public ResponseEntity<?> deleteRuleFromFormat(@PathVariable UUID formatId, @PathVariable UUID ruleId) {
    return formatService.removeRuleFromFormat(new RemoveRuleFromFormatCommand(formatId, ruleId))
        .fold(error -> mapErrors(error),
            formatOption -> formatOption.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(RuleTranslator.toDto(formatOption.get())));
  }

  @PutMapping("/{formatId}/rules/{ruleId}")
  public ResponseEntity<?> updateRuleInFormat(@PathVariable UUID formatId, @PathVariable UUID ruleId,
      @RequestBody UpdateFormatRuleRequest request) {
    return RuleTranslator.toValidationRule(request.rule())
        .fold(
            errors -> ResponseEntity.badRequest().body(Map.of("error", errors.asJava())),
            newRule -> formatService
                .updateRuleFromFormat(new UpdateRuleFromFormatCommand(formatId, ruleId, (IDeckValidationRule) newRule))
                .fold(
                    error -> mapErrors(error),
                    formatOption -> formatOption.isEmpty()
                        ? ResponseEntity.notFound().build()
                        : ResponseEntity.ok(RuleTranslator.toDto(formatOption.get()))));
  }

  private ResponseEntity<Object> mapErrors(CustomError error) {
    return switch (error) {
      case CustomError.ValidationError(var errors) ->
        ResponseEntity.badRequest().body(Map.of("error", errors.asJava()));
      case CustomError.RepositoryError(var message, var e) ->
        ResponseEntity.internalServerError().body(Map.of("error", "An error occurred. Try again later"));
      case CustomError.RehydrationError(var errors) ->
        ResponseEntity.internalServerError().body(Map.of("error", "An error occurred. Try again later"));
      case CustomError.NotFoundError(var message) -> ResponseEntity.notFound().build();
      case CustomError.ConflictError() -> ResponseEntity.status(HttpStatus.CONFLICT).build();
      case CustomError.NoUserError() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    };
  }
}
