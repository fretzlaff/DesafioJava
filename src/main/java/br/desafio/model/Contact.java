package br.desafio.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

/**
 * Classe que represanta o modelo de dados que será gravado no banco de dados
 */
@Data
@Entity
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", unique=true, nullable = false)
	private Long id;

	@Column(nullable = false)
	@NotBlank(message = "Nome é uma informação obrigatória.")
	private String name;

	@Column(nullable = false)
	@NotBlank(message = "Email é uma informação obrigatória.")
	@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message ="Formato de e-mail inválido.")
	private String email;

	@Column(nullable = false)
	@NotNull(message = "Idade é uma informação obrigatória.")
	private Integer age;

	@Column(nullable = false)
	private String state;

	@Column(nullable = false)
	private String role;
}
