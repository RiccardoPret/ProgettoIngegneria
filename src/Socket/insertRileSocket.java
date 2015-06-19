package Socket;

import java.net.Socket;

import model.Configurazione;

public class insertRileSocket implements Runnable {
	Socket socket;
	Configurazione conf;
	int id_disp;

	 public insertRileSocket(Socket accept) {
		// TODO Auto-generated constructor stub
	}

	public void sendConfigSocket(Socket socket) {
		// TODO Auto-generated constructor stub
		 this.socket=socket;
	}

		@Override
		public void run() {
			// TODO Auto-generated method stub

		}
}
