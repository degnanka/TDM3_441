package fr.esisar.exo3;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurTCP {

	public static void main(String[] args) throws Exception {
		ServeurTCP serveur = new ServeurTCP();
		serveur.execute();
	}

	public void execute() throws IOException{
		System.out.println("Demarrage du serveur");
        ServerSocket socketEcoute = new ServerSocket();
        socketEcoute.bind(new InetSocketAddress(2000));
        
        System.out.println("Attente de la connexion du client");
        Socket socketConnexion = socketEcoute.accept();

        System.out.println("Un client est connecté");
        System.out.println("IP:"+ socketConnexion.getInetAddress());
        System.out.println("Port:"+ socketConnexion.getPort());
		
        for (int i=0;i<=5;i++) {
        	byte[] bufR = new byte[2048];
        	InputStream is = socketConnexion.getInputStream();
        	int lenBufR = is.read(bufR);
        	if (lenBufR!=-1)
        	{
        		String message = new String(bufR, 0 , lenBufR);
        		System.out.println("Message recu = "+message);
        	}    	
        }
        
        socketEcoute.close();
        System.out.println("Arret du serveur .");
	}
}
