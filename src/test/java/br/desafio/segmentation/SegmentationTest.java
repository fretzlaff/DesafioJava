package br.desafio.segmentation;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import br.desafio.controller.SegmentationController;
import br.desafio.controller.validation.SegmentationValidator;
import br.desafio.helpers.SearchParams;
import br.desafio.helpers.types.ComparisonRule;
import br.desafio.helpers.types.ContactAttribute;
import br.desafio.helpers.types.GroupCombinator;
import br.desafio.model.Segmentation;
import br.desafio.repos.ContactsCustomRepos;
import br.desafio.repos.SegmentationRepos;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = SegmentationController.class)
public class SegmentationTest {

    @Autowired
	private MockMvc mockMvc;

    @MockBean
    private SegmentationRepos segmentationRepos;

    @MockBean
    private ContactsCustomRepos contactsCustomRepos;

    @MockBean
    private SegmentationValidator segmentationValidator;

    final private Segmentation segmentation = new Segmentation();

	@Before
	public void setup() throws Exception {
		segmentation.setDescription("segmentation description");
		segmentation.getSearchParams().clear();

		addParamsToList(ContactAttribute.AGE, ComparisonRule.LESS_THAN, "30");
		addParamsToList(ContactAttribute.STATE, ComparisonRule.EQUALS, "SC");
		addParamsToList(ContactAttribute.ROLE, ComparisonRule.STARTS_WITH, "Analista", GroupCombinator.OR);

		final List<Segmentation> segmentations = new ArrayList<>();
		segmentations.add(segmentation);

		when(segmentationRepos.save(segmentation)).thenReturn(segmentation);
		when(segmentationRepos.findAll()).thenReturn(segmentations);
		when(segmentationRepos.findOne(anyLong())).thenReturn(segmentation);
		when(segmentationValidator.supports(any())).thenReturn(true);
	}

	@Test
	public void testList() throws Exception {
		this.mockMvc.perform(get("/segmentation/"))
			.andExpect(status().isOk())
			.andExpect(view().name("segmentation/list"))
			.andExpect(content().string(Matchers.containsString("/segmentation/add/")));
	}

	@Test
	public void saveSuccess() throws Exception {

		this.mockMvc.perform(post("/segmentation/submitSearch")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.param("description", segmentation.getDescription())
		            .param("searchParams[0].needsCombinator", "false")
		            .param("searchParams[0].contactAttribute", "AGE")
		            .param("searchParams[0].comparisonRule", "LESS_THAN")
		            .param("searchParams[0].value", "30")
		            .param("searchParams[1].groupCombinator", "AND")
		            .param("searchParams[1].needsCombinator", "true")
		            .param("searchParams[1].contactAttribute", "STATE")
		            .param("searchParams[1].comparisonRule", "EQUALS")
		            .param("searchParams[1].value", "SC")
		            .param("save", "save")
					.sessionAttr("segmentation", new Segmentation()))
			.andExpect(status().isOk())
			.andExpect(view().name("segmentation/list"))
			.andExpect(content().string(Matchers.containsString("/segmentation/add/")))
			.andExpect(content().string(Matchers.containsString("<td>" + segmentation.getDescription() + "</td>")));
	}

	@Test
	public void testSaveError() throws Exception {

		this.mockMvc.perform(post("/segmentation/submitSearch")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
		            .param("searchParams[0].needsCombinator", "false")
		            .param("searchParams[0].contactAttribute", "AGE")
		            .param("searchParams[0].comparisonRule", "LESS_THAN")
		            .param("save", "save")
					.sessionAttr("segmentation", new Segmentation()))
			.andExpect(status().isOk())
			.andExpect(view().name("segmentation/add"))
			.andExpect(content().string(Matchers.containsString("<span>Descrição é uma informação obrigatória.</span>")))
			.andExpect(content().string(Matchers.containsString("<span>Valor é uma informação obrigatória.</span>")));
	}

	@Test
	public void openEditSuccess() throws Exception {

		this.mockMvc.perform(get("/segmentation/edit/1"))
			.andExpect(status().isOk())
			.andExpect(view().name("segmentation/add"))
			.andExpect(xpath("//input[@name='description' and @value='" + segmentation.getDescription() + "']").exists())

			.andExpect(xpath("//select[@name='searchParams[0].contactAttribute' and option[@value='AGE' and @selected='selected']]").exists())
			.andExpect(xpath("//select[@name='searchParams[0].comparisonRule' and option[@value='LESS_THAN' and @selected='selected']]").exists())
			.andExpect(xpath("//input[@name='searchParams[0].value' and @value='30']").exists())

			.andExpect(xpath("//select[@name='searchParams[1].contactAttribute' and option[@value='STATE' and @selected='selected']]").exists())
			.andExpect(xpath("//select[@name='searchParams[1].comparisonRule' and option[@value='EQUALS' and @selected='selected']]").exists())
			.andExpect(xpath("//input[@name='searchParams[1].value' and @value='SC']").exists())
			.andExpect(xpath("//select[@name='searchParams[1].groupCombinator' and option[@value='AND' and @selected='selected']]").exists())

			.andExpect(xpath("//select[@name='searchParams[2].contactAttribute' and option[@value='ROLE' and @selected='selected']]").exists())
			.andExpect(xpath("//select[@name='searchParams[2].comparisonRule' and option[@value='STARTS_WITH' and @selected='selected']]").exists())
			.andExpect(xpath("//input[@name='searchParams[2].value' and @value='Analista']").exists())
			.andExpect(xpath("//select[@name='searchParams[2].groupCombinator' and option[@value='OR' and @selected='selected']]").exists());
	}


	private void addParamsToList(final ContactAttribute attribute, final ComparisonRule rule, final String value) {
		addParamsToList(attribute, rule, value, GroupCombinator.AND);
	}

	private void addParamsToList(final ContactAttribute attribute, final ComparisonRule rule, final String value, final GroupCombinator combinator) {
		final SearchParams params = new SearchParams();
		params.setContactAttribute(attribute);
		params.setComparisonRule(rule);
		params.setValue(value);
		if (segmentation.getSearchParams().size() > 0) {
			params.setNeedsCombinator(true);
			params.setGroupCombinator(combinator);
		}

		segmentation.getSearchParams().add(params);
	}

}
