package br.desafio.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import br.desafio.helpers.SearchParams;
import br.desafio.model.Contact;
import br.desafio.model.Segmentation;
import br.desafio.repos.ContactsCustomRepos;
import br.desafio.repos.SegmentationRepos;

/**
 * Responsável por receber as requisições REST da view de pesquisa
 */
@RestController
@RequestMapping("segmentation")
public class SegmentationController {

	@Autowired
	private SegmentationRepos segmentationRepos;

	@Autowired
	private ContactsCustomRepos contactsCustomRepos;

	@RequestMapping("/")
	public ModelAndView listSegmentations() {
		final ModelAndView modeView = new ModelAndView("/segmentation/list");
		modeView.addObject("segmentations", segmentationRepos.findAll());
		return modeView;
	}


	@RequestMapping("/search")
	public ModelAndView search() {
		final Segmentation segmentation = new Segmentation();
		segmentation.getSearchParams().add(new SearchParams());
		return sendToSearch(segmentation);
	}

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") final Long id) {
    	final Segmentation segmentation = segmentationRepos.findOne(id);
    	segmentation.fillParamsFromJson();
    	return sendToSearch(segmentation);
	}

	@RequestMapping(value = "/submitSearch", params={"addRow"}, method = RequestMethod.POST)
	public ModelAndView addRow(@ModelAttribute final Segmentation segmentation) {
		final boolean needsCombinator = !segmentation.getSearchParams().isEmpty();
		segmentation.getSearchParams().add(new SearchParams(needsCombinator));
		return sendToSearch(segmentation);
	}

	@RequestMapping(value = "/submitSearch", params={"removeRow"}, method = RequestMethod.POST)
	public ModelAndView removeRow(@ModelAttribute final Segmentation segmentation, final HttpServletRequest request) {
		final int rowId = Integer.valueOf(request.getParameter("removeRow"));
		segmentation.getSearchParams().remove(rowId);
		return sendToSearch(segmentation);
	}

	@RequestMapping(value = "/submitSearch", params={"search"}, method = RequestMethod.POST)
	public ModelAndView submitSearch(@Valid final Segmentation segmentation, final BindingResult result) {
        if(result.hasErrors()) {
            return sendToSearch(segmentation);
        }
        final List<Contact> contacts = contactsCustomRepos.findBySearchParams(segmentation.getSearchParams());
		return sendToSearch(segmentation, contacts);
	}

	@RequestMapping(value = "/submitSearch", params={"cancel"}, method = RequestMethod.POST)
	public ModelAndView cancelar() {
		return new ModelAndView("/segmentation/list");
	}

	@RequestMapping(value = "/submitSearch", params={"save"}, method = RequestMethod.POST)
	public ModelAndView save(@Valid final Segmentation segmentation, final BindingResult result) {
        if(result.hasErrors()) {
            return sendToSearch(segmentation);
        }
        segmentation.convertParamsToJson();
		segmentationRepos.save(segmentation);
		return listSegmentations();
	}

	public ModelAndView sendToSearch(final Segmentation segmentation) {
		return sendToSearch(segmentation, null);
	}

	public ModelAndView sendToSearch(final Segmentation segmentation, final Iterable<Contact> iterable) {
		final ModelAndView modeView = new ModelAndView("/segmentation/search");
		modeView.addObject("segmentation", segmentation);
		if (iterable != null) {
			modeView.addObject("contacts", iterable);
		}
		return modeView;
	}

}
