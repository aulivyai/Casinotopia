package ca.uqam.casinotopia.controleur.serveur;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ca.uqam.casinotopia.TypeEtatPartie;
import ca.uqam.casinotopia.TypeJeu;
import ca.uqam.casinotopia.commande.Commande;
import ca.uqam.casinotopia.commande.CommandeServeur;
import ca.uqam.casinotopia.commande.CommandeServeurControleurChat;
import ca.uqam.casinotopia.commande.CommandeServeurControleurClient;
import ca.uqam.casinotopia.commande.CommandeServeurControleurPrincipal;
import ca.uqam.casinotopia.commande.CommandeServeurControleurRoulette;
import ca.uqam.casinotopia.commande.CommandeServeurControleurThread;
import ca.uqam.casinotopia.commande.client.CmdAfficherJeuRoulette;
import ca.uqam.casinotopia.connexion.Connexion;
import ca.uqam.casinotopia.controleur.ControleurServeur;
import ca.uqam.casinotopia.modele.client.ModelePartieRouletteClient;
import ca.uqam.casinotopia.modele.serveur.ModeleClientServeur;
import ca.uqam.casinotopia.modele.serveur.ModelePartieRouletteServeur;
import ca.uqam.casinotopia.modele.serveur.ModeleUtilisateurServeur;

public class ControleurServeurThread extends ControleurServeur implements Runnable {

	private static final long serialVersionUID = -656190032558998826L;

	protected Map<String, ControleurServeur> lstControleurs = new HashMap<String, ControleurServeur>();

	private ModeleUtilisateurServeur modele;

	public ControleurServeurThread(Socket clientSocket, int number) {
		super(new Connexion(clientSocket), new ModeleClientServeur());
		//this.setConnexion(new Connexion(clientSocket));
		this.modele = new ModeleUtilisateurServeur();
		this.modele.number = number;

		this.ajouterControleur("ControleurPrincipalServeur", ControleurPrincipalServeur.getInstance());
		this.ajouterControleur("ControleurClientServeur", new ControleurClientServeur(this.getConnexion(), this.client));
	}

	public void ajouterControleur(String nom, ControleurServeur ctrl) {
		this.lstControleurs.put(nom, ctrl);
	}

