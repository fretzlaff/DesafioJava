package br.desafio.contact;

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

import br.desafio.controller.ContactsController;
import br.desafio.controller.validation.ContactValidator;
import br.desafio.model.Contact;
import br.desafio.repos.ContactsRepos;
import br.desafio.service.ContactChangeService;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ContactsController.class)
public class ContactTest {

    @Autowired
	private MockMvc mockMvc;

    @MockBean
    private ContactsRepos contactsRepos;

    @MockBean
    private ContactValidator contactValidator;

    @MockBean
    private ContactChangeService contactChangeService;

	final private String name = "joao";

	final private String email = "joao@empresa.com";

	final private Integer age = 22;

	final private String state = "SC";

	final private String role = "Programador";

	@Before
	public void setup() throws Exception {

		final Contact contact = new Contact();
		contact.setId(1l);
		contact.setName(name);
		contact.setEmail(email);
		contact.setAge(age);
		contact.setState(state);
		contact.setRole(role);

		final List<Contact> contacts = new ArrayList<>();
		contacts.add(contact);

		when(contactsRepos.save(contact)).thenReturn(contact);
		when(contactsRepos.findAll()).thenReturn(contacts);
		when(contactsRepos.findOne(anyLong())).thenReturn(contact);
		when(contactValidator.supports(any())).thenReturn(true);
	}

	@Test
	public void testContactList() throws Exception {
		this.mockMvc.perform(get("/contact/"))
			.andExpect(status().isOk())
			.andExpect(view().name("contact/list"))
			.andExpect(content().string(Matchers.containsString("/contact/add/")));
	}

	@Test
	public void saveSuccess() throws Exception {

		this.mockMvc.perform(post("/contact/save")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.param("name", name)
		            .param("email", email)
		            .param("age", String.valueOf(age))
		            .param("state", state)
		            .param("role", role)
					.sessionAttr("contact", new Contact()))
			.andExpect(status().isOk())
			.andExpect(view().name("contact/list"))
			.andExpect(content().string(Matchers.containsString("<td>" + name + "</td>")))
			.andExpect(content().string(Matchers.containsString("<td>" + email + "</td>")))
			.andExpect(content().string(Matchers.containsString("<td>" + age + "</td>")))
			.andExpect(content().string(Matchers.containsString("<td>" + state + "</td>")))
			.andExpect(content().string(Matchers.containsString("<td>" + role + "</td>")));
	}

	@Test
	public void saveError() throws Exception {

		this.mockMvc.perform(post("/contact/save")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
		            .param("state", state)
		            .param("role", role)
					.sessionAttr("contact", new Contact()))
			.andExpect(status().isOk())
			.andExpect(view().name("contact/add"))
			.andExpect(content().string(Matchers.containsString("Email é uma informação obrigatória.")))
			.andExpect(content().string(Matchers.containsString("Nome é uma informação obrigatória.")))
			.andExpect(content().string(Matchers.containsString("Idade é uma informação obrigatória.")));
	}

	@Test
	public void saveErrorInvalidEmail() throws Exception {

		this.mockMvc.perform(post("/contact/save")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.param("name", name)
		            .param("email", "email_incorreto")
		            .param("age", String.valueOf(age))
		            .param("state", state)
		            .param("role", role)
					.sessionAttr("contact", new Contact()))
			.andExpect(status().isOk())
			.andExpect(view().name("contact/add"))
			.andExpect(content().string(Matchers.containsString("Formato de e-mail inválido.")));
	}

	@Test
	public void openEditSuccess() throws Exception {

		this.mockMvc.perform(get("/contact/edit/1"))
			.andExpect(status().isOk())
			.andExpect(view().name("contact/add"))
			.andExpect(xpath("//input[@name='name' and @value='" + name + "']").exists())
			.andExpect(xpath("//input[@name='email' and @value='" + email + "']").exists())
			.andExpect(xpath("//input[@name='age' and @value='" + age + "']").exists())
			.andExpect(xpath("//input[@name='state' and @value='" + state+ "']").exists())
			.andExpect(xpath("//input[@name='role' and @value='" + role + "']").exists());
	}

}
