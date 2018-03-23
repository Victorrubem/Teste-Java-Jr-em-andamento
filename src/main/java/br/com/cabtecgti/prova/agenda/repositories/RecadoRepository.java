package br.com.cabtecgti.prova.agenda.repositories;



import javax.ejb.DependsOn;
import javax.ejb.Stateless;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import br.com.cabtecgti.prova.agenda.entities.Recado;

/**
 * Repositório de recados.
 * 
 * @author Cabtec GTI
 *
 */
@Stateless
@DependsOn("DatabaseMigrations")
public class RecadoRepository extends AbstractRepository<Recado, Long> {

    public RecadoRepository() {
        super(Recado.class);
    }
    
    @Override
    public Criteria searchCampo(Criteria criteria, FiltroSearch filtro){
    	criteria.add(Restrictions.like(filtro.getNomeCampo(), Long.parseLong(filtro.getCampo())));
    	//criteria.add(Restrictions.ilike(filtro.getNomeCampo(), filtro.getCampo(),MatchMode.START));
		return criteria;
    }
   
    
}