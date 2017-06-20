package br.desafio.controller;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.desafio.model.Contact;
import br.desafio.repos.ContactsRepos;

/**
 * Responsável por receber as requisições REST da view de contatos
 */
@RestController
@RequestMapping("contact")
public class ContactsController extends AbstractController<CrudRepository<Contact, Serializable>, Contact> {

	@Autowired
	private ContactsRepos contactsRepos;


	@Override
	public String getRootPath() {
		return "contact";
	}

	@Override
	public CrudRepository<Contact, Serializable> getEntityRepos() {
		return contactsRepos;
	}
}
