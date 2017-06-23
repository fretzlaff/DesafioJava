package br.desafio.model;

import org.hibernate.validator.constraints.NotBlank;

import br.desafio.helpers.ComparisonRule;
import br.desafio.helpers.ContactAttribute;
import br.desafio.helpers.GroupCombinator;
import lombok.Data;

/**
 * Classe responsável por agrupar e armazenar os critéiosda consulta
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
	private ContactAttribute contactAttribute = ContactAttribute.AGE;

	/**
	 * Regra de comparação entre o atributo e o valor
	 */
	private ComparisonRule comparisonRule = ComparisonRule.EQUALS;

	/**
	 * Valor informado pelo usuário
	 */
	@NotBlank(message = "Valor é uma informação obrigatória.")
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
