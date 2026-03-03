package com.molardev.deckbox.infrastructure.controllers.dtos;

import java.util.Map;
import java.util.UUID;

public record AddRuleToFormatRequest(UUID formatId, String ruleType, Map<String, String> params) {}
