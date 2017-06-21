package br.desafio.contact;

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
import br.desafio.controller.validation.ContactValidator;
import br.desafio.model.Contact;
import br.desafio.repos.ContactsRepos;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ContactsController.class)
public class ContactTest {

    @Autowired
	private MockMvc mockMvc;

    @MockBean
    private ContactsRepos contactsRepos;

    @MockBean
    private ContactValidator contactValidator;

	@Before
	public void setup() throws Exception {
		when(contactValidator.supports(any())).thenReturn(true);
	}

	@Test
	public void testContactList() throws Exception {
		this.mockMvc.perform(get("/contact/"))
			.andExpect(status().isOk())
			.andExpect(view().name("contact/list"))
			.andExpect(content().string(Matchers.containsString("/contact/add/")))
			.andDo(print());
	}

	@Test
	public void testContactSave() throws Exception {

		this.mockMvc.perform(post("/contact/save")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.param("name", "joao")
		            .param("email", "joao@empresa.com")
		            .param("age", "22")
		            .param("state", "SC")
		            .param("role", "Programador")
					.sessionAttr("contact", new Contact()))
			.andExpect(status().isOk())
			.andExpect(view().name("contact/list"))
			.andExpect(content().string(Matchers.containsString("/contact/add/")))
			.andDo(print());
	}

}
