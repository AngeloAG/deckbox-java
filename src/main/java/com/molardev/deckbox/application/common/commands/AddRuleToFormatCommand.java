package com.molardev.deckbox.application.common.commands;

import java.util.Map;
import java.util.UUID;

public record AddRuleToFormatCommand(UUID id, String ruleType, Map<String, String> params) {
	
}
