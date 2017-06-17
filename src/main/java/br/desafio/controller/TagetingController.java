package br.desafio.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import br.desafio.search.SearchParams;
import br.desafio.search.SearchWrapper;

/**
 * Responsável por receber as requisições REST da view de pesquisa
 */
@RestController
public class TagetingController {

	@RequestMapping("/search")
	public ModelAndView search() {
		final SearchWrapper searchWrapper = new SearchWrapper();
		searchWrapper.getParamsList().add(new SearchParams());
		return sendToSearch(searchWrapper);
	}

	@RequestMapping(value = "/submitSearch", params={"addRow"}, method = RequestMethod.POST)
	public ModelAndView addRow(@ModelAttribute final SearchWrapper searchWrapper, final ModelMap model) {
		final boolean needsCombinator = !searchWrapper.getParamsList().isEmpty();
		searchWrapper.getParamsList().add(new SearchParams(needsCombinator));
		return sendToSearch(searchWrapper);
	}

	@RequestMapping(value = "/submitSearch", params={"removeRow"}, method = RequestMethod.POST)
	public ModelAndView removeRow(@ModelAttribute final SearchWrapper searchWrapper, final HttpServletRequest request) {
		final int rowId = Integer.valueOf(request.getParameter("removeRow"));
		searchWrapper.getParamsList().remove(rowId);
		return sendToSearch(searchWrapper);
	}

	@RequestMapping(value = "/submitSearch", method = RequestMethod.POST)
	public ModelAndView submitSearch(@ModelAttribute final SearchWrapper searchWrapper) {
		final boolean needsCombinator = !searchWrapper.getParamsList().isEmpty();
		searchWrapper.getParamsList().add(new SearchParams(needsCombinator));
		return sendToSearch(searchWrapper);
	}

	public ModelAndView sendToSearch(final SearchWrapper searchWrapper) {
		final ModelAndView modeView = new ModelAndView("/search");
		modeView.addObject("searchWrapper", searchWrapper);
		return modeView;
	}

}
