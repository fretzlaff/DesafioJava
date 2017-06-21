package br.desafio.segmentation;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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

import br.desafio.controller.ContactsController;
import br.desafio.controller.validation.SegmentationValidator;
import br.desafio.model.Contact;
import br.desafio.repos.SegmentationRepos;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ContactsController.class)
public class SegmentationTest {

    @Autowired
	private MockMvc mockMvc;

    @MockBean
    private SegmentationRepos segmentationRepos;

    @MockBean
    private SegmentationValidator segmentationValidator;

	@Before
	public void setup() throws Exception {
		when(segmentationValidator.supports(any())).thenReturn(true);
	}

	@Test
	public void testSegmentationList() throws Exception {
		this.mockMvc.perform(get("/segmentation/"))
			.andExpect(status().isOk())
			.andExpect(view().name("segmentation/list"))
			.andExpect(content().string(Matchers.containsString("/segmentation/add/")))
			.andDo(print());
	}

	@Test
	public void testSegmentationSave() throws Exception {

		/*
		 	searchParams[0].needsCombinator
			searchParams[0].contactAttribute
			searchParams[0].comparisonRule
			searchParams[0].value
			searchParams[1].groupCombinator

			verificar o nome dos objetos na httpsession
		 */
		this.mockMvc.perform(post("/segmentation/save")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.param("description", "segmentation")
		            .param("role", "Programador")
					.sessionAttr("contact", new Contact()))
			.andExpect(status().isOk())
			.andExpect(view().name("contact/list"))
			.andExpect(content().string(Matchers.containsString("/contact/add/")))
			.andDo(print());
	}

}
