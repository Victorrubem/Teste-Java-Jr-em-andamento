package br.com.cabtecgti.prova.agenda.controllers;

import java.util.List;
import java.util.Map;


import org.primefaces.model.SortOrder;


public class LazyDataModel extends org.primefaces.model.LazyDataModel<Object> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int primeiro;
	private int tamanho;
	private int linhaAtual;

	public LazyDataModel() {
		// TODO Auto-generated constructor stub
	}

	
	public List<Object> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {
		primeiro = first;
		tamanho = pageSize;

		if (linhaAtual != first) {
			linhaAtual = first;
			
			
		}
return null;
		//return queryResult != null ? (List<Object>) queryResult.getResult() : null;
		

		
	}

	public int getPrimeiro() {
		return primeiro;
	}

	public void setPrimeiro(int primeiro) {
		this.primeiro = primeiro;
	}

	public int getTamanho() {
		return tamanho;
	}

	public void setTamanho(int tamanho) {
		this.tamanho = tamanho;
	}

	public int getLinhaAtual() {
		return linhaAtual;
	}

	public void setLinhaAtual(int linhaAtual) {
		this.linhaAtual = linhaAtual;
	}

}
