package br.desafio.helpers.types;

import lombok.Getter;

public enum ContactAttribute {
	AGE("Idade"),
	STATE("Estado"),
	ROLE("Cargo");

	@Getter
	private String name;

	private ContactAttribute(final String pName) {
		this.name= pName;
	}

	public String getColumnName() {
		return this.toString().toLowerCase();
	}

}