package ca.uqam.casinotopia.modele.serveur;

import ca.uqam.casinotopia.Jeu;
import ca.uqam.casinotopia.Partie;
import ca.uqam.casinotopia.TypeJeu;
import ca.uqam.casinotopia.modele.Modele;
import ca.uqam.casinotopia.observateur.Observateur;

@SuppressWarnings("serial")
public class ModelePartieBlackJackServeur extends Partie implements Modele {

	public ModelePartieBlackJackServeur(int id, boolean optionArgent, boolean optionMultijoueur, Jeu infoJeu) {
		super(id, optionArgent, optionMultijoueur, infoJeu);
	}
}