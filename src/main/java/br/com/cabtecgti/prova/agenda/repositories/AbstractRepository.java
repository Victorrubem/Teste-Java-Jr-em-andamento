package br.com.cabtecgti.prova.agenda.repositories;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;


import br.com.cabtecgti.prova.agenda.entities.MasterEntity;

/**
 * Ancestral para as classes repositório do projeto. Já recebe o EntityManager
 * por injeção.
 * 
 * @author Cabtec GTI
 *
 * @param <E>
 *            Entidade
 * @param <ID>
 *            Tipo do ID da entidade
 */
public abstract class AbstractRepository<E extends MasterEntity, ID extends Serializable> {

	@PersistenceContext(unitName = "agenda")
	protected EntityManager entityManager;

	private Class<E> entityClass;

	public AbstractRepository(final Class<E> entityClass) {
		this.entityClass = entityClass;
	}

	/**
	 * Insere um novo registro.
	 * 
	 * @param entity
	 *            a entidade a ser salva.
	 * @return A entidade criada.
	 */
	public E create(final E entity) {

		entityManager.persist(entity);
		return entity;
	}

	/**
	 * Atualiza uma entidade existente.
	 * 
	 * @param entity
	 *            a entidade a ser atualizada.
	 * @return A entidade atualizada.
	 */
	public E update(final E entity) {
		return entityManager.merge(entity);
	}

	/**
	 * Remove a entidade.
	 * 
	 * @param entity
	 *            A entidade a ser removida.
	 */
	public void delete(final E entity) {
		final E toDelete = entityManager.merge(entity);
		entityManager.remove(toDelete);

	}

	/**
	 * Recupera uma entidade pelo ID.
	 * 
	 * @param id
	 *            Id da entidade a ser recuperada
	 * @return A entidade. Nulo se não existir entidade para o ID informado.
	 */
	public E findById(final ID id) {
		return entityManager.find(entityClass, id);
	}

	/**
	 * Executa pesquisa de entidades usando Filtros.
	 * 
	 * @param filter
	 * @param offset
	 * @param limit
	 * @return
	 */
	public ResultList<E> searchFiltro(FiltroSearch filtro) {
		final int count = quantidadeFiltrados(filtro);
		System.out.println(count);
		if (count > 0) {
			
			final List<E> result = filtrados(filtro);

			return new ResultList<>(result, count, filtro.getPrimeiroRegistro(), filtro.getQuantidadeRegistros());
		} else {
		
			return new ResultList<>(Collections.emptyList(), count, filtro.getPrimeiroRegistro(),filtro.getQuantidadeRegistros());
		}
	}

	public int quantidadeFiltrados(FiltroSearch filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);

		criteria.setProjection(Projections.rowCount());

		return ((Number) criteria.uniqueResult()).intValue();
	}

	private Criteria criarCriteriaParaFiltro(FiltroSearch filtro) {
		Session session = entityManager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(entityClass);
	
		if (filtro.getCampo() != null) {
			
			searchCampo(criteria, filtro);
		} 
		return criteria;
	}

	/**
	 * Método a ser implementado na classe descendente. Retorna o campo a ser
	 * pesquisado
	 * 
	 * @param criteria
	 *            Criteria
	 */
	public Criteria searchCampo(Criteria criteria, FiltroSearch filtro) {
		return criteria;

	}

	@SuppressWarnings("unchecked")
	public List<E> filtrados(FiltroSearch filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);

		criteria.setFirstResult(filtro.getPrimeiroRegistro());
		criteria.setMaxResults(filtro.getQuantidadeRegistros());

		if (filtro.isAscendente() && filtro.getPropriedadeOrdenacao() != null) {
			criteria.addOrder(Order.asc(filtro.getPropriedadeOrdenacao()));
		} else if (filtro.getPropriedadeOrdenacao() != null) {
			criteria.addOrder(Order.desc(filtro.getPropriedadeOrdenacao()));
		}

		return criteria.list();
	}

	public Class<E> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<E> entityClass) {
		this.entityClass = entityClass;
	}
}