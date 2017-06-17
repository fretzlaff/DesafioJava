package br.desafio.search.types;

import lombok.Getter;

/**
 * Define as regras de comparação entre os atributos escolhidos e os valores informados
 */
public enum ComparisonRule {

	EQUALS("Igual a"),
	CONTAINS("Contém"),
	STARTS_WITH("Começa com"),
	ENDS_WITH("Termina com"),
	LESS_THAN("Menor que"),
	LESS_IGUALS_THAN("Menor ou igual a"),
	GREATHER_THAN("Maior que"),
	GREATHER_EQUALS_THAN("Maior ou igual a");

	@Getter
	private static final ComparisonRule[] INTEGER_RULES = new ComparisonRule[] {
			EQUALS,
			LESS_THAN,
			LESS_IGUALS_THAN,
			GREATHER_THAN,
			GREATHER_EQUALS_THAN
			};

	@Getter
	private static final ComparisonRule[] STRING_RULES = new ComparisonRule[] {
			EQUALS,
			CONTAINS,
			STARTS_WITH,
			ENDS_WITH
			};

	@Getter
	private String name;

	private ComparisonRule(final String pName) {
		this.name= pName;
	}

}
