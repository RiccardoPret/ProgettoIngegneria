package Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import controller.DatabaseDriver;
import model.Configurazione;
import model.Dispositivo;

public class sendConfigSocket implements Runnable {
Socket socket;
Configurazione conf;
int id_disp;
DatabaseDriver dd= DatabaseDriver.getInstance();

 public sendConfigSocket(Socket socket) {
	// TODO Auto-generated constructor stub
	 this.socket=socket;
}

	@Override
	public void run() {
		  try (
		            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);		            BufferedReader in = new BufferedReader(
		                new InputStreamReader(
		                    socket.getInputStream()));
		        ) {
		          String inputLine, outputLine;
		          while( !in.ready()){}
		          id_disp=Integer.parseInt((in.readLine()));
		          System.out.println("id_ricevuto:"+id_disp);
		          dd.openConnection();
		          conf=dd.getConfigurazione(new Dispositivo(id_disp));
		          dd.closeConnection();
		 
		          out.write(conf.getfPos()+"\n");
		          out.write(conf.getfSms()+"\n");
		          out.write(conf.getspeedAlarm()+"\n");
		          out.write(conf.isEmailEnabled()+"\n");
		          out.write(conf.isSmsEnabled()+"\n");
		          out.flush();  
		          socket.close();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		  
	}
}
