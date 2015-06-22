package Socket;

public class Gestore_connessioniEntrata {

	public static Gestore_connessioniEntrata instance = null;
	private ConfigSocket socketAggConfig; // socket per ricevere la richiesta di mandare la configurazione
	private PosizioneSocket socketInsPosizioni; // socket per ricevere socketInsPosizioni
	private EmailSender socketMail;
	

	private Gestore_connessioniEntrata() {
		 socketAggConfig=new ConfigSocket();
		 socketInsPosizioni=new PosizioneSocket();
		 socketMail=new EmailSender();
	}

	public static Gestore_connessioniEntrata getInstance() {
		if (instance == null) {
			instance = new Gestore_connessioniEntrata();
		}
		return instance;
	}
	
	public void startConfigSocket(){
		socketAggConfig.active=true;
		new Thread(socketAggConfig).start();
	}
	
	public void startSocketInsPosizione(){
		socketInsPosizioni.active=true;
		new Thread(socketInsPosizioni).start();
	}
	
	public void startSocketEmail(){
		socketMail.active=true;
		new Thread(socketMail).start();
	}
	
	public void startAllSocket(){
		startConfigSocket();
		startSocketInsPosizione();
		startSocketEmail();
	}
	
	public void stopConfigSocket(){
		socketAggConfig.active=false;
	}
	
	public void stopRilevazioniSocket(){
		socketInsPosizioni.active=false;
	}
	public void stopSocketEmail(){
		socketMail.active=false;
	}
	
	public void stopAllSocket(){
		stopConfigSocket();
		stopRilevazioniSocket();
		stopSocketEmail();
	}
}
