package br.com.cabtecgti.prova.agenda.controllers;

/**
 * Listener para receber a notificação de item selecionado a partir de uma
 * action de seleção.
 * 
 * @author Cabtec GTI
 * 
 */
public interface SelectListener {

	/**
	 * Recebe a notificação de que um elemento foi selecionado.
	 * 
	 * @param selected
	 */
    void selected(Object selected);
    void selectedGoRecado(Object selected);
}