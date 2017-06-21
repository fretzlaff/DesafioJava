package br.desafio.helpers.types;

import lombok.Getter;

public enum ContactAttribute {
	AGE("Idade", ComparisonRule.INTEGER_RULES),
	STATE("Estado", ComparisonRule.STRING_RULES),
	ROLE("Cargo", ComparisonRule.STRING_RULES);

	@Getter
	private String name;

	@Getter
	private ComparisonRule[] comparisonRule;

	private ContactAttribute(final String name, final ComparisonRule[] comparisonRule) {
		this.name= name;
		this.comparisonRule = comparisonRule;
	}

	public String getColumnName() {
		return this.toString().toLowerCase();
	}

}
