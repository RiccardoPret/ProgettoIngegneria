package Socket;

import java.io.IOException;
import java.net.ServerSocket;

public class PosizioneSocket implements Runnable {

	boolean active = false;
	ServerSocket socketInsPosizione;
	
	public PosizioneSocket(){
		try {
			socketInsPosizione=new ServerSocket(4001);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		while (active) {
			try {
				new Thread(new ThreadInsPosizione(socketInsPosizione.accept())).start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
