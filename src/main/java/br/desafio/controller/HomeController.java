package br.desafio.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Responsável por receber as requisições no path raiz "/" e direciona para a página inicial.
 */
@RestController
public class HomeController {

	@RequestMapping("/")
	public ModelAndView home() {
        return new ModelAndView("index");
	}

}
