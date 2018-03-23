package br.com.cabtecgti.prova.agenda.controllers;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

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
	private Object entity;

	/**
	 * Guarda o resultado de uma pesquisa.
	 */
	private Object result;
	private Integer offset = 0;
	private Integer limit = SEARCH_LIMIT_DEFAULT;
	private Object selected;
	private SelectListener selectListener;
	private FiltroSearch filtro;
	@EJB
	private RecadoRepository repoRecado;
	@EJB
	private ContatoRepository repoContato;

	/**
	 * Método disparado quando um item é selecionado na lista. Se houver um
	 * {@link #listener} configurado, o mesmo é notificado.
	 */
	public void select() {
		if (selectListener != null) {
			selectListener.selected(selected);
		}
	}
	
	public void selectGoRecado() {
		if (selectListener != null) {
			selectListener.selectedGoRecado(selected);
		}
	}

	/**
	 * Construtor que recebe o identificador de menu relativo à página em
	 * exibição.
	 * 
	 * @param menu
	 */
	public BaseAction(final String menu) {
		this.menu = menu;
	}

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

	// Alterei o if com && getId() != 0, recebia 0 como prâmetro
	public void editOrCreate() {
		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}
		if (getId() != null && getId() != 0) {
			edit(getId());
		} else {
			create();
		}
	}

	public void search() {
		final ResultList<?> resultList = doSearch();
		final Object result = getResult();
		if (result instanceof LazyDataModel) {
			final LazyDataModel dataModel = (LazyDataModel) result;
			dataModel.updateResult(resultList);
			
		} else {
			
			setResult(new LazyDataModel(resultList));
		}
	}

	protected ResultList<?> doSearch() {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "MÉTODO AGUARDANDO IMPLEMENTAÇÃO.", null));

		return new ResultList<Object>(Collections.emptyList(), 0, offset, limit);
	}

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

		public void updateResult(final ResultList<?> queryResult) {
			this.queryResult = queryResult;

			setWrappedData(queryResult.getResult());
			setPageSize(queryResult.getLimit());
			setRowCount(queryResult.getTotalCount());
			setRowIndex(getPageSize() > 0 ? queryResult.getOffset() : -1);
		}

		@SuppressWarnings("unchecked")
		@Override
		public List<Object> load(final int first, final int pageSize, final String sortField, final SortOrder sortOrder,
				final Map<String, String> filters) {

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
				final Map<String, String> filters) {

			throw new UnsupportedOperationException("Lazy loading for multiSort is not implemented.");
		}
	}

	/**
	 * Método a ser implementado na classe descendente. Apresenta formulário
	 * vazio para cadastrar um novo registro.
	 */
	public void create() {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "MÉTODO AGUARDANDO IMPLEMENTAÇÃO.", null));
	}

	/**
	 * Método a ser implementado na classe descendente. Recupera uma entidade
	 * para edição.
	 * 
	 * @param id
	 *            ID da entidade
	 */
	public void edit(final Object id) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "MÉTODO AGUARDANDO IMPLEMENTAÇÃO.", null));
	}

	public void delete() {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "MÉTODO AGUARDANDO IMPLEMENTAÇÃO.", null));
	}

	public void save() {

		System.out.println("Entrou no Save");
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "MÉTODO AGUARDANDO IMPLEMENTAÇÃO.", null));
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

	public Object getEntity() {
		return entity;
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

	protected void setSelectListener(final SelectListener selectListener) {
		this.selectListener = selectListener;
	}

	public void clear() {
		this.result = null;
	}

	public String getMenu() {
		return menu;
	}

	protected void setEntity(final Object entity) {
		this.entity = entity;
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

}