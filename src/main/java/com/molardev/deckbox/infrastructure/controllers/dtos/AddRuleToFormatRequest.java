package com.molardev.deckbox.infrastructure.controllers.dtos;

import java.util.UUID;

public record AddRuleToFormatRequest(UUID formatId, RuleDto rule) {}
