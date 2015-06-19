package Socket;

import java.io.IOException;
import java.net.ServerSocket;

public class ConfigSocket implements Runnable {

	public boolean active = false;
	ServerSocket configPort;
	
	public void ConfigSocket(){

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			configPort=new ServerSocket(4000);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		while (active) {
			try {
				new Thread(new sendConfigSocket(configPort.accept())).start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
