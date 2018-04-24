package br.com.cabtecgti.prova.agenda.controllers;

import javax.inject.Named;

import br.com.cabtecgti.prova.agenda.repositories.ResultList;

@Named
@br.com.cabtecgti.faces.bean.ViewScoped
public class HomeAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    public HomeAction() {
        super("home");
    }

	@Override
	protected ResultList<?> doSearch() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void create() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void edit(Object id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}

}
