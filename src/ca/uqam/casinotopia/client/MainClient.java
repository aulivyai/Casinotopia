package ca.uqam.casinotopia.client;

import ca.uqam.casinotopia.controleur.Controleur;
import ca.uqam.casinotopia.controleur.client.ControleurPrincipalClient;

@SuppressWarnings("serial")
public class MainClient extends Controleur {

	public static void main(String[] args) {
		new ControleurPrincipalClient();
	}
}