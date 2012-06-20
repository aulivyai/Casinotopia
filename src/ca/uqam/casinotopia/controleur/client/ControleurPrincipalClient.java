package ca.uqam.casinotopia.controleur.client;

import java.awt.EventQueue;

import javax.swing.JOptionPane;

import ca.uqam.casinotopia.commande.Commande;
import ca.uqam.casinotopia.commande.serveur.authentification.CmdAuthentifierClient;
import ca.uqam.casinotopia.commande.serveur.compte.CmdCreerCompte;
import ca.uqam.casinotopia.commande.serveur.compte.CmdModifierCompte;
import ca.uqam.casinotopia.connexion.Connexion;
import ca.uqam.casinotopia.controleur.ControleurClient;
import ca.uqam.casinotopia.modele.client.ModeleClientClient;
import ca.uqam.casinotopia.modele.client.ModelePrincipalClient;
import ca.uqam.casinotopia.modele.client.ModeleChatClient;
import ca.uqam.casinotopia.modele.client.ModelePartieRouletteClient;
import ca.uqam.casinotopia.modele.client.ModeleSalleClient;

public class ControleurPrincipalClient extends ControleurClient {

	private String[] listeServeur;

	private boolean enReceptionDeCommande = false;

	public ControleurPrincipalClient() {
		super(new ModelePrincipalClient());
		this.modeleNav.ajouterControleur("ControleurPrincipalClient", this);
		this.modeleNav.initFrame();
		this.listeServeur = new String[] { "localhost", "oli.dnsd.me", "dan.dnsd.me" };
		this.enReceptionDeCommande = false;
		this.afficherConnexion();
	}

	/**
	 * Afficher l'interface de connexion au serveur
	 */
	private void afficherConnexion() {
		EventQueue.invokeLater(this.modeleNav.getFrameConnexion());
	}

	public void afficherFrameApplication() {
		this.modeleNav.initFrameApplication();
		ControleurBarreMenuBas ctrl = new ControleurBarreMenuBas(this.connexion, this.client, this.getModeleNav());
		this.modeleNav.ajouterControleur("ControleurBarreMenuBas", ctrl);
		this.modeleNav.changerMenuFrameApplication("VueBarreMenuBas", ctrl.getVue());
		EventQueue.invokeLater(this.modeleNav.getFrameApplication());
	}

	public void cmdConnexionAuServeur(String nomUtilisateur, char[] motDePasse) {
		if (!this.connexion.isConnected()) {
			System.out.println("recherche de serveur...");
			this.setMessageConnexion("recherche de serveur...");
			int i = 0;
			while (this.connexion.isConnected() == false && i < this.listeServeur.length) {
				this.setConnexion(new Connexion(this.listeServeur[i], 7777));
				i++;
			}
		}

		if (this.connexion.isConnected()) {
			this.setMessageConnexion("connect�!");

			this.connexion.envoyerCommande(new CmdAuthentifierClient(nomUtilisateur, motDePasse));

			this.receptionCommandes();

			System.out.println("FIN DE CONNEXION");
		}
	}

	private void receptionCommandes() {
		if (!this.enReceptionDeCommande) {
			this.enReceptionDeCommande = true;
			new Thread(new ClientThread(this)).start();
		}
	}

	public void setMessageConnexionErreur(String message) {
		this.modeleNav.getFrameConnexion().setMessageErreur(message);
	}

	public void setMessageConnexion(String message) {
		this.modeleNav.getFrameConnexion().setMessage(message);
	}

	public void actionInitClient(ModeleClientClient modele) {
		this.client = modele;
		
		ControleurClientClient ctrlClient = new ControleurClientClient(this.connexion, this.client, this.modeleNav);
		this.modeleNav.ajouterControleur("ControleurClientClient", ctrlClient);
		
		this.afficherFrameApplication();
		this.afficherMenuPrincipal();
	}

	public void actionAfficherMenuPrincipal() {
		this.afficherMenuPrincipal();
	}
	
	private void afficherMenuPrincipal() {
		ControleurMenuPrincipal ctrlMenuPrincipal = new ControleurMenuPrincipal(this.connexion, this.client, this.modeleNav);
		this.modeleNav.ajouterControleur("ControleurMenuPrincipal", ctrlMenuPrincipal);
		this.modeleNav.cacherFrameConnexion();
		this.modeleNav.changerVueFrameApplication("VueMenuPrincipal", ctrlMenuPrincipal.getVue());
	}

	public void actionAfficherSalle(ModeleSalleClient modele) {
		modele.ajouterClient(this.client);
		ControleurSalleClient ctrlSalle = new ControleurSalleClient(this.connexion, modele, this.client, this.modeleNav);
		this.modeleNav.ajouterControleur("ControleurSalleClient", ctrlSalle);
		this.modeleNav.changerVueFrameApplication("VueSalle", ctrlSalle.getVue());
		
		ctrlSalle.getVue().demarrerMondeVirtuel();
		
		/*Thread threadMondeVirtuel = new Thread(ctrlSalle.getVue());
		threadMondeVirtuel.start();*/
	}
	
