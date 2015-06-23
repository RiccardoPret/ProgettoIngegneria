package Socket;

import java.io.IOException;
import java.net.ServerSocket;

public class ConfigSocket implements Runnable {

	public boolean active = false;
	ServerSocket configPort;
	
	public ConfigSocket(){
		try {
			configPort=new ServerSocket(4000);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		

		while (active) {
			try {
				new Thread(new ThreadInvioConfigurazione(configPort.accept())).start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
