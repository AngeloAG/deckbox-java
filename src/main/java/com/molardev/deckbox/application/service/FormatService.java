package com.molardev.deckbox.application.service;

import com.molardev.deckbox.application.common.commands.AddRuleToFormatCommand;
import com.molardev.deckbox.application.common.commands.CreateFormatCommand;
import com.molardev.deckbox.application.common.commands.DeleteFormatCommand;
import com.molardev.deckbox.application.common.commands.RemoveRuleFromFormatCommand;
import com.molardev.deckbox.application.common.interfaces.IFormatRepository;
import com.molardev.deckbox.domain.entity.Format;
import com.molardev.deckbox.domain.valueobject.FormatReference;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public class FormatService {
	// private final IFormatRepository deckFormatRepository;

	// public FormatService(IFormatRepository deckFormatRepository) {
	// 	this.deckFormatRepository = deckFormatRepository;
	// }

	// public Validation<Seq<String>, Format> createDeckFormat(CreateFormatCommand command) {
	// 	return FormatReference.create(command.name(), command.description())
	// 		.flatMap(formatRef -> Format.create(formatRef, List.of()))
	// 		.flatMap(deckFormatRepository::save);
	// }	

	// public Validation<Seq<String>, Format> addRuleToFormat(AddRuleToFormatCommand command) {
	// 	return deckFormatRepository.findById(command.id()).flatMap(format -> 
	// 		deckFormatRepository.save(format.addRule(command.rule()))
	// 	);
	// }

	// public Validation<Seq<String>, Format> removeRuleFromFormat(RemoveRuleFromFormatCommand command) {
	// 	return deckFormatRepository.findById(command.id()).flatMap(format -> 
	// 		deckFormatRepository.save(format.removeRule(command.rule()))
	// 	);
	// }

	// public Validation<Seq<String>, Seq<FormatReference>> getAllFormatReferences() {
	// 	return deckFormatRepository.findAllFormats();
	// }

	// public Validation<Seq<String>, Void> deleteFormatById(DeleteFormatCommand command) {
	// 	return deckFormatRepository.deleteById(command.id());
	// }
}