	/*public void afficherSalle(String nomSalle) {
		ControleurSalleClient ctrlSalle = new ControleurSalleClient(this.connexion, new ModeleSalleClient(nomSalle), this.client, this.modeleNav);
		this.modeleNav.ajouterControleur("ControleurSalleClient", ctrlSalle);
		this.modeleNav.cacherFrameConnexion();
		this.modeleNav.changerVueFrameApplication("VueSalle", ctrlSalle.getVue());
	}*/

	public void actionAfficherChat(ModeleChatClient modele) {
		ControleurChatClient ctrlChatClient = new ControleurChatClient(this.connexion, modele, this.client, this.modeleNav);
		this.modeleNav.ajouterControleur("ControleurChatClient", ctrlChatClient);
		this.modeleNav.changerVueFrameApplication("VueChat", ctrlChatClient.getVue());
	}

	public void actionAfficherJeuRoulette(ModelePartieRouletteClient modele) {
		ControleurRouletteClient ctrlRouletteClient = new ControleurRouletteClient(this.connexion, modele, this.client, this.modeleNav);
		this.modeleNav.ajouterControleur("ControleurRouletteClient", ctrlRouletteClient);
		this.modeleNav.changerVueFrameApplication("VueRoulette", ctrlRouletteClient.getVue());
		//TODO Forcer le refresh via le pattern observeur?
		ctrlRouletteClient.actionUpdateTableJeu(modele.getTableJeu().getCases());
	}

	public void actionQuitterPartieRouletteClient() {
		//TODO Au lieu d'afficher le menu principal, il faudrait afficher la salle o� le jeu �tait
		ControleurRouletteClient ctrlRoulette = (ControleurRouletteClient) this.modeleNav.getControleur("ControleurRouletteClient");
		ControleurMenuPrincipal ctrlMenu = (ControleurMenuPrincipal) this.modeleNav.getControleur("ControleurMenuPrincipal");
		
		this.modeleNav.retirerControleur("ControleurRouletteClient");
		
		ctrlMenu.cmdJoindreSalle(ctrlRoulette.getModele().getInfoJeu().getIdSalle());
	}
	
	public void actionQuitterChatClient() {
		this.modeleNav.retirerControleur("ControleurChatClient");
		this.afficherMenuPrincipal();
	}

	public void actionAfficherJeuMachine() {
		ControleurMachineClient ctrlMachineClient = new ControleurMachineClient(this.connexion, this.client, this.modeleNav);
		this.modeleNav.ajouterControleur("ControleurMachineClient", ctrlMachineClient);
		this.modeleNav.changerVueFrameApplication("VueRoulette", ctrlMachineClient.getVue());
	}

	public void actionQuitterSalleClient() {
		ControleurSalleClient ctrlSalle = (ControleurSalleClient) this.modeleNav.getControleur("ControleurSalleClient");
		ctrlSalle.quitterSalleClient();
		
		this.modeleNav.retirerControleur("ControleurSalleClient");
		this.afficherMenuPrincipal();
	}

	public void cmdCreerCompte(ModeleClientClient modeleClientClient) {
		if (!this.connexion.isConnected()) {
			this.setMessageConnexion("recherche de serveur...");
			int i = 0;
			while (this.connexion.isConnected() == false && i < this.listeServeur.length) {
				this.setConnexion(new Connexion(this.listeServeur[i], 7777));
				i++;
			}
		}
		
		ControleurClientClient ctrlClient = new ControleurClientClient(this.connexion, this.client, this.modeleNav);
		this.modeleNav.ajouterControleur("ControleurClientClient", ctrlClient);
		
		Commande cmd = new CmdCreerCompte(modeleClientClient);
		this.connexion.envoyerCommande(cmd);
		//TODO Pourquoi il faut r�initialiser la r�ception? Sa cr�e un autre thread...
		//this.receptionCommandes();
	}


	public void cmdAfficherCreationCompte() {
		modeleNav.initFrameGestionCompte(this, true);
		EventQueue.invokeLater(this.modeleNav.getFrameGestionCompte());	
	}

	public void setMessageInformationCreationCompte(String message) {
		JOptionPane.showMessageDialog(null, message);
	}

	public void afficherModificationCompte() {
		modeleNav.initFrameGestionCompte(this, false);
		EventQueue.invokeLater(this.modeleNav.getFrameGestionCompte());	
	}

	public void cmdModifierCompte(ModeleClientClient modeleClientClient) {
		ControleurClientClient ctrlClient = new ControleurClientClient(this.connexion, this.client, this.modeleNav);
		this.modeleNav.ajouterControleur("ControleurClientClient", ctrlClient);
		
		Commande cmd = new CmdModifierCompte(modeleClientClient);
		this.connexion.envoyerCommande(cmd);
		//TODO Pourquoi il faut r�initialiser la r�ception? Sa cr�e un autre thread...
		//this.receptionCommandes();
	}
	
	public void actionQuitterPartieMachineClient() {
		this.modeleNav.retirerControleur("ControleurMachineClient");
		this.afficherMenuPrincipal();
	}

	public void actionAfficherAttentePartie() {
		//TODO faire une vue/fenetre/popup
		System.out.println("En attente d'autres joueurs...");
	}
}