	@Override
	public void run() {
		try {
			System.out.println("client no " + this.modele.number + " connect�");
			while (this.connexion.isConnected()) {
				Commande cmd = null;
				try {
					System.out.println("ATTENTE DE COMMANDE DU CLIENT");
					cmd = (Commande) this.connexion.getObjectInputStream().readObject();
					System.out.println("COMMANDE CLIENT OBTENUE");
					if (cmd != null) {
						if (cmd instanceof CommandeServeur) {
							if (cmd instanceof CommandeServeurControleurClient) {
								if (!this.lstControleurs.containsKey("ControleurClientServeur")) {
									System.out.println("ERREUR : Envoie d'une commande � un controleur non-instanci�! (ControleurClientServeur)");
									// THROW EXCEPTION
									// On ne devrait jamais recevoir une
									// commande pour un controleur en
									// particulier sans que ce dernier ait �t�
									// cr��
									// (par l'envoie d'une commande du client,
									// g�n�ralement au ControleurServeurThread)
								}
								cmd.action(this.lstControleurs.get("ControleurClientServeur"));
							}
							else if (cmd instanceof CommandeServeurControleurRoulette) {
								if (!this.lstControleurs.containsKey("ControleurRouletteServeur")) {
									System.out.println("ERREUR : Envoie d'une commande � un controleur non-instanci�! (ControleurRouletteServeur)");
									// THROW EXCEPTION
									// On ne devrait jamais recevoir une
									// commande pour un controleur en
									// particulier sans que ce dernier ait �t�
									// cr��
									// (par l'envoie d'une commande du client,
									// g�n�ralement au ControleurServeurThread)
								}
								cmd.action(this.lstControleurs.get("ControleurRouletteServeur"));
							}
							else if (cmd instanceof CommandeServeurControleurChat) {
								if (!this.lstControleurs.containsKey("CommandeServeurControleurChat")) {
									System.out.println("ERREUR : Envoie d'une commande � un controleur non-instanci�! (CommandeServeurControleurChat)");
									// THROW EXCEPTION
									// On ne devrait jamais recevoir une
									// commande pour un controleur en
									// particulier sans que ce dernier ait �t�
									// cr��
									// (par l'envoie d'une commande du client,
									// g�n�ralement au ControleurServeurThread)
								}
								cmd.action(this.lstControleurs.get("CommandeServeurControleurChat"));
							}
							else if (cmd instanceof CommandeServeurControleurPrincipal) {
								cmd.action(this.lstControleurs.get("ControleurPrincipalServeur"));
							}
							else if (cmd instanceof CommandeServeurControleurThread) {
								cmd.action(this);
							}
						}
						else {
							System.err.println("Seulement des commandes destin�es au serveur sont recevables!");
						}
					}
					else {
						System.err.println("Un probl�me est survenu (commande nulle).");
					}
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					System.out.println("D�connexion du client " + this.modele.number);
					this.modele.getUtilisateur().deconnecter();
					this.getConnexion().close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Thread.sleep(1);// sauve du cpu
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * public void actionAjouterJoueurDansPartie(TypeJeu type) { switch(type) {
	 * case ROULETTE : ModelePartieRouletteServeur partieRoulette =
	 * (ModelePartieRouletteServeur)
	 * this.ctrlPrincipal.rechercherPartieEnAttente(TypeJeu.ROULETTE); break;
	 * case BLACKJACK : ModelePartieBlackJackServeur partieBlackJack =
	 * (ModelePartieBlackJackServeur)
	 * this.ctrlPrincipal.rechercherPartieEnAttente(TypeJeu.BLACKJACK); break; }
	 * }
	 */

	// TODO Un Jeu poss�de une liste de parties associ�es... donc au d�part on
	// cr�e les jeu, et on ajoute des partie aux jeux?
	public void actionAjouterJoueurDansRoulette(int idJeu) {
		// TODO cr�er la partie dans la liste de partie sur le controleur
		// principal et aussi dans le controleurServeurThread du client

		ControleurPrincipalServeur ctrlPrincipal = (ControleurPrincipalServeur) this.lstControleurs.get("ControleurPrincipalServeur");

		ModelePartieRouletteServeur partieRoulette = (ModelePartieRouletteServeur) ctrlPrincipal.rechercherPartieEnAttente(idJeu);

		System.out.println("JEU DANS SERVEUR_THREAD : " + ctrlPrincipal.getJeu(idJeu));

		System.out.println("MAP<JEU> DANS SERVEUR_THREAD : " + ctrlPrincipal.getLstJeux().get(TypeJeu.ROULETTE));

		System.out.println("PARTIE DANS SERVEUR_THREAD : " + partieRoulette);
		
		//TODO � enlever (pour des tests)
		partieRoulette = null;

		// TODO comment faire pour trouver un id unique a une partie? Parcourir le map de jeu AU COMPLET (tout type de jeux confondus)?
		// Genre, dans une boucle de i=0 ... 99999999, pour chaque i on test si une partie avec cet id existe?
		// Ou encore, se faire un Map simple de Map<Integer, Partie>, et �
		// chaque fois qu'on cr�e/supprime une partie, on met � jourle map de partie et le map de Jeu.
		// Donc, toutes les parties du map de partie se retrouvent � quelque part dans le map de jeu
		if (partieRoulette == null) {
			partieRoulette = new ModelePartieRouletteServeur(ctrlPrincipal.getIdPartieLibre(), true, true, ctrlPrincipal.getJeu(idJeu));
			ctrlPrincipal.ajouterPartie(partieRoulette, TypeEtatPartie.EN_ATTENTE);
			System.out.println("AUCUNE PARTIE EN ATTENTE DISPONIBLE, CR�ATION D'UNE NOUVELLE, ID : " + String.valueOf(partieRoulette.getId()));
		}
		else {
			System.out.println("PARTIE EN ATTENTE TROUV�E, ID : " + String.valueOf(partieRoulette.getId()));
		}

		this.ajouterControleur("ControleurRouletteServeur", new ControleurRouletteServeur(this.connexion, this.client, partieRoulette));

		this.cmdAfficherJeuRoulette(partieRoulette);
	}

	public void cmdAfficherJeuRoulette(ModelePartieRouletteServeur modeleServeur) {
		ModelePartieRouletteClient modeleClient = new ModelePartieRouletteClient(modeleServeur.getId(), modeleServeur.isOptionArgent(),
				modeleServeur.isOptionMultijoueur(), modeleServeur.getInfoJeu());
		System.out.println("CONNEXION DANS CMD_AFFICHER_JEU_ROULETTE --> " + this.connexion);
		this.connexion.envoyerCommande(new CmdAfficherJeuRoulette(modeleClient));
	}

	/**
	 * @return the modele
	 */
	public ModeleUtilisateurServeur getModele() {
		return this.modele;
	}

	/**
	 * @param modele
	 *            the modele to set
	 */
	public void setModele(ModeleUtilisateurServeur modele) {
		this.modele = modele;
	}

	public ArrayList<String> getAllUtilisateurs() {
		System.out.println("GET_ALL_UTILISATEURS");
		ArrayList<String> liste = new ArrayList<String>();
		for (int i = 0; i < ControleurPrincipalServeur.NUMCONNEXION; i++) {
			if (ControleurPrincipalServeur.thread[i] != null && ControleurPrincipalServeur.thread[i].isAlive()
					&& ControleurPrincipalServeur.serverThread[i].getModele().getUtilisateur().getNomUtilisateur() != null) {
				liste.add(ControleurPrincipalServeur.serverThread[i].getModele().getUtilisateur().getNomUtilisateur());
			}
		}
		return liste;
	}

	public void envoyerCommandeATous(Commande cmd) {
		System.out.println("ENVOYER_COMMANDE_TOUS");
		for (int i = 0; i < ControleurPrincipalServeur.NUMCONNEXION; i++) {
			if (ControleurPrincipalServeur.thread[i] != null && ControleurPrincipalServeur.thread[i].isAlive()
					&& ControleurPrincipalServeur.serverThread[i].getModele().getUtilisateur().getNomUtilisateur() != null) {
				ControleurPrincipalServeur.serverThread[i].getConnexion().envoyerCommande(cmd);
			}
		}
	}

}
