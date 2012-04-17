package ca.uqam.casinotopia.controleur.serveur;

import java.util.Map;

import ca.uqam.casinotopia.Case;
import ca.uqam.casinotopia.commande.Commande;
import ca.uqam.casinotopia.commande.client.CmdUpdateCasesRoulette;
import ca.uqam.casinotopia.connexion.Connexion;
import ca.uqam.casinotopia.controleur.ControleurServeur;
import ca.uqam.casinotopia.modele.serveur.ModeleRouletteServeur;

public class ControleurRouletteServeur extends ControleurServeur {

	public ControleurRouletteServeur(Connexion connexion) {
		super(connexion);
		
		if(!this.lstModeles.containsKey("ModeleRouletteServeur")) {
			ModeleRouletteServeur modeleRoulette = new ModeleRouletteServeur(0, true, true, null);
			this.ajouterModele(modeleRoulette);
		}
	}
	
	public void actionEffectuerMises(Map<Integer, Map<Case, Integer>> mises) {
		System.out.println("ACTION_EFFECTUER_MISES");
		ModeleRouletteServeur modele = (ModeleRouletteServeur) this.getModele(ModeleRouletteServeur.class.getSimpleName());
		modele.effectuerMises(mises);
		
		this.cmdUpdateTableJoueurs(modele.getId(), modele.getTableJeu().getCases());
	}
	
	//TODO Les cases (mises) seront r�cup�r�es � partie de la partie, je le passe en param�tre pour les tests
	public void cmdUpdateTableJoueurs(int partieId, Map<Case, Map<Integer, Integer>> cases) {
		//TODO Rechercher les joueurs de la partie et mettre � jour leur table de jeu
		
		Commande cmd = new CmdUpdateCasesRoulette(cases);
		
		System.out.println("AVANT ENVOI UPDATE ROULLETE");
		
		this.getConnexion().envoyerCommande(cmd);
	}

}