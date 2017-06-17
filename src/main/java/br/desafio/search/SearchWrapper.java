package br.desafio.search;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * Wrapper da lista de grupos de atributos de pesquisa
 * Só precisa dessa classe por causa de uma limitação do framework Thymeleaf para renderizar direto uma lista
 */
@Data
public class SearchWrapper {

	private List<SearchParams> paramsList = new ArrayList<>();

}
