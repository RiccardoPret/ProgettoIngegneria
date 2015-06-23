package Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Timestamp;

import org.postgresql.geometric.PGpoint;

import controller.DatabaseDriverC3P0;
import model.Dispositivo;
import model.Posizione;

public class ThreadInsPosizione implements Runnable {
	Socket socket;
	Dispositivo dis;
	int id_disp;
	Posizione posizione;
	DatabaseDriverC3P0 dd;

	 public ThreadInsPosizione(Socket accept) {
		// TODO Auto-generated constructor stub
		 this.socket=accept;
		 dd=DatabaseDriverC3P0.getInstance();
	}

	public void sendConfigSocket(Socket socket) {
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
			          while( !in.ready()){}
			          id_disp=Integer.parseInt((in.readLine()));
			          //dd.openConnection();
			          dis=dd.getDispositivoFromId(id_disp);
			          posizione=new Posizione(dis);
			          PGpoint coord=new PGpoint(Double.parseDouble(in.readLine()), Double.parseDouble(in.readLine()));
			          Timestamp time=new Timestamp(Long.parseLong(in.readLine()));
			          posizione.setCoordinate(coord);
			          posizione.setTimestamp(time);
			          dd.insertPosizione(posizione);
			        //  dd.closeConnection();
			          socket.close();
			        } catch (IOException e) {
			            e.printStackTrace();
			        }
		}
}
