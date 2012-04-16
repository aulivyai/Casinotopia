package ca.uqam.casinotopia.observateur;

public interface Sujet {
	public void ajouterObservateur(Observateur obs);
	public void retirerObservateur(Observateur obs);
	public boolean estObserveePar(Observateur obs);
	public void notifierObservateur();
}
