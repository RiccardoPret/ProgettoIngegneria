package Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import controller.DatabaseDriver;

public class updateConfigSocket implements Runnable {

	int id;
	String res[];
	public updateConfigSocket(int id_dispositivo) {
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
