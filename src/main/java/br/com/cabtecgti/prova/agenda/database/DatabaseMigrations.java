package br.com.cabtecgti.prova.agenda.database;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.sql.DataSource;

/**
 * Executa a rotina de migrations para atualizar/criar estrutura do banco de dados.
 * 
 * @author Cabtec GTI
 *
 */
@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class DatabaseMigrations implements Serializable {

    private static final long serialVersionUID = 1L;
    @Resource(name = "AGENDA_DS")
    private DataSource ds;

    @PostConstruct
    @TransactionAttribute(TransactionAttributeType.NEVER)
    public void init() {
        new Migrations(ds).migrate();
    }
}