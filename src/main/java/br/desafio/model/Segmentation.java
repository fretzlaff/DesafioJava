package br.desafio.model;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.Transient;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.util.Base64Utils;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import br.desafio.helpers.SearchParams;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Modelo de dados para armazenar a segmentação
 */
@Slf4j
@Data
@Entity
public class Segmentation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", unique=true, nullable = false)
	private Long id;

	@Column(nullable = false)
	@NotBlank(message = "Descrição é uma informação obrigatória.")
	private String description;

	@Column(nullable = false, length=500)
	private String jsonStringParams;

	@Valid
	@Transient
	private List<SearchParams> searchParams = new ArrayList<>();

	public Segmentation() {
		searchParams.add(new SearchParams());
	}

	public void convertParamsToJson() {
		log.info("Convertendo parâmetros de pesquisa para json");
		final Gson gson = new Gson();
		final String json = gson.toJson(searchParams);
		jsonStringParams = Base64Utils.encodeToString(json.getBytes());
	}

	@PostLoad
	public void fillParamsFromJson() {
		// Serializa a lista de parâmetros de pesquisa
		log.info("Carragndo parâmetros de pesquisa do banco");
		final Gson gson = new Gson();
		final Type listType = new TypeToken<List<SearchParams>>(){
			private static final long serialVersionUID = 4831247127082152645L;}.getType();
		this.searchParams = gson.fromJson(new String(Base64Utils.decodeFromString(jsonStringParams)), listType);
	}

}