package ca.uqam.casinotopia.observateur;

import java.util.HashSet;
import java.util.Set;

public class BaseSujet implements Sujet {
	
	Set<Observateur> observateurs = new HashSet<Observateur>();
	
	Sujet sujetConcret;
	
	public BaseSujet(Sujet sujetConcret) {
		this.sujetConcret = sujetConcret;
	}

	@Override
	public void ajouterObservateur(Observateur obs) {
		this.observateurs.add(obs);
	}

	@Override
	public void retirerObservateur(Observateur obs) {
		this.observateurs.remove(obs);
	}

	@Override
	public boolean estObserveePar(Observateur obs) {
		return this.observateurs.contains(obs);
	}

	@Override
	public void notifierObservateur() {
		for(Observateur obs : this.observateurs) {
			obs.update(sujetConcret);
		}
	}

}
