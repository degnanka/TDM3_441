package fr.esisar.exo4.joueur;

public class Question {
	String question;

	Question(){

	}

	Question(String question) {
		this.question = question;
	}
	
	//description: set l attribut question avec le contenu du parametre (setter)
	public void fill(String str) {
		question = str;
	}
	
	//description: test si un '?'est present dans la chaine
	//return: true si un '?' est present, false sinon
	public boolean valid() {
		String findStr = "?";
		return question.contains(findStr);	 
	}
	
	//description: parcours la chaine a la recherche d'un '?' et en calcule le nombre.
	//return: le nombre de '?' trouves, corrspondant au nombre de questions du server
	public int nbQuestion() {
		int cnt=0;
		for(int i=0;i<question.length();i++) {
			if(question.charAt(i)=='?') {
				++cnt;
			}
		}
		return cnt;
	}
}