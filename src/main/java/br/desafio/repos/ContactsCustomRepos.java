package br.desafio.repos;

import java.util.List;

import br.desafio.model.Contact;
import br.desafio.model.SearchParams;

public interface ContactsCustomRepos  {

	List<Contact> findBySearchParams(List<SearchParams> paramsList);

}
