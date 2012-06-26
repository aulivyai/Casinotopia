package ca.uqam.casinotopia.controleur.client;

import java.util.List;

import ca.uqam.casinotopia.commande.serveur.chat.CmdEnvoyerMessageChat;
import ca.uqam.casinotopia.commande.serveur.chat.CmdQuitterChat;
import ca.uqam.casinotopia.commande.serveur.chat.CmdSeConnecterAuChat;
import ca.uqam.casinotopia.connexion.Connexion;
import ca.uqam.casinotopia.controleur.ControleurClient;
import ca.uqam.casinotopia.modele.client.ModeleChatClient;
import ca.uqam.casinotopia.modele.client.ModeleClientClient;
import ca.uqam.casinotopia.modele.client.ModelePrincipalClient;
import ca.uqam.casinotopia.vue.chat.VueChat;

/**
 * Controleur g�rant les actions du chat.
 */
public class ControleurChatClient extends ControleurClient {

	/**
	 * modele du chat
	 */
	private ModeleChatClient modele;
	
	/**
	 * vue du chat
	 */
	private VueChat vue;
	
	public ControleurChatClient(Connexion connexion, ModeleChatClient modele, ModeleClientClient client, ModelePrincipalClient modeleNavigation) {
		super(connexion, client, modeleNavigation);
		this.vue = new VueChat(this);
		this.modele = modele;
		this.modele.ajouterObservateur(this.vue);
		this.vue.setLstConnecteModel(this.modele.getLstUtilisateurModel());
	}

	public VueChat getVue() {
		return this.vue;
	}

	public void setVue(VueChat vue) {
		this.vue = vue;
	}
	
	public ModeleChatClient getModele() {
		return this.modele;
	}

	/**
	 * initialise le chat
	 * @param listeUtilisateur liste des utilisteurs � afficher
	 * @param listeMessages liste des messages � afficher
	 * @param salle le nom de la salle � afficher
	 */
	public void initChat(List<String> listeUtilisateur, List<String> listeMessages, String salle){
		this.modele.setChatUtilisateur(listeUtilisateur);
		
		this.modele.setMessages(listeMessages);
		this.modele.setSalle(salle);
	}

	/**
	 * envoie une commande au serveur pour se connecter
	 * @param salle
	 */
	public void cmdSeConnecterAuChat(String salle) {
		this.modele.setSalle(salle);
		
		this.connexion.envoyerCommande(new CmdSeConnecterAuChat(salle));
	}
	
	/**
	 * envoie une commande au serveur pour se d�connecter du chat
	 */
	public void cmdQuitterChat() {
		this.connexion.envoyerCommande(new CmdQuitterChat());
	}
	
	/**
	 * change la salle du client
	 * @param salle la nouvelle salle
	 */
	public void actionChangementSalle(String salle) {
		this.modele.setSalle(salle);
	}

	/**
	 * envoie une commande pour envoyer un message, et efface le textbox
	 */
	public void cmdEnvoyerMessageChat() {
		this.connexion.envoyerCommande(new CmdEnvoyerMessageChat(this.vue.txtMessage.getText(), this.modele.getSalle()));
		this.vue.txtMessage.setText("");
		this.vue.txtMessage.setFocusable(true);
	}
	
	/**
	 * ajoute un message � la liste des messages du chat
	 * @param message le message � afficher � l'�cran
	 */
	public void actionAjouterMessageChat(String message) {
		this.modele.setMessages(this.modele.getMessages() + "\n" + message);
	}

	/**
	 * affiche la liste d'utilisateurs � l'�cran
	 * @param listeUtilisateur la liste � afficher
	 */
	public void setChatUtilisateur(List<String> listeUtilisateur) {
		this.modele.setChatUtilisateur(listeUtilisateur);
	}
}