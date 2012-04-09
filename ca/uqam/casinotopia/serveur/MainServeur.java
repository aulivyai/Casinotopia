package ca.uqam.casinotopia.serveur;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


public class MainServeur {


	static final int NUMCONNEXION = 10;
	static ServerSocket server;
	static Thread[] thread = new Thread[NUMCONNEXION];
	static ServerThread[] serverThread = new ServerThread[NUMCONNEXION];
	private static Boolean actif = true; 
	
	public static void main(String[] args) {
		

	    try {
	      InetAddress address = InetAddress.getLocalHost();
	      System.out.println("Ton ip est surement : "+address.getHostAddress());
	    }
	    catch (UnknownHostException e) {
	      System.out.println("Could not find this computer's address.");
	    }
		try {
			System.out.println("cr�ation du server");
			server = new ServerSocket(7777);
			System.out.println("server d�marr�");
			while(actif){
				Socket skt = server.accept();
				for (int i = 0; i < NUMCONNEXION; i++) {
					if(thread[i] != null && !thread[i].isAlive()){
						thread[i] = null;
					}
					if(thread[i] == null){
						serverThread[i] = new ServerThread(skt,i);
						thread[i] = new Thread(serverThread[i]);
						thread[i].start();
						System.out.println("client "+i+" connect�");
						break;
					}
				}
				//indiquer au client que le serveur est plein
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	

	

}
