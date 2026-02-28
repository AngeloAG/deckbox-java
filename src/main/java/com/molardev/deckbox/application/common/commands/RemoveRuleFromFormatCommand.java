package com.molardev.deckbox.application.common.commands;

import java.util.UUID;

public record RemoveRuleFromFormatCommand(UUID id, UUID rule) {}
