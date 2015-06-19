package Socket;

public class Gestore_connessioniEntrata {

	public static Gestore_connessioniEntrata instance = null;
	private ConfigSocket config; // socket per ricevere la richiesta di mandare la configurazione
	private RilevazioneSocket rilevazioni; // socket per ricevere rilevazioni
	

	private Gestore_connessioniEntrata() {
		 config=new ConfigSocket();
		 rilevazioni=new RilevazioneSocket();
	}

	public static Gestore_connessioniEntrata getInstance() {
		if (instance == null) {
			instance = new Gestore_connessioniEntrata();
		}
		return instance;
	}
	
	public void startConfigSocket(){
		config.active=true;
		new Thread(config).start();
	}
	
	public void startRilevazioniSocket(){
		rilevazioni.active=true;
		new Thread(rilevazioni).start();
	}
	public void startAllSocket(){
		startConfigSocket();
		startRilevazioniSocket();
	}
	
	public void stopConfigSocket(){
		config.active=false;
	}
	
	public void stopRilevazioniSocket(){
		rilevazioni.active=false;
	}
	
	public void stopAllSocket(){
		rilevazioni.active=false;
		config.active=false;
	}
}
