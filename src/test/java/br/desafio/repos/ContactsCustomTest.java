package br.desafio.repos;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import br.desafio.helpers.ComparisonRule;
import br.desafio.helpers.ContactAttribute;
import br.desafio.helpers.GroupCombinator;
import br.desafio.model.SearchParams;

/**
 * Teste unitário para testar a montagem da query a partir
 * de diversas combinações dos parâmetros de busca.
 */
public class ContactsCustomTest {

	final private ContactsCustomImpl custom = new ContactsCustomImpl();

	@Test
	public void testOneParamsGroup() {
		final List<SearchParams> paramsList = buildParams(ContactAttribute.AGE, ComparisonRule.EQUALS, "25");

		final String query = custom.buildSearchCriteria(paramsList).trim();

		final String expected = "from Contact where age = :value0";
		assertEquals(expected, query);
	}

	@Test
	public void testTwoParamsGroup() {
		final List<SearchParams> paramsList = buildParams(ContactAttribute.AGE, ComparisonRule.LESS_THAN, "25");
		addParams(ContactAttribute.STATE, ComparisonRule.EQUALS, "SC", paramsList);

		final String query = custom.buildSearchCriteria(paramsList).trim();

		final String expected = "from Contact where age < :value0 AND state = :value1";
		assertEquals(expected, query);
	}

	@Test
	public void testAllAttributeTypes() {
		final List<SearchParams> paramsList = buildParams(ContactAttribute.AGE, ComparisonRule.LESS_THAN, "25");
		addParams(ContactAttribute.STATE, ComparisonRule.EQUALS, "SC", paramsList);
		addParams(ContactAttribute.ROLE, ComparisonRule.CONTAINS, "Analista", paramsList);

		final String query = custom.buildSearchCriteria(paramsList).trim();

		final String expected = "from Contact where age < :value0 AND state = :value1 AND role like :value2";
		assertEquals(expected, query);
	}

	@Test
	public void testORCombinator() {
		final List<SearchParams> paramsList = buildParams(ContactAttribute.AGE, ComparisonRule.LESS_THAN, "25");
		addParams(ContactAttribute.STATE, ComparisonRule.EQUALS, "SC", GroupCombinator.OR, paramsList);

		final String query = custom.buildSearchCriteria(paramsList).trim();

		final String expected = "from Contact where age < :value0 OR state = :value1";
		assertEquals(expected, query);
	}

	private List<SearchParams> buildParams(final ContactAttribute attribute, final ComparisonRule rule, final String value) {
		final SearchParams params = new SearchParams();
		params.setContactAttribute(attribute);
		params.setComparisonRule(rule);
		params.setValue(value);

		final List<SearchParams> paramsList = new ArrayList<>();
		paramsList.add(params);

		return paramsList;
	}

	private void addParams(final ContactAttribute attribute, final ComparisonRule rule, final String value, final List<SearchParams> paramsList) {
		addParams(attribute, rule, value, GroupCombinator.AND, paramsList);
	}

	private void addParams(final ContactAttribute attribute, final ComparisonRule rule, final String value, final GroupCombinator combinator, final List<SearchParams> paramsList) {
		final SearchParams params = new SearchParams();
		params.setContactAttribute(attribute);
		params.setComparisonRule(rule);
		params.setValue(value);
		params.setGroupCombinator(combinator);

		paramsList.add(params);
	}

}
