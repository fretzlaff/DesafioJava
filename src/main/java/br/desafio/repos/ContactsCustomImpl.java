package br.desafio.repos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import br.desafio.helpers.SearchParams;
import br.desafio.model.Contact;
import lombok.extern.slf4j.Slf4j;

/**
 * Responsável por montar a query a partir dos critérios definidos na segmentação
 */
@Slf4j
@Repository
public class ContactsCustomImpl implements ContactsCustomRepos {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Contact> findBySearchParams(final List<SearchParams> paramsList) {
		final StringBuilder builder = new StringBuilder("from Contact ");
		buildSearchCriteria(paramsList, builder);
		final String query = builder.toString();
		log.info("Query: {}", query);

		return entityManager.createQuery(query, Contact.class).getResultList();
	}

	private void buildSearchCriteria(final List<SearchParams> paramsList, final StringBuilder builder) {
		boolean first = true;
		for (final SearchParams searchParam : paramsList) {
			if (first) {
				//na primeira iteração coloca o where
				builder.append("where");
				first = false;
			} else {
				//na demais iterações coloca AND ou OR
				builder.append(searchParam.getGroupCombinator());
			}
			builder.append(" ");
			builder.append(searchParam.getContactAttribute().getColumnName());
			builder.append(searchParam.getComparisonRule().getQueryOperation(searchParam.getValue()));
		}
	}

}
