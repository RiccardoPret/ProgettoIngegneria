package Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import controller.DatabaseDriver;

public class ThredAggConfigurazione implements Runnable {

/*
 * questa classe invia un messaggio al dispositivo con id passato nel costruttore 
 * per dirgli di aggiornare la sua configurazione.
 * il dispositivo in seguito alla richiesta si connetterà alla porta 4000 
 * e farà la richiesta per la nuova configurazione
 * 
 * */
	
	
	int id;
	String res[];
	public ThredAggConfigurazione(int id_dispositivo) {
		// TODO Auto-generated constructor stub
		this.id=id_dispositivo;
		DatabaseDriver dd=DatabaseDriver.getInstance();
		dd.openConnection();
		res=dd.getIpPort(id);
		dd.closeConnection();
	}

	@Override
	public void run() {
		try (			
			    Socket socket = new Socket(res[0], Integer.parseInt(res[1]));
			    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			    BufferedReader in = new BufferedReader(
			        new InputStreamReader(socket.getInputStream()));
			){
				out.write("aggiorna\n");
				out.flush();
				socket.close();
			
			}catch(IOException e){}

	}

}
