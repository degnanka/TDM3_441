package fr.esisar.exo2;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientTCP {

	public static void main(String[] args) throws Exception {
		ClientTCP client = new ClientTCP();
		client.execute();
	}
	
	public void execute() throws IOException{
		System.out.println("Demarrage du client");
        Socket socket = new Socket();
        InetSocketAddress adrDest = new InetSocketAddress("127.0.0.1", 2000);
        socket.connect(adrDest);        
		
        byte[] bufE = new String("Bonjour le serveur\n").getBytes();
        OutputStream os = socket.getOutputStream();
        os.write(bufE);
        System.out.println("Message envoye");
        
        socket.close();
        System.out.println("Arret du client");

	}

}
