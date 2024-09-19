package fr.esisar.exo4.joueur;


public class Joueur {

	static String host;
	static int port;
	static String test;
	static int op1;
	static int num;


	public static void main(String[] args) throws Exception
	{
		//flag used to exit the main loop: if server returns "SCORE", game finished
		boolean mustStop=false;
		//string containing server messages
		String strReceive="";
		//create Question object used to process server messages
		Question q = new Question();
		//get args
		host = args[0];
		port = Integer.parseInt(args[1]);

		//create TCP client and open it
		ClientJeu client = new ClientJeu(host,port);	
		client.open();

		//main loop (while server does not return "SCORE") 
		while(!mustStop) {
			//append server messages while no '?'and "SCORE" are contained in the
			//received message
			do {
				strReceive = strReceive + client.receive();
				System.out.println(strReceive);
				if(strReceive.contains("SCORE")) {
					mustStop = true;
				}
				q.fill(strReceive);
			}while(!q.valid() || mustStop==true);
			//if game continues (no "SCORE" has been sent by server
			if(mustStop==false) {
				//get number of question (i.e number of operations)
				int nbQ = q.nbQuestion();
				//allocate result array of size number of operation
				int[] result = new int[nbQ];
				//for the number of operation
				for(int i=0;i<nbQ;i++) {
					//get numbers of operation
					int op1, op2;
					//typical trame: 89+32=?78+56=?2
					//convert first op by converting substring from 0 to '+'
					op1 = Integer.parseInt(strReceive.substring(0, strReceive.indexOf('+')));
					//split from '+' operator and get the rest
					strReceive = strReceive.substring(strReceive.indexOf('+')+1);
					//convert second op by converting substring from 0 to '='
					op2 = Integer.parseInt(strReceive.substring(0, strReceive.indexOf('=')));
					System.out.println(strReceive);
					//if current operation processing is not the last one
					if(i!=(nbQ-1)) {
						//split from '?' and get the rest, in order to take
						//the following operation
						strReceive = strReceive.substring(strReceive.indexOf('?')+1);
					}
					else {
						//we proceeded the last operation, so test if last character is '?'
						//(corresponding to the end of operation)
						if(strReceive.charAt(strReceive.length()-1) != '?') {
							//if the last character is not '?', that means server sent
							//beginning of the following operation
							//split from first '?' operator and get the rest
							strReceive = strReceive.substring(strReceive.indexOf('?')+1);
						}
						else {
							//the last character is '?', so erase the server message
							strReceive = "";
						}
					}
					//do the operation
					result[i] = op1 + op2;
				}
				//initialize result string to be sent to server
				String msgResult="";
				//for all operation
				for(int j=0;j<nbQ;j++) {
					//append the result of the operation and a ';'
					msgResult = msgResult.concat(result[j]+";");
				}
				System.out.println(msgResult);
				//sent the operations results to the server
				client.send(msgResult);
				//results has been sent, server answers with new operations
				//go to beginning of the main loop
			}
		}

		client.close();
	}
}