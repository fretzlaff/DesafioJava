package br.desafio.repos;

import java.util.List;

import br.desafio.helpers.SearchParams;
import br.desafio.model.Contact;

public interface ContactsCustomRepos  {

	List<Contact> findBySearchParams(List<SearchParams> paramsList);

}
