package ca.uqam.casinotopia.vue.salle;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import ca.uqam.casinotopia.controleur.ControleurClient;
import ca.uqam.casinotopia.controleur.client.ControleurChatClient;
import ca.uqam.casinotopia.controleur.client.ControleurSalleClient;
import ca.uqam.casinotopia.modele.client.ModeleChatClient;
import ca.uqam.casinotopia.modele.client.ModeleClientClient;
import ca.uqam.casinotopia.modele.client.ModeleSalleClient;
import ca.uqam.casinotopia.objet.AvatarClient;
import ca.uqam.casinotopia.objet.JeuClient;
import ca.uqam.casinotopia.observateur.Observable;
import ca.uqam.casinotopia.type.TypeDeplacement;
import ca.uqam.casinotopia.vue.GridBagHelper;
import ca.uqam.casinotopia.vue.Vue;
import ca.uqam.casinotopia.vue.chat.VueChat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Vue principale des salles
 */
@SuppressWarnings("serial")
public class VueSalle extends Vue {
	
	private ControleurSalleClient controleur;
	
	/**
	 * La game loop associ� � la salle
	 */
	private MondeVirtuel mondeVirtuel;
	
	/**
	 * Sert � g�rer les multiples keyPressed
	 */
	private Map<TypeDeplacement, Boolean> keysPressed;
	
	/**
	 * Liste de clients dans la salle
	 */
	private Map<Integer, ModeleClientClient> lstClients;
	
	/**
	 * Liste des jeux de la salle
	 */
	private Map<Integer, JeuClient> lstJeux;
	
	public int idClient;
	

	public VueSalle(ControleurClient controleur, Map<Integer, ModeleClientClient> lstClients, int idClient, Map<Integer, JeuClient> lstJeux) {
		this.controleur = (ControleurSalleClient) controleur;
		
		//TODO g�rer l'ajout des avatars en dehors du constructeur (events update)
		
		this.keysPressed = new HashMap<TypeDeplacement, Boolean>();
		this.keysPressed.put(TypeDeplacement.HAUT, false);
		this.keysPressed.put(TypeDeplacement.BAS, false);
		this.keysPressed.put(TypeDeplacement.GAUCHE, false);
		this.keysPressed.put(TypeDeplacement.DROITE, false);
		
		this.lstClients = lstClients;
		
		this.idClient = idClient;
		
		this.lstJeux = lstJeux;

		this.setPanelOptions();
		this.addComponents();
		this.createComponentsMap();
	}
	
	public void demarrerMondeVirtuel() {
		this.mondeVirtuel = new MondeVirtuel(this);
		
		Thread threadMondeVirtuel = new Thread(mondeVirtuel);
		threadMondeVirtuel.start();
	}

