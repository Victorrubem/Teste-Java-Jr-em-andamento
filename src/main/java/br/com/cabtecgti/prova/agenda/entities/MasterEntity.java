package br.com.cabtecgti.prova.agenda.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * Classe ancentral para as entidades do projeto. Já possui o mapeamento do ID e do VERSION.
 * 
 * @author Cabtec GTI
 */
@MappedSuperclass
public abstract class MasterEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "global_seq", strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Version
    @Column(name = "gti_version", nullable = false)
    private Integer version;

    public MasterEntity() {
        // noop
    }

    public MasterEntity(final Long id, final Integer version) {
        super();
        this.id = id;
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(final Integer version) {
        this.version = version;
    }

}