package Socket;

import java.io.IOException;
import java.net.ServerSocket;

public class RilevazioneSocket implements Runnable {

	boolean active = false;
	ServerSocket rilPort;
	
	public void ConfigSocket(){
		try {
			rilPort=new ServerSocket(4001);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		/*
		while (active) {
			try {
				new Thread(new insertRileSocket(rilPort.accept()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
*/
	}
}
