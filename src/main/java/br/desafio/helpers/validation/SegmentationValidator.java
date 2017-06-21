package br.desafio.helpers.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.desafio.model.Segmentation;
import br.desafio.repos.SegmentationRepos;

@Component
public class SegmentationValidator implements Validator {

	@Autowired
	private SegmentationRepos segmentationRepos;

	@Override
	public boolean supports(final Class<?> clazz) {
		return Segmentation.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		final Segmentation segmentation = (Segmentation) target;
		if (segmentation.getId() == null || segmentation.getId() == 0) {
			if (segmentationRepos.findByDescription(segmentation.getDescription()) != null) {
				errors.rejectValue("description", "field.unique", null, "Já existe segmentação com descrição \"" + segmentation.getDescription() + "\"");
			}
		}
	}

}
