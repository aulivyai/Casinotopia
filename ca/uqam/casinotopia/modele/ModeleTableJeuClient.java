package ca.uqam.casinotopia.modele;

import java.util.HashMap;
import java.util.Map;

import ca.uqam.casinotopia.Case;
import ca.uqam.casinotopia.observateur.BaseSujet;
import ca.uqam.casinotopia.observateur.Observateur;

public class ModeleTableJeuClient implements Modele {
	
private Map<Case, Map<Integer, Integer>> cases = new HashMap<Case, Map<Integer, Integer>>();
	
	private BaseSujet sujet = new BaseSujet(this);
	
	public ModeleTableJeuClient() {
		this.initialiserCases();
	}
	
	private void initialiserCases() {
		this.ajouterCase(1, "noire", false);
		this.ajouterCase(2, "rouge", true);
		this.ajouterCase(3, "rouge", false);
		this.ajouterCase(4, "noire", true);
		this.ajouterCase(5, "noire", false);
		this.ajouterCase(6, "rouge", true);
	}
	
	public void ajouterCase(int numero, String couleur, boolean boolPaires) {
		this.cases.put(new Case(numero, couleur, boolPaires), new HashMap<Integer, Integer>());
	}
	
	public void updateTableJeu(Map<Case, Map<Integer, Integer>> cases) {
		this.cases = cases;
	}
	
	public Map<Case, Map<Integer, Integer>> getCases() {
		return this.cases;
	}
	
	@Override
	public void ajouterObservateur(Observateur obs) {
		this.sujet.ajouterObservateur(obs);
	}

	@Override
	public void retirerObservateur(Observateur obs) {
		this.sujet.retirerObservateur(obs);
	}

	@Override
	public boolean estObserveePar(Observateur obs) {
		return this.sujet.estObserveePar(obs);
	}

	@Override
	public void notifierObservateur() {
		this.sujet.notifierObservateur();
	}

}
