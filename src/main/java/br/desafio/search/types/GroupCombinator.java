package br.desafio.search.types;

import lombok.Getter;

public enum GroupCombinator {
	AND("E"),
	OR("OU");

	@Getter
	private String name;

	private GroupCombinator(final String pName) {
		this.name= pName;
	}
}
