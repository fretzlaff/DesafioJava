package br.desafio.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import br.desafio.repos.SegmentationRepos;
import br.desafio.search.SearchParams;
import br.desafio.search.SearchWrapper;

/**
 * Responsável por receber as requisições REST da view de pesquisa
 */
@RestController
@RequestMapping("segmentation")
public class SegmentationController {

	@Autowired
	private SegmentationRepos segmentationRepos;

	@RequestMapping("/")
	public ModelAndView listSegmentations() {
		final ModelAndView modeView = new ModelAndView("/segmentation/list");
		modeView.addObject("segmentations", segmentationRepos.findAll());
		return modeView;
	}


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

	@RequestMapping(value = "/submitSearch", params={"search"}, method = RequestMethod.POST)
	public ModelAndView submitSearch(@ModelAttribute final SearchWrapper searchWrapper) {

		//TODO: Fazer consulta
		return sendToSearch(searchWrapper);
	}

	@RequestMapping(value = "/submitSearch", params={"cancel"}, method = RequestMethod.POST)
	public ModelAndView cancelar() {
		return new ModelAndView("/segmentation/list");
	}

	@RequestMapping(value = "/submitSearch", params={"save"}, method = RequestMethod.POST)
	public ModelAndView save(@ModelAttribute final SearchWrapper searchWrapper) {
		//TODO: Salvar pesquisa
		return listSegmentations();
	}

	public ModelAndView sendToSearch(final SearchWrapper searchWrapper) {
		final ModelAndView modeView = new ModelAndView("/segmentation/search");
		modeView.addObject("searchWrapper", searchWrapper);
		return modeView;
	}

}
