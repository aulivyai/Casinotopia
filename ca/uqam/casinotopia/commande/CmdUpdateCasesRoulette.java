package ca.uqam.casinotopia.commande;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import ca.uqam.casinotopia.Case;
import ca.uqam.casinotopia.controleur.Controleur;
import ca.uqam.casinotopia.controleur.ControleurRouletteClient;

public class CmdUpdateCasesRoulette implements CommandeClientControleurRoulette {
	
	/**
	 * Map<idJoueur, Map<CaseMisee, NbrJetonsMises>>
	 */
	//private Map<Integer, Map<Case, Integer>> mises = new HashMap<Integer, Map<Case, Integer>>();
	
	/**
	 * Map<Case, Map<idJoueur, nbrJetonsMises>>
	 */
	private Map<Case, Map<Integer, Integer>> cases = new HashMap<Case, Map<Integer, Integer>>();
	
	public CmdUpdateCasesRoulette(Map<Case, Map<Integer, Integer>> cases) {
		this.cases = cases;
	}

	@Override
	public void action(Controleur controleur) {
		((ControleurRouletteClient)controleur).updateTableJeu(this.cases);
	}

}
