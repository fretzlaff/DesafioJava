package br.desafio.controller;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import br.desafio.controller.validation.SegmentationValidator;
import br.desafio.model.Contact;
import br.desafio.model.SearchParams;
import br.desafio.model.Segmentation;
import br.desafio.repos.ContactsCustomRepos;
import br.desafio.repos.SegmentationRepos;

/**
 * Responsável por receber as requisições REST da view de pesquisa
 */
@RestController
@RequestMapping("segmentation")
public class SegmentationController extends AbstractController<CrudRepository<Segmentation, Serializable>, Segmentation> {

	@Autowired
	private SegmentationRepos segmentationRepos;

	@Autowired
	private ContactsCustomRepos contactsCustomRepos;

	@Autowired
	private SegmentationValidator segmentationValidator;

    @InitBinder("segmentation")
    protected void initBinder(final WebDataBinder binder) {
        binder.addValidators(segmentationValidator);
    }


	@RequestMapping(value = "/submitSearch", params={"addCriteria"}, method = RequestMethod.POST)
	public ModelAndView addCriteria(@ModelAttribute final Segmentation segmentation) {
		final boolean needsCombinator = !segmentation.getSearchParams().isEmpty();
		segmentation.getSearchParams().add(new SearchParams(needsCombinator));
		return sendToSearch(segmentation);
	}

	@RequestMapping(value = "/submitSearch", params={"removeCriteria"}, method = RequestMethod.POST)
	public ModelAndView removeCriteria(@ModelAttribute final Segmentation segmentation, final HttpServletRequest request) {
		final int rowId = Integer.valueOf(request.getParameter("removeCriteria"));
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

	@Override
	@RequestMapping(value = "/submitSearch", params={"save"}, method = RequestMethod.POST)
	public ModelAndView save(@Valid final Segmentation segmentation, final BindingResult result, final HttpServletRequest request) {
        segmentation.convertParamsToJson();
		return super.save(segmentation, result, request);
	}

	public ModelAndView sendToSearch(final Segmentation segmentation) {
		return sendToSearch(segmentation, null);
	}

	public ModelAndView sendToSearch(final Segmentation segmentation, final Iterable<Contact> iterable) {
		final ModelAndView modeView = add(segmentation);
		modeView.addObject("segmentation", segmentation);
		if (iterable != null) {
			modeView.addObject("contacts", iterable);
		}
		return modeView;
	}

	@Override
	public CrudRepository<Segmentation, Serializable> getEntityRepos() {
		return segmentationRepos;
	}

	@Override
	public String getRootPath() {
		return "segmentation";
	}

}