	@Override
	protected void addComponents() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 700, 308 };
		gridBagLayout.rowHeights = new int[] { 50, 250, 380 };
		this.setLayout(gridBagLayout);
		
		JPanel pnlAvatars = new JPanel();
		pnlAvatars.setName("pnlAvatars");
		pnlAvatars.setLayout(null);
		pnlAvatars.setFocusable(true);
		pnlAvatars.requestFocus();
		pnlAvatars.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent ke) {
				switch (ke.getKeyCode()) {
		            case KeyEvent.VK_UP :
		            	keysPressed.put(TypeDeplacement.HAUT, true);
		            	break;
		            case KeyEvent.VK_DOWN :
		            	keysPressed.put(TypeDeplacement.BAS, true);
		            	break;
		            case KeyEvent.VK_LEFT :
		            	keysPressed.put(TypeDeplacement.GAUCHE, true);
		            	break;
		            case KeyEvent.VK_RIGHT :
		            	keysPressed.put(TypeDeplacement.DROITE, true);
		            	break;
		        }
			}
			
			@Override
			public void keyReleased(KeyEvent ke) {
				switch (ke.getKeyCode()) {
		            case KeyEvent.VK_UP :
		            	keysPressed.put(TypeDeplacement.HAUT, false);
		            	break;
		            case KeyEvent.VK_DOWN :
		            	keysPressed.put(TypeDeplacement.BAS, false);
		            	break;
		            case KeyEvent.VK_LEFT :
		            	keysPressed.put(TypeDeplacement.GAUCHE, false);
		            	break;
		            case KeyEvent.VK_RIGHT :
		            	keysPressed.put(TypeDeplacement.DROITE, false);
		            	break;
		        }
			}
		});
		
		for(JeuClient jeu : this.lstJeux.values()) {
			JLabel imgTable;
			switch(jeu.getType()) {
				case ROULETTE :
					imgTable = new JLabel(new ImageIcon(VueSalle.class.getResource("/img/table/roulette.gif")));
					imgTable.setName("tableJeu" + jeu.getId());
					imgTable.setBounds(jeu.getEmplacement());
					break;
				case MACHINE :
					imgTable = new JLabel(new ImageIcon(VueSalle.class.getResource("/img/table/machine.png")));
					imgTable.setName("tableJeu" + jeu.getId());
					imgTable.setBounds(jeu.getEmplacement());
					break;
				default :
					imgTable = null;
			}
			
			pnlAvatars.add(imgTable);
		}
		
		for(ModeleClientClient client : this.lstClients.values()) {
			AvatarClient avatar = client.getAvatar();
			JLabel imgAvatar = new JLabel(new ImageIcon(VueSalle.class.getResource(avatar.getPathImage())));
			imgAvatar.setName("avatarClient" + client.getId());
			imgAvatar.setBounds(avatar.getX(), avatar.getY(), avatar.getLargeur(), avatar.getHauteur());
			pnlAvatars.add(imgAvatar);
		}
		
		pnlAvatars.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		this.add(pnlAvatars, new GridBagHelper().setXY(0, 0).setWH(1, 3).setFill(GridBagConstraints.BOTH).end());
		
		JButton btnQuitter = new JButton("Quitter");
		btnQuitter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controleur.cmdQuitterSalle();
			}
		});
		btnQuitter.setToolTipText("Quitter la salle");
		this.add(btnQuitter, new GridBagHelper().setXY(1, 0).setAnchor(GridBagConstraints.EAST).end());
		
		

		ControleurChatClient ctrlChatClient = new ControleurChatClient(this.controleur.getConnexion(), new ModeleChatClient(), this.controleur.getModeleClient(), this.controleur.getModeleNav());
		this.controleur.getModeleNav().ajouterControleur("ControleurChatClient", ctrlChatClient);
		
		VueChat chat = ctrlChatClient.getVue();
		chat.cacherSalle();
		ctrlChatClient.cmdSeConnecterAuChat("Salle1");
		chat.setName("chat");
		this.add(chat, new GridBagHelper().setXY(1, 2).setWH(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.SOUTH).end());
	}
	
	/**
	 * Afficher la vue de s�lection des options d'un jeu.
	 * 
	 * @param jeu Le jeu li� aux options
	 */
	public void afficherSelectionOptionJeu(JeuClient jeu) {
		VueSelectionOptionJeu vueOptionsJeu = (VueSelectionOptionJeu) this.getComponentByName("vueOptionsJeu");
		
		if(vueOptionsJeu == null) {
			vueOptionsJeu = new VueSelectionOptionJeu(this.controleur, jeu);
			vueOptionsJeu.setName("vueOptionsJeu");
			vueOptionsJeu.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			
			this.add(vueOptionsJeu, new GridBagHelper().setXY(1, 1).setFill(GridBagConstraints.BOTH).end());
	
			this.componentMap.put(vueOptionsJeu.getName(), vueOptionsJeu);
			
			revalidate();
			repaint();
		}
	}
	
	/**
	 * Cacher la vue de s�lection des options d'un jeu.
	 */
	public void cacherSelectionOptionJeu() {
		VueSelectionOptionJeu vueOptionsJeu = (VueSelectionOptionJeu) this.getComponentByName("vueOptionsJeu");
		
		if(vueOptionsJeu != null) {
			this.remove(vueOptionsJeu);
			this.componentMap.remove("vueOptionsJeu");
			
			revalidate();
			repaint();
		}
	}
	
	/**
	 * Mise � jour d'un avatar dans la salle
	 * Permet de modifier son image et sa position
	 * 
	 * @param avatar L'avatar a modifier
	 */
	private void updateAvatar(AvatarClient avatar) {
		JLabel imgAvatar = (JLabel) this.getComponentByName("avatarClient" + avatar.getId());
		
		imgAvatar.setIcon(new ImageIcon(VueSalle.class.getResource(avatar.getPathImage())));
		
		//TODO NullPointerException quand on est dans une salle et qu'on p�se sur le "X"? R�gl�?
		imgAvatar.setBounds(avatar.getX(), avatar.getY(), avatar.getLargeur(), avatar.getHauteur());
		
		revalidate();
		repaint();
	}
	
	/**
	 * Ajouter un client dans la salle
	 * 
	 * @param nouveauClient Le client � ajouter
	 */
	private void ajouterClient(ModeleClientClient nouveauClient) {
		JPanel pnlAvatars = (JPanel) this.getComponentByName("pnlAvatars");
		AvatarClient avatar = nouveauClient.getAvatar();
		JLabel imgAvatar = new JLabel(new ImageIcon(VueSalle.class.getResource(avatar.getPathImage())));
		imgAvatar.setName("avatarClient" + nouveauClient.getId());
		imgAvatar.setBounds(avatar.getX(), avatar.getY(), avatar.getLargeur(), avatar.getHauteur());
		pnlAvatars.add(imgAvatar);
		
		revalidate();
		repaint();
		
		this.lstClients.put(nouveauClient.getId(), nouveauClient);
		this.componentMap.put(imgAvatar.getName(), imgAvatar);
	}
	
	/**
	 * Retirer un client de la salle
	 * 
	 * @param clientRetire Le client � retirer
	 */
	private void retirerClient(ModeleClientClient clientRetire) {
		JPanel pnlAvatars = (JPanel) this.getComponentByName("pnlAvatars");
		
		if(clientRetire != null) {
			JLabel imgAvatar = (JLabel) this.getComponentByName("avatarClient" + clientRetire.getId());
			pnlAvatars.remove(imgAvatar);
			
			revalidate();
			repaint();
			
			//TODO Utile???
			this.lstClients.remove(clientRetire.getId());
			this.componentMap.remove(imgAvatar.getName());
		}
	}

	@Override
	public void update(Observable observable) {
		if(observable instanceof AvatarClient) {
			AvatarClient modele = (AvatarClient) observable;
			switch (modele.getTypeModif()) {
				case DEPLACEMENT :
					this.updateAvatar(modele);
					break;
			}
		}
		else if(observable instanceof ModeleSalleClient) {
			ModeleSalleClient modele = (ModeleSalleClient) observable;
			switch (modele.getTypeModif()) {
				case NOUVEAU_CLIENT :
					this.ajouterClient(modele.getDernierClient());
					break;
					
				case RETIRER_CLIENT :
					this.retirerClient(modele.getClientRetire());
					break;
			}
		}
	}
	
	public ControleurSalleClient getControleur() {
		return this.controleur;
	}
	
	public Map<TypeDeplacement, Boolean> getKeysPressed() {
		return this.keysPressed;
	}
}