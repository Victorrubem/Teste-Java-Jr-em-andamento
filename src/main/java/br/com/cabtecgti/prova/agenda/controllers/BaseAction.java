package br.com.cabtecgti.prova.agenda.controllers;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import br.com.cabtecgti.prova.agenda.entities.Contato;
import br.com.cabtecgti.prova.agenda.entities.MasterEntity;
import br.com.cabtecgti.prova.agenda.entities.Recado;
import br.com.cabtecgti.prova.agenda.repositories.ContatoRepository;
import br.com.cabtecgti.prova.agenda.repositories.FiltroSearch;
import br.com.cabtecgti.prova.agenda.repositories.RecadoRepository;
import br.com.cabtecgti.prova.agenda.repositories.ResultList;

/**
 * Bean ancestral para a aplicação.
 * 
 * @author Cabtec GTI
 *
 */
public abstract class BaseAction implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final int SEARCH_LIMIT_DEFAULT = 10;
	private final String menu;
	private Long id;
	private MasterEntity entity;
	
	private List<MasterEntity> objetosBanco;
	
	
	/**
	 * Guarda o resultado de uma pesquisa.
	 */
	private Object result;
	private Integer offset = 0;
	private Integer limit = SEARCH_LIMIT_DEFAULT;
	private Object selected;
	private FiltroSearch filtro;
	
	@EJB
	private RecadoRepository repoRecado;
	@EJB
	private ContatoRepository repoContato;

	/**
	 * Construtor que recebe o identificador de menu relativo à página em
	 * exibição.
	 * 
	 * @param menu
	 */
	public BaseAction(final String menu) {
		this.menu = menu;
	}



	// Alterei o if com && getId() != 0, recebia 0 como parâmetro
	// Método que atribui o tipo da entidade(entity) à ser alterada ou incluida
	public void editOrCreate() {
		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}
		if (this.getId() != null && this.getId() != 0) {
			edit(this.getId());
		} else {
			create();
		}
	}

	public void search() {
		final ResultList<?> resultList = doSearch();
		final Object result = this.getResult();
		if (result instanceof LazyDataModel) {
			final LazyDataModel dataModel = (LazyDataModel) result;
			dataModel.updateResult(resultList);
			
		} else {
			
			setResult(new LazyDataModel(resultList));
		}
	}
	

//Bloco de Navegação de Páginas
	// Navegar para o endereço fornecido
	protected void navigateTo(final String outcome) {
		final FacesContext ctx = FacesContext.getCurrentInstance();
		ctx.getApplication().getNavigationHandler().handleNavigation(ctx, null, outcome.toString());
	}

	// Retorna o endereço
	public void navBack() {
		final StringBuilder outcome = new StringBuilder(menu).append(".xhtml");
		navigateTo(outcome.toString());
	}

	public String goCreate() {
		return new StringBuilder(menu).append("-edit.xhtml?faces-redirect=true").toString();
	}

	protected void navigateToEdit(final String page, final Long id) {
		final StringBuilder outcome = new StringBuilder(page).append("?id=").append(id).append("&faces-redirect=true");
		navigateTo(outcome.toString());
	}
	
	public void goEdit(SelectEvent event) {
		final MasterEntity entity = (MasterEntity) event.getObject();
		final StringBuilder outcome = new StringBuilder(menu).append("-edit.xhtml").append("?id=").append(entity.getId()).append("&faces-redirect=true");
		navigateTo(outcome.toString());
    }
	
