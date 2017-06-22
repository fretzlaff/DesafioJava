package br.desafio.controller;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.data.repository.CrudRepository;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Abstração da operações comuns entre os controllers
 *
 * @param <T> Tipo do repositório de dados
 * @param <S> Tipo da entidade de dados
 */
public abstract class AbstractController<T extends CrudRepository<S, Serializable>, S> {

	@RequestMapping("/")
	public ModelAndView list() {
		final ModelAndView modeView = new ModelAndView(getRootPath() + "/list");
		modeView.addObject(getRootPath() + "s", getEntityRepos().findAll());
		return modeView;
	}

    public ModelAndView add(final S entity) {
    	return add(entity, null);
    }

	@GetMapping("/add")
    public ModelAndView add(final S entity, final HttpServletRequest request) {
        final ModelAndView mv = new ModelAndView(getRootPath() + "/add");
        mv.addObject(getRootPath(), entity);
        return mv;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") final Long id) {
        return add(getEntityRepos().findOne(id));
    }

    @GetMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable("id") final Long id) {
    	getEntityRepos().delete(id);
        return list();
    }

    @PostMapping("/save")
    public ModelAndView save(@Valid final S entity, final BindingResult result, final HttpServletRequest request) {
        if(result.hasErrors()) {
            return add(entity);
        }
        getEntityRepos().save(entity);
        return list();
    }


    public abstract T getEntityRepos();

    public abstract String getRootPath();

}
