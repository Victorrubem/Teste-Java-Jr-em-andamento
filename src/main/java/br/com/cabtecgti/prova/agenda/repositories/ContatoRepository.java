package br.com.cabtecgti.prova.agenda.repositories;

import java.util.Collections;
import java.util.List;

import javax.ejb.DependsOn;
import javax.ejb.Stateless;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import br.com.cabtecgti.prova.agenda.entities.Contato;
import br.com.cabtecgti.prova.agenda.entities.Recado;

/**
 * Repositório de contatos.
 * 
 * @author Cabtec GTI
 *
 */
@Stateless
@DependsOn("DatabaseMigrations")
public class ContatoRepository extends AbstractRepository<Contato, Long> {

    public ContatoRepository() {
        super(Contato.class);
    }
    
    @Override
    public Criteria searchCampo(Criteria criteria, FiltroSearch filtro){
    	criteria.add(Restrictions.ilike(filtro.getNomeCampo(), filtro.getCampo(),MatchMode.START));
		return criteria;
    }
    
    public List<Recado> recadosNaoLidos(final Long idContato) {
        return Collections.emptyList();
    }
    public List<Recado> recadosLidos(final Long idContato) {
        return Collections.emptyList();
    }
}