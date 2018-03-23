package br.com.cabtecgti.prova.agenda.entities;

import java.util.Date;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Recado anotado para o contato.
 * 
 * @author Cabtec GTI
 */
@Entity
@Table(name = "recados")
@SequenceGenerator(name = "global_seq", sequenceName = "global_seq")
public class Recado extends MasterEntity {

    private static final long serialVersionUID = 1L;

    @NotNull
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = { CascadeType.DETACH, CascadeType.REFRESH, CascadeType.REMOVE })
    @JoinColumn(name = "idContato", nullable = false)
    private Contato contato;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "horario", nullable = false)
    private Date horario;

    @NotNull
    @Size(min = 3, max = 255)
    @Column(name = "texto", nullable = false)
    private String texto;

    @NotNull
    @Column(name = "indLido", nullable = false)
    private Boolean indLido;

    public Recado() {
        // noop
    }

    public Recado(final Long id, final Integer version) {
        super(id, version);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Recado) {
            final Recado outro = (Recado) obj;
            return Objects.equals(contato, outro.contato) && Objects.equals(horario, outro.horario);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(contato, horario);
    }

    public Contato getContato() {
        return contato;
    }

    public void setContato(final Contato contato) {
        this.contato = contato;
    }

    public Date getHorario() {
        return horario;
    }

    public void setHorario(final Date horario) {
        this.horario = horario;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(final String texto) {
        this.texto = texto;
    }

    public Boolean getIndLido() {
        return indLido;
    }

    public void setIndLido(final Boolean indLido) {
        this.indLido = indLido;
    }

}