package br.desafio.helpers;

import br.desafio.helpers.types.ComparisonRule;
import br.desafio.helpers.types.ContactAttribute;
import br.desafio.helpers.types.GroupCombinator;
import lombok.Data;

/**
 * Classe responsável por agrupar e armazenar os grupos de campos da consulta
 */
@Data
public class SearchParams {

	public SearchParams() {

	}


	public SearchParams(final boolean needsCombinator) {
		this.needsCombinator = needsCombinator;
	}

	/**
	 * Atributos do modelo Contato que poderão ser pesquisados
	 */
	private ContactAttribute contactAttribute;

	/**
	 * Regra de comparação entre o atributo e o valor
	 */
	private ComparisonRule comparisonRule;

	/**
	 * Valor informado pelo usuário
	 */
	private String value;

	/**
	 * Define a regra de comparação entre dois grupo de atributos.
	 * Os valores possíveis são "E" ou "OU"
	 * Só vai existir a partir da criação do segundo grupo de filtros
	 */
	private GroupCombinator groupCombinator = GroupCombinator.AND;

	/**
	 * Define se precisa considerar o combinador de grupos
	 */
	private boolean needsCombinator = false;

}