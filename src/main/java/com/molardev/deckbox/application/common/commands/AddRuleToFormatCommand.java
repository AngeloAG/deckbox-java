package com.molardev.deckbox.application.common.commands;

import java.util.UUID;

import com.molardev.deckbox.domain.service.IDeckValidationRule;

public record AddRuleToFormatCommand(UUID id, IDeckValidationRule rule) {
	
}
