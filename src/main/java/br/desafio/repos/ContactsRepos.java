package br.desafio.repos;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.desafio.model.Contact;

/**
 * Classe responsável por realizar as operação dos contatos com o banco de dados.
 */
@Repository
public interface ContactsRepos extends CrudRepository<Contact, Serializable> {


}
