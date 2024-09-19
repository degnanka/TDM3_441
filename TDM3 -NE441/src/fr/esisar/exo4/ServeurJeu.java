package fr.esisar.exo4;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class ServeurJeu {

	public static void main(String[] args) throws Exception {
		ServeurJeu serveur = new ServeurJeu();
		serveur.execute();
	}

	public void execute() throws IOException, InterruptedException {
		// Déclaration du serveur
		System.out.println("Démarrage du serveur");
		ServerSocket socketEcoute = new ServerSocket();
		socketEcoute.bind(new InetSocketAddress(7500));

		// Attente d'un client
		System.out.println("Attente de la connexion du client");
		Socket socketConnexion = socketEcoute.accept();

		// Flux IO
		InputStream is = socketConnexion.getInputStream();
		OutputStream os = socketConnexion.getOutputStream();

		int score_joueur = 0;
		long t_debut;
		long t_fin;
		long t_reponse;

		try {
			while (true) {
				// Création de plusieurs questions
				String question = creerQuestions(2);  // Par exemple, 2 questions
				System.out.println("Questions envoyées : " + question);

				// Générer le résultat attendu (pour toutes les questions)
				String[] reponsesAttendues = reponseQuestions(question);
				System.out.println("Réponses attendues : " + String.join(";", reponsesAttendues));

				// Envoi des questions
				byte[] bufE = question.getBytes();
				os.write(bufE);

				// Enregistrer le temps avant de recevoir la réponse
				t_debut = System.currentTimeMillis();

				// Réception de la réponse
				byte[] bufR = new byte[2048];
				int lenBufR = is.read(bufR);
				if (lenBufR == -1) {
					break; // Si aucune donnée n'est reçue (fin de connexion), quitter la boucle
				}
				String reponse = new String(bufR, 0, lenBufR).trim();
				System.out.println("Réponse reçue : " + reponse);

				// Enregistrer le temps après la réception
				t_fin = System.currentTimeMillis();

				// Calcul du temps de réponse
				t_reponse = t_fin - t_debut;
				System.out.println("Temps de réponse = " + t_reponse + " ms");

				// Vérification des réponses
				String[] reponsesDonnees = reponse.split(";");
				boolean toutesCorrectes = true;

				for (int i = 0; i < reponsesAttendues.length; i++) {
					if (i < reponsesDonnees.length && reponsesDonnees[i].equals(reponsesAttendues[i])) {
						if (t_reponse <= 200) {
							score_joueur += 100;  // Réponse rapide (moins de 200 ms)
						} else {
							score_joueur += 1;  // Réponse correcte mais lente
						}
					} else {
						toutesCorrectes = false;
						break;
					}
				}

				if (!toutesCorrectes) {
					// Si une réponse est incorrecte, afficher le score et quitter la boucle
					String scoreMessage = "SCORE PARTIE : " + score_joueur + " points\nSCORE TOTAL\n"+ socketConnexion.getInetAddress()+ " : " + score_joueur + "\n";
					byte[] bufScore = scoreMessage.getBytes();
					os.write(bufScore);
					System.out.println("Réponse envoyée");
					break;
				}

				System.out.println("Temps de réponse : " + t_reponse + " ms, Score : " + score_joueur);
			}

		} catch (IOException e) {
			System.out.println("Erreur avec le client : " + e.getMessage());
		} finally {
			socketConnexion.close();
			System.out.println("Connexion fermée, attendre 15 secondes");
			Thread.sleep(15000);
		}
	}

	// Fonction pour créer plusieurs questions
	/*private static String creerQuestions(int nbQuestions) {
		StringBuilder questions = new StringBuilder();
		Random rand = new Random();
		for (int i = 0; i < nbQuestions; i++) {
			int a = rand.nextInt(100);
			int b = rand.nextInt(100);
			questions.append(a).append("+").append(b).append("=?");
		}
		return questions.toString();
	}*/
	private static String creerQuestions(int nbQuestions) {
	    String questions = ""; 
	    Random rand = new Random();
	    for (int i = 0; i < nbQuestions; i++) {
	        int a = rand.nextInt(100);
	        int b = rand.nextInt(100);
	        questions += a + "+" + b + "=?";
	    }
	    return questions;
	}


	// Fonction pour obtenir les réponses attendues à partir des questions
	private static String[] reponseQuestions(String questions) {
		String[] parties = questions.split("\\+|=\\?");
		String[] reponses = new String[parties.length / 2]; // Il y a 2 parties par question
		for (int i = 0; i < reponses.length; i++) {
			int a = Integer.parseInt(parties[2 * i]);
			int b = Integer.parseInt(parties[2 * i + 1]);
			reponses[i] = Integer.toString(a + b);
		}
		return reponses;
	}

}
