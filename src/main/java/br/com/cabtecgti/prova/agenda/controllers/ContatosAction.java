package br.com.cabtecgti.prova.agenda.controllers;


import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;


import br.com.cabtecgti.prova.agenda.entities.Contato;
import br.com.cabtecgti.prova.agenda.repositories.FiltroSearch;
import br.com.cabtecgti.prova.agenda.repositories.ResultList;

@Named
@br.com.cabtecgti.faces.bean.ViewScoped
public class ContatosAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private List<Contato> filtrados;
	

	public ContatosAction() {
		super("contatos");
		setFiltro(new FiltroSearch());
	}

	@Override
	protected ResultList<Contato> doSearch() {
		this.getFiltro().setNomeCampo("nome");
		this.getFiltro().setPrimeiroRegistro(this.getOffset());
		this.getFiltro().setQuantidadeRegistros(this.getLimit());
		return this.getRepoContato().searchFiltro(getFiltro());
	}
	
	

	@Override
	public void create() {
		setEntity(new Contato());
	}

	@Override
	public void edit(final Object id) {
		final Contato contato = this.getRepoContato().findById((Long) id);
		setEntity(contato);
		if (contato == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Registro não encontrado.", null));
		}
	}

	
	
	@Override
	public void delete() {
	
		Contato entity = (Contato) getEntity();
		this.getRepoContato().delete(entity);
		setEntity(null);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Deletado Com Sucesso!.", null));
		
	}
	
	
	public List<Contato> getFiltrados() {
		return filtrados;
	}

	public void setFiltrados(List<Contato> filtrados) {
		this.filtrados = filtrados;
	}

}
