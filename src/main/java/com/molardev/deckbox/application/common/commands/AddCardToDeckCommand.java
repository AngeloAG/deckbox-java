package com.molardev.deckbox.application.common.commands;

import java.util.UUID;

import com.molardev.deckbox.domain.valueobject.CardEntry;

public record AddCardToDeckCommand(UUID deckId, CardEntry cardEntry) {}
