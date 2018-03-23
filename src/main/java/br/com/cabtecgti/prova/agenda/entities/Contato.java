package br.com.cabtecgti.prova.agenda.entities;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Dados do contato.
 * 
 * @author Cabtec GTI
 */
@Entity
@Table(name = "contatos")
@SequenceGenerator(name = "global_seq", sequenceName = "global_seq")
public class Contato extends MasterEntity {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(min = 3, max = 140)
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotNull
    @Min(0)
    @Max(Integer.MAX_VALUE)
    @Column(name = "ramal", nullable = false)
    private Integer ramal;

    public Contato() {
        // noop
    }

    public Contato(final Long id, final Integer version) {
        super(id, version);
    }

    public Contato(final Long id, final String nome, final Integer ramal, final Integer version) {
        this(id, version);
        this.nome = nome;
        this.ramal = ramal;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Contato) {
            final Contato outro = (Contato) obj;
            return Objects.equals(nome, outro.nome);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(final String nome) {
        this.nome = nome;
    }

    public Integer getRamal() {
        return ramal;
    }

    public void setRamal(final Integer ramal) {
        this.ramal = ramal;
    }

}