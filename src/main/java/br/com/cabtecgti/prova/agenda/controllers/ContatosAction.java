package br.com.cabtecgti.prova.agenda.controllers;

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
	

	public ContatosAction() {
		super("contatos");
		setSelectListener(new ContatoSelected());
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
	public void save() {
		final Contato entity = (Contato) getEntity();
		String msg = null;
		if (entity.getId() != null && entity.getId()!= 0) {
			setEntity(this.getRepoContato().update(entity));
			msg = "Registro salvo com sucesso.";
		} else {
			setEntity(this.getRepoContato().create(entity));
			msg = "Registro criado com sucesso.";
		}
		setEntity(null);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
	}
	
	@Override
	public void delete() {
	
		Contato entity = (Contato) getEntity();
		this.getRepoContato().delete(entity);
		setEntity(null);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Deletado Com Sucesso!.", null));
		
	}
	
	private class ContatoSelected implements SelectListener {
		@Override
		public void selected(final Object selected) {
			final Contato contato = (Contato) selected;
			ContatosAction.this.navigateToEdit("contatos-edit.xhtml", contato.getId());
		}
		@Override
		public void selectedGoRecado(Object selected) {
			final Contato contato = (Contato) selected;
			ContatosAction.this.navigateToEdit("recados-edit.xhtml", contato.getId());
			
		}
		
	}

}
