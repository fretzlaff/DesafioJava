package br.desafio.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.desafio.controller.listener.ChangeListener;
import br.desafio.controller.validation.ContactValidator;
import br.desafio.model.Contact;
import br.desafio.repos.ContactsRepos;
import br.desafio.service.ContactChangeService;

/**
 * Responsável por receber as requisições REST da view de contatos
 */
@RestController
@RequestMapping("contact")
public class ContactsController extends AbstractController<CrudRepository<Contact, Serializable>, Contact> {

	/**
	 * Armazena os listener que vão escutar os aventos de alteração de um contato
	 * Usado no polling da tela para atualizar os contatos quando necessário
	 */
	private static List<ChangeListener<Contact>> changeListeners = new ArrayList<>();

	@Autowired
	private ContactsRepos contactsRepos;

	@Autowired
	private ContactValidator contactValidator;

	@Autowired
	private ContactChangeService contactChangeService;


    @InitBinder("contact")
    protected void initBinder(final WebDataBinder binder) {
        binder.addValidators(contactValidator);
    }

    /**
     * Esse método recebe a chamada ajax da tela para identificar se houve alteração em algum contato
     * Ele verifica se houve alteração durante 20s (10 x 2s), caso contrário retorna nulo
     */
	@GetMapping("/listen")
    public String listen() {
		String contactName = null;

		for (int i = 0; i < 10; i++) {
			final Contact contact = contactChangeService.nextChangedContact();
			if (contact == null) {
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				contactName = contact.getName();
				break; // sai do looping
			}
		}
		return contactName;
    }

	/**
	 * Verifica se houve alteração nos atributos idade, estato e cargo do contato a ser salvo
	 * Caso tenha sido alterado gera uma notificações para ser consumida pela tela
	 * de visualização da segmentação
	 */
    @Override
	public void beforeSave(final Contact entity) {
       	boolean changed = false;
       	if (entity.getId() == null || entity.getId() == 0) {
       		// contato novo deve notificar tbém
			changed = true;
       	} else {
       		// contato alterado considera os atributos da pesquisa (idade, estado e cargo)
           	final Contact contact = contactsRepos.findOne(entity.getId());
    		if (entity.getAge() != null && !entity.getAge().equals(contact.getAge())) {
    			changed = true;
    		}

    		if (entity.getState() != null && !entity.getState().equals(contact.getState())) {
    			changed = true;
    		}

    		if (entity.getRole() != null && !entity.getRole().equals(contact.getRole())) {
    			changed = true;
    		}
       	}
		if (changed) {
			notifyChanges(entity);
		}
	}

	public void notifyChanges(final Contact entity) {
		for (final ChangeListener<Contact> changeListener : changeListeners) {
			changeListener.notify(entity);
		}
	}

	public static void addListener(final ChangeListener<Contact> changeListener) {
		changeListeners.add(changeListener);
	}

	@Override
	public String getRootPath() {
		return "contact";
	}

	@Override
	public CrudRepository<Contact, Serializable> getEntityRepos() {
		return contactsRepos;
	}
}
