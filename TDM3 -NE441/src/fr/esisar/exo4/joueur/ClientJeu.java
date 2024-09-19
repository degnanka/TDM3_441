package fr.esisar.exo4.joueur;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;


public class ClientJeu{
	String host;
	int port;
	Socket socket;
	OutputStream os;
	InputStream is;
	InetSocketAddress adrDest;


	public ClientJeu(String host, int port){
		this.host = host;
		this.port = port;
	}

	//open socket and connect to server
	public void open() throws IOException {
		//
		System.out.println("Demarrage du client ...");

		//Creation de la socket
		socket = new Socket();
		// Connexion au serveur 
		InetSocketAddress adrDest = new InetSocketAddress(host, port);
		socket.connect(adrDest);
		os = socket.getOutputStream();
		is = socket.getInputStream();
	}

	//send a String on the socket 
	public void send(String message) throws IOException
	{
		// Envoi de la requete
		byte[] bufE = new String(message).getBytes();
		os.write(bufE);
	}

	//receive String from socket. Blocking method 
	public String receive() throws IOException {
		// Attente de la reponse 
		byte[] bufR = new byte[2048];
		int lenBufR = is.read(bufR);
		String reponse = new String(bufR, 0 , lenBufR );
		return reponse;
	}

	//send byte buffer on the socket 
	public void send(byte[] buf) throws IOException
	{
		// Envoi de la requete
		os.write(buf);
	}

	//receive byte buffer from socket. Blocking method 
	public int receive(byte[] buf) throws IOException {
		return is.read(buf);
	}

	//read the code
	public void close() throws IOException
	{
		// Fermeture de la socket
		socket.close();
		System.out.println("Arret du client .");
	}
}
