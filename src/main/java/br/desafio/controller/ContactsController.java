package br.desafio.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import br.desafio.model.Contact;
import br.desafio.repos.ContactsRepos;

/**
 * Responsável por receber as requisições REST da view de contatos
 */
@RestController
@RequestMapping("contact")
public class ContactsController {

	@Autowired
	private ContactsRepos contactsRepos;

	@RequestMapping("/")
	public ModelAndView listContacts() {
		final ModelAndView modeView = new ModelAndView("/contact/list");
		modeView.addObject("contacts", contactsRepos.findAll());
		return modeView;
	}

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") final Long id) {
        return add(contactsRepos.findOne(id));
    }

    @GetMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable("id") final Long id) {
        contactsRepos.delete(id);
        return listContacts();
    }

    @GetMapping("/add")
    public ModelAndView add(final Contact contact) {
        final ModelAndView mv = new ModelAndView("/contact/add");
        mv.addObject("contact", contact);
        return mv;
    }

    @PostMapping("/save")
    public ModelAndView save(@Valid final Contact contact, final BindingResult result) {
        if(result.hasErrors()) {
            return add(contact);
        }
        contactsRepos.save(contact);
        return listContacts();
    }
}
