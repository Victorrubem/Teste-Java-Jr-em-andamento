package br.com.cabtecgti.prova.agenda.repositories;

import java.io.Serializable;



public class FiltroSearch implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long idEntity;
	
	private int primeiroRegistro;
	private int quantidadeRegistros;
	private String propriedadeOrdenacao;
	private boolean ascendente;
	private String campo;
	private String nomeCampo;
	
	

	public int getPrimeiroRegistro() {
		return primeiroRegistro;
	}

	public void setPrimeiroRegistro(int primeiroRegistro) {
		this.primeiroRegistro = primeiroRegistro;
	}

	public int getQuantidadeRegistros() {
		return quantidadeRegistros;
	}

	public void setQuantidadeRegistros(int quantidadeRegistros) {
		this.quantidadeRegistros = quantidadeRegistros;
	}

	public String getPropriedadeOrdenacao() {
		return propriedadeOrdenacao;
	}

	public void setPropriedadeOrdenacao(String propriedadeOrdenacao) {
		this.propriedadeOrdenacao = propriedadeOrdenacao;
	}

	public boolean isAscendente() {
		return ascendente;
	}

	public void setAscendente(boolean ascendente) {
		this.ascendente = ascendente;
	}

	public Long getIdEntity() {
		return idEntity;
	}

	public void setIdEntity(Long idEntity) {
		this.idEntity = idEntity;
	}

	public String getCampo() {
		return campo;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

	public String getNomeCampo() {
		return nomeCampo;
	}

	public void setNomeCampo(String nomeCampo) {
		this.nomeCampo = nomeCampo;
	}


}
