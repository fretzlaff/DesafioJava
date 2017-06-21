package br.desafio.controller.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.desafio.model.Contact;
import br.desafio.repos.ContactsRepos;

@Component
public class ContactValidator implements Validator {

	@Autowired
	private ContactsRepos contactRepos;

	@Override
	public boolean supports(final Class<?> clazz) {
		return Contact.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		final Contact contact = (Contact) target;
		if (contact.getId() == null || contact.getId() == 0) {
			if (contactRepos.findByName(contact.getName()) != null) {
				errors.rejectValue("name", "field.unique", null, "Já existe contato com nome \"" + contact.getName() + "\"");
			}
			if (contactRepos.findByEmail(contact.getEmail()) != null) {
				errors.rejectValue("email", "field.unique", null, "Já existe contato com email \"" + contact.getEmail() + "\"");
			}
		}
	}

}
