package br.com.cabtecgti.prova.agenda.controllers;

import java.util.Date;



import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.primefaces.event.CloseEvent;
import org.primefaces.event.ToggleEvent;

import br.com.cabtecgti.prova.agenda.entities.Contato;
import br.com.cabtecgti.prova.agenda.entities.Recado;
import br.com.cabtecgti.prova.agenda.repositories.FiltroSearch;
import br.com.cabtecgti.prova.agenda.repositories.ResultList;

@Named
@br.com.cabtecgti.faces.bean.ViewScoped
public class RecadosAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	

	private Contato contatoSelecionado;
	private Long idContato;
	private boolean click;
	private Recado recadoSelect;
	private HtmlDataTable table;

	

	public RecadosAction() {
		super("recados");
		setSelectListener(new ContatoSelected());
		setFiltro(new FiltroSearch());
	}

	@Override
	protected ResultList<Recado> doSearch() {
		getFiltro().setNomeCampo("contato.id");
		getFiltro().setPrimeiroRegistro(this.getOffset());
		getFiltro().setQuantidadeRegistros(this.getLimit());
		getFiltro().setCampo(this.contatoSelecionado.getId().toString());
		return this.getRepoRecado().searchFiltro(getFiltro());
		
	}

	@Override
	public void create() {
		setEntity(new Recado());
	}

	@Override
	public void edit(final Object id) {
		final Recado recado = this.getRepoRecado().findById((Long) id);
		setEntity(recado);
		if (recado == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Registro não encontrado. ID", null));
		}
	}

	@Override
	public void save() {
		System.out.println("Entrou no Save " + ((Recado) getEntity()).getTexto());
		final Recado entity = (Recado) getEntity();
		String msg = null;
		if (entity.getId() != null && entity.getId() != 0) {
			setEntity(this.getRepoRecado().update(entity));
			msg = "Registro salvo com sucesso.";
		} else {
			entity.setHorario(new Date());
			entity.setIndLido(false);
			entity.setContato(getRepoContato().findById(this.getIdContato()));
			setEntity(this.getRepoRecado().create(entity));
			setEntity(new Recado());
			msg = "Registro criado com sucesso.";
		}

		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
	}

	@Override
	public void delete() {
		Recado entity = (Recado) getEntity();
		this.getRepoRecado().delete(entity);
		setEntity(null);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Deletado Com Sucesso!.", null));
	}

	
	/** Início Bloco Contato Selecionado
	 * Recebe o Id do contato selecionado;
	 * cria contato selecionado através do id;
	 * Atribui Objeto Contato Selecionado;
	 * 
	 * 
	 * */
	private class ContatoSelected implements SelectListener {
		@Override
		public void selected(final Object selected) {
			final Contato contato = (Contato) selected;
			RecadosAction.this.navigateToEdit("recados-edit.xhtml", contato.getId());
		}

		@Override
		public void selectedGoRecado(Object selected) {
			final Contato contato = (Contato) selected;
			RecadosAction.this.navigateToEdit("recados-edit.xhtml", contato.getId());
			
		}
	}
	
	public void getIdContatoSelected() {
		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}
		if (this.idContato != null && this.idContato != 0) {
			System.out.println("Id Selecionado" + this.idContato);
			this.findContato(this.idContato);
			System.out.println("Id Selecionado do contato " + getContatoSelecionado().getNome());
		}
	}
	
	public void findContato(final Object id) {
		final Contato contato = getRepoContato().findById((Long) id);
		setContatoSelecionado(contato);
		setEntity(new Recado());
		if (contato == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Registro não encontrado.", null));
		}
	}
	

	/** Fim do Bloco Contato Selecionado
	 * 
	 * 
	 * 
	 * */
	
	public void cliked(){
		Recado recadoSelecionado = (Recado) this.table.getRowData();
		this.recadoSelect = recadoSelecionado;
		if(!this.recadoSelect.getIndLido()){
		this.recadoSelect.setIndLido(true);
		setEntity(this.recadoSelect);
		this.save();
		}
	}
	
	 public void onClose(CloseEvent event) {
	        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Panel Closed", "Closed panel id:'" + event.getComponent().getId() + "'");
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        this.click = false;
	    }
	     
	    public void onToggle(ToggleEvent event) {
	        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, event.getComponent().getId() + " toggled", "Status:" + event.getVisibility().name());
	        FacesContext.getCurrentInstance().addMessage(null, message);
	    }
	
	public Contato getContatoSelecionado() {
		return contatoSelecionado;
	}

	public void setContatoSelecionado(Contato contatoSelecionado) {
		this.contatoSelecionado = contatoSelecionado;
	}

	public Long getIdContato() {
		return idContato;
	}

	public void setIdContato(Long idContato) {
		this.idContato = idContato;
	}

	public boolean isClick() {
		return click;
	}

	public void setClick(boolean click) {
		this.click = click;
	}

	

	public HtmlDataTable getTable() {
		return table;
	}

	public void setTable(HtmlDataTable table) {
		this.table = table;
	}

	public Recado getRecadoSelect() {
		return recadoSelect;
	}

	public void setRecadoSelect(Recado recadoSelect) {
		this.recadoSelect = recadoSelect;
	}


}
