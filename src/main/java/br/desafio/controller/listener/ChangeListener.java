package br.desafio.controller.listener;

/**
 * Interface para notificar alterações na entidade contato
 */
public interface ChangeListener<T> {

	void notify(T entity);

}