//Fim do Bloco de Navegação
	

	
	//
	// Inner classes
	//

	public class LazyDataModel extends org.primefaces.model.LazyDataModel<Object> {

		private static final long serialVersionUID = 1L;

		private int currentRow;
		private ResultList<?> queryResult;
		

		public LazyDataModel() {
			this.setPageSize(0);
			this.setRowCount(0);
			this.setRowIndex(-1);
		}

		public LazyDataModel(final ResultList<?> queryResult) {
			updateResult(queryResult);
		}

	
		
		@SuppressWarnings("unchecked")
		public void updateResult(final ResultList<?> queryResult) {
			this.queryResult = queryResult;
			
			setWrappedData(queryResult.getResult());
			setPageSize(queryResult.getLimit());
			setRowCount(queryResult.getTotalCount());
			setRowIndex(getPageSize() > 0 ? queryResult.getOffset() : -1);
			setObjetosBanco((List<MasterEntity>) queryResult.getResult());
		}

		
		@SuppressWarnings("unchecked")
		@Override
		public List<Object> load(final int first, final int pageSize, final String sortField, final SortOrder sortOrder,
				final Map<String, Object> filters) {

			offset = first;
			limit = pageSize;

			if (currentRow != first) {
				currentRow = first;
				search();
			}

			return queryResult != null ? (List<Object>) queryResult.getResult() : null;
		}

		@Override
		public List<Object> load(final int first, final int pageSize, final List<SortMeta> multiSortMeta,
				final Map<String, Object> filters) {

			throw new UnsupportedOperationException("Lazy loading for multiSort is not implemented.");
		}
		
		
		 @Override
		 public Object getRowData(String rowKey) {
			 for(Object contato : getObjetosBanco()) {
		            if(((MasterEntity) contato).getId() == Long.parseLong(rowKey)){
		                return contato;
		            }
		        }
			return null;
		 }
		 
		 @Override
		 public Object getRowKey(Object objeto) {
			 return ((MasterEntity) objeto).getId();
		 }
		
	}
	
	
	
	/**
	 * Método a ser implementado na classe descendente.
	 * 
	 */
	protected abstract ResultList<?> doSearch();

	/**
	 * Método a ser implementado na classe descendente. Apresenta formulário
	 * vazio para cadastrar um novo registro.
	 */
	public abstract void create();

	/**
	 * Método a ser implementado na classe descendente. Recupera uma entidade
	 * para edição.
	 * 
	 * @param id
	 *            ID da entidade
	 */
	public abstract void edit(final Object id);

	public abstract void delete();

	public abstract void save();
	
	
	/**
	 * Método verifica qual o tipo (Contato/Recado) logo em seguida salva/altera. 
	 */
	public void salva() {
		
		final MasterEntity entity =  this.getEntity();
		String msg = null;
		
		if (entity.getId() != null && entity.getId()!= 0) {
			
			if(entity instanceof Contato){
				final Contato contato = (Contato) entity;
				this.setEntity(this.getRepoContato().update(contato));
			}else{
				if(entity instanceof Recado){
					final Recado recado = (Recado) entity;
					this.setEntity(this.getRepoRecado().update(recado));
				}
			}
			msg = "Registro salvo com sucesso.";
			
		} else {
			
			if(entity instanceof Contato){
				final Contato contato = (Contato) entity;
				this.setEntity(this.getRepoContato().create(contato));
			}else{
				if(entity instanceof Recado){
					final Recado recado = (Recado) entity;
					this.setEntity(this.getRepoRecado().create(recado));
				}
			}
			msg = "Registro criado com sucesso.";
		}
		this.setEntity(null);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
	}
	

	/**
	 * Retorna o resultado de uma pesquisa.
	 * 
	 * @return o resultado de uma pesquisa.
	 */
	public Object getResult() {
		return result;
	}

	protected void setResult(final Object result) {
		this.result = result;
	}

	public Object getSelected() {
		return selected;
	}

	public void setSelected(final Object selected) {
		this.selected = selected;
	}

	public MasterEntity getEntity() {
		return entity;
	}
	
	protected void setEntity(final MasterEntity entity) {
		this.entity = entity;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		System.out.println("Adicionou ID");
		this.id = id;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(final Integer offset) {
		this.offset = offset;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(final Integer limit) {
		this.limit = limit;
	}

	public void clear() {
		this.result = null;
	}

	public String getMenu() {
		return menu;
	}


	public FiltroSearch getFiltro() {
		return filtro;
	}

	public void setFiltro(FiltroSearch filtro) {
		this.filtro = filtro;
	}

	public RecadoRepository getRepoRecado() {
		return repoRecado;
	}

	public void setRepoRecado(RecadoRepository repo) {
		this.repoRecado = repo;
	}

	public ContatoRepository getRepoContato() {
		return repoContato;
	}

	public void setRepoContato(ContatoRepository repoContato) {
		this.repoContato = repoContato;
	}

	
	public List<MasterEntity> getObjetosBanco() {
		return this.objetosBanco;
	}

	
	public void setObjetosBanco(List<MasterEntity> objetosBanco) {
		this.objetosBanco = objetosBanco;
	}

	

}