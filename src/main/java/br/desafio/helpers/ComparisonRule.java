package br.desafio.helpers;

import lombok.Getter;

/**
 * Define as regras de comparação entre os atributos escolhidos e os valores informados
 */
public enum ComparisonRule {

	EQUALS("Igual a", " = "),
	CONTAINS("Contém", " like "),
	STARTS_WITH("Começa com", " like "),
	ENDS_WITH("Termina com", " like "),
	LESS_THAN("Menor que", " < "),
	LESS_IGUALS_THAN("Menor ou igual a", " <= "),
	GREATHER_THAN("Maior que", " > "),
	GREATHER_EQUALS_THAN("Maior ou igual a", " >= ");

	public static final ComparisonRule[] INTEGER_RULES = new ComparisonRule[] {
			EQUALS,
			LESS_THAN,
			LESS_IGUALS_THAN,
			GREATHER_THAN,
			GREATHER_EQUALS_THAN
			};

	public static final ComparisonRule[] STRING_RULES = new ComparisonRule[] {
			EQUALS,
			CONTAINS,
			STARTS_WITH,
			ENDS_WITH
			};

	@Getter
	private String name;

	@Getter
	private String queryOperation;

	private ComparisonRule(final String name, final String queryOperation) {
		this.name= name;
		this.queryOperation = queryOperation;
	}

}
