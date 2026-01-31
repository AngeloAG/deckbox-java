package com.molardev.deckbox.infrastructure.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.molardev.deckbox.application.common.commands.AddRuleToFormatCommand;
import com.molardev.deckbox.application.common.commands.CreateFormatCommand;
import com.molardev.deckbox.application.common.commands.GetFormatByIdCommand;
import com.molardev.deckbox.application.service.FormatService;
import com.molardev.deckbox.infrastructure.controllers.dtos.AddRuleToFormatRequest;
import com.molardev.deckbox.infrastructure.controllers.dtos.CreateFormatRequest;
import com.molardev.deckbox.infrastructure.controllers.dtos.FormatDto;
import com.molardev.deckbox.infrastructure.controllers.translations.RuleTranslator;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;





@RestController
@RequestMapping("/formats")
public class FormatController {
  private FormatService formatService;

  public FormatController(FormatService formatService) {
    this.formatService = formatService;
  }
  
  @PostMapping()
  public ResponseEntity<Object> createFormat(@RequestBody CreateFormatRequest request) {
      return formatService.createDeckFormat(new CreateFormatCommand(request.getName(), request.getDescription()))
        .fold(errors -> ResponseEntity.badRequest().body(errors.asJava()), 
              format -> ResponseEntity.ok(
                new FormatDto(
                  format.getFormatReference().getId().toString(),
                  format.getFormatReference().getName(),
                  format.getFormatReference().getDescription(),
                  format.getRules().map(rule -> RuleTranslator.toDto(rule)).asJava()
                )
              ));
  }
  
  @GetMapping("/{formatId}")
  public ResponseEntity<Object> getFormat(@PathVariable UUID formatId) {
      return formatService.getFormatById(new GetFormatByIdCommand(formatId))
        .fold(errors -> ResponseEntity.badRequest().body(errors.asJava()),
              format -> ResponseEntity.ok(new FormatDto(format.getFormatReference().getId().toString(),
                                                          format.getFormatReference().getName(),
                                                          format.getFormatReference().getDescription(),
                                                          new ArrayList<>())));
  }
  
  @PostMapping("/{formatId}/rules")
  public ResponseEntity<Object> addRuleToFormat(@PathVariable UUID formatId, @RequestBody AddRuleToFormatRequest request) {
       return RuleTranslator.toValidationRule(request.rule())
        .flatMap(validationRule -> formatService.addRuleToFormat(new AddRuleToFormatCommand(formatId, validationRule)))
        .fold(errors -> ResponseEntity.badRequest().body(errors.asJava()), 
              format -> ResponseEntity.ok(new FormatDto(format.getFormatReference().getId().toString(), 
                                                        format.getFormatReference().getName(),
                                                        format.getFormatReference().getDescription(), 
                                                        format.getRules().map(RuleTranslator::toDto).asJava())));
  }
}
