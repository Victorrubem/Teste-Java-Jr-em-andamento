package br.com.cabtecgti.prova.agenda.controllers;

import javax.inject.Named;

@Named
@br.com.cabtecgti.faces.bean.ViewScoped
public class HomeAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    public HomeAction() {
        super("home");
    }

}
