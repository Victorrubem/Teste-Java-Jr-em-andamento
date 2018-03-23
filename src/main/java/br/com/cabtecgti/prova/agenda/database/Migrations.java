package br.com.cabtecgti.prova.agenda.database;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Classe responsável pela atualização do banco de dados.
 * 
 * @author roberto.badaro
 */
public class Migrations {

    private static final Logger logger = LoggerFactory.getLogger(Migrations.class);

    private DataSource ds;

    public Migrations(final DataSource ds) {
        super();
        this.ds = ds;
    }

    public void migrate() {
        logger.info("Verificando Database Upgrade (Flyway)");
        final Date now = new Date();
        final Map<String, String> placeholders = new HashMap<>(2);
        placeholders.put("migration_user", "_MIGRATION_");
        placeholders.put("sysdate", new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(now));

        final Flyway flyway = new Flyway();
        flyway.setPlaceholders(placeholders);
        flyway.setBaselineOnMigrate(true);
        flyway.setBaselineVersionAsString("0.0.0");
        flyway.setBaselineDescription("Banco preexistente");
        flyway.setValidateOnMigrate(false); // nao verifica checksum
        flyway.setDataSource(ds);

        final MigrationInfo info = flyway.info().current();
        logger.info(String.format("Versão corrente: %s",
            (info != null ? (info.getVersion().getVersion() + "/" + info.getState()) : "BANCO ZERADO")));

        if (info != null && MigrationState.FAILED == info.getState()) {
            logger.info("Upgrade anterior falhou. REPARANDO...");
            flyway.repair();
        }
        final int cnt = flyway.migrate();

        logger.info("Database Upgrade finalizado. " + cnt + " scripts executados.");
    }

}