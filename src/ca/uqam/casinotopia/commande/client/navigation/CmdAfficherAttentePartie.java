package ca.uqam.casinotopia.commande.client.navigation;

import ca.uqam.casinotopia.commande.CommandeClientControleurPrincipal;
import ca.uqam.casinotopia.controleur.Controleur;
import ca.uqam.casinotopia.controleur.client.ControleurPrincipalClient;

public class CmdAfficherAttentePartie implements CommandeClientControleurPrincipal {

	private static final long serialVersionUID = -8501685682102346657L;

	@Override
	public void action(Controleur controleur) {
		((ControleurPrincipalClient) controleur).actionAfficherAttentePartie();
	}
}