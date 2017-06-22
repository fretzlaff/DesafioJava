package br.desafio.repos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.google.common.annotations.VisibleForTesting;

import br.desafio.helpers.ComparisonRule;
import br.desafio.helpers.ContactAttribute;
import br.desafio.model.Contact;
import br.desafio.model.SearchParams;
import lombok.extern.slf4j.Slf4j;

/**
 * Responsável por montar a query a partir dos critérios definidos na segmentação
 * O detalhe mais importante dessa classe é o fato de que a query deve ser montada
 * usando parâmetros para evitar SQL Injection
 */
@Slf4j
@Repository
public class ContactsCustomImpl implements ContactsCustomRepos {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Contact> findBySearchParams(final List<SearchParams> paramsList) {
		final String queryStr = buildSearchCriteria(paramsList);
		log.info("Query: {}", queryStr);

		final Query query = entityManager.createQuery(queryStr, Contact.class);
		setParameterValues(query, paramsList);

		return query.getResultList();
	}

	@VisibleForTesting
	protected String buildSearchCriteria(final List<SearchParams> paramsList) {
		final StringBuilder builder = new StringBuilder("from Contact ");

		boolean first = true;
		int index = 0;
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
			builder.append(searchParam.getComparisonRule().getQueryOperation());
			builder.append(":value").append(index).append(" ");
			index++;
		}

		return builder.toString();
	}

	private void setParameterValues(final Query query, final List<SearchParams> paramsList) {
		int index = 0;
		for (final SearchParams searchParam : paramsList) {
			final String valueName = "value" + index;
			final Object value = getValueFrom(searchParam);
			query.setParameter(valueName, value);
			index++;
		}
	}

	private Object getValueFrom(final SearchParams searchParam) {
		Object result = null;
		if (searchParam.getContactAttribute().equals(ContactAttribute.AGE)) {
			result = Integer.valueOf(searchParam.getValue());
		} else if (searchParam.getComparisonRule().equals(ComparisonRule.CONTAINS)) {
			result = "%" + searchParam.getValue() + "%";
		} else if (searchParam.getComparisonRule().equals(ComparisonRule.STARTS_WITH)) {
			result = searchParam.getValue() + "%";
		} else if (searchParam.getComparisonRule().equals(ComparisonRule.ENDS_WITH)) {
			result = "%" + searchParam.getValue();
		} else {
			result = searchParam.getValue();
		}
		return result;
	}

}
