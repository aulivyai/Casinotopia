package ca.uqam.casinotopia.vue.navigation;

import ca.uqam.casinotopia.controleur.client.ControleurMenuPrincipal;
import ca.uqam.casinotopia.modele.client.ModeleChatClient;
import ca.uqam.casinotopia.observateur.Observable;
import ca.uqam.casinotopia.vue.GridBagHelper;
import ca.uqam.casinotopia.vue.Vue;

import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class VueMenuPrincipal extends Vue {
	private ControleurMenuPrincipal controleur;

	/**
	 * Create the panel.
	 */
	public VueMenuPrincipal(ControleurMenuPrincipal controleur) {
		this.controleur = controleur;

		this.setPanelOptions();
		this.addComponents();
		this.createComponentsMap();
	}

	@Override
	protected void addComponents() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 450 };
		gridBagLayout.rowHeights = new int[] { 100, 100, 100, 100 };

		this.setLayout(gridBagLayout);
		
		JButton btnSalle = new JButton("Afficher la salle");
		btnSalle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controleur.cmdJoindreSalle(1);
				//controleur.cmdJoindreSalle("MEGAFUN");
				//controleur.afficherSalle("MEGAFUN");
			}
		});
		this.add(btnSalle, new GridBagHelper().setXY(0, 0).end());

		JButton btnRoulette = new JButton("Jouer \u00E0 la roulette");
		btnRoulette.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controleur.cmdJouerRoulette();
			}
		});
		this.add(btnRoulette, new GridBagHelper().setXY(0, 1).end());

		JButton btnChat = new JButton("Aller sur le chat");
		btnChat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controleur.actionAfficherChat(new ModeleChatClient());
			}
		});
		this.add(btnChat, new GridBagHelper().setXY(0, 2).end());
		
		JButton btnMachine = new JButton("Jouer \u00E0 la machine \u00E0 sous");
		btnMachine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controleur.actionJouerMachine();
			}
		});
		
		GridBagConstraints gbc_btnMachine = new GridBagConstraints();
		gbc_btnMachine.gridx = 0;
		gbc_btnMachine.gridy = 3;
		add(btnMachine, gbc_btnMachine);
	}

	@Override
	public void update(Observable observable) {
		// TODO Auto-generated method stub

	}
}