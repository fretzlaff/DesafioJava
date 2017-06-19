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
import javax.persistence.Transient;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.util.Base64Utils;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import br.desafio.search.SearchParams;
import lombok.Data;

/**
 * Modelo de dados para armazenar a segmentação
 */
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

	@Column(nullable = false)
	private String jsonSearchParams;

	@Transient
	private List<SearchParams> searchParams = new ArrayList<>();

	public void setSearchParams(final List<SearchParams> searchWrapper) {
		final Gson gson = new Gson();
		final String json = gson.toJson(searchWrapper);
		jsonSearchParams = Base64Utils.encodeToString(json.getBytes());
	}

	public void setJsonSearchParams(final String jsonSearchParams) {
		this.jsonSearchParams = jsonSearchParams;
		final Gson gson = new Gson();
		final Type listType = new TypeToken<List<SearchParams>>(){
			private static final long serialVersionUID = 4831247127082152645L;}.getType();
		searchParams = gson.fromJson(new String(Base64Utils.decodeFromString(jsonSearchParams)), listType);
	}

}