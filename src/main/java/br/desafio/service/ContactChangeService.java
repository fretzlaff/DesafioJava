package br.desafio.service;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import org.springframework.stereotype.Service;

import br.desafio.controller.ContactsController;
import br.desafio.controller.listener.ChangeListener;
import br.desafio.model.Contact;

/**
 * Serviço responsável por controlar as alterações dos contatos
 * Esse serviço se registra como listener dos eventos de alteração de contato
 * e coloca os contatos alterados numa fila
 * O método nextChangedContact deve ser chamado pelo controller para
 * buscar o contato que foi alterado.
 */
@Service
public class ContactChangeService implements ChangeListener<Contact>{

	private final Queue<Contact> queue = new ArrayBlockingQueue<>(100);

	public ContactChangeService() {
		ContactsController.addListener(this);
	}

	public Contact nextChangedContact() {
		return queue.poll();
	}

	@Override
	public void notify(final Contact contact) {
		queue.add(contact);
	}

}
