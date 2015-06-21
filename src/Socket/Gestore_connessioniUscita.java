package Socket;

public class Gestore_connessioniUscita {
	public static Gestore_connessioniUscita instance = null;

	public Gestore_connessioniUscita() {
		// TODO Auto-generated constructor stub
	}
	
	public static Gestore_connessioniUscita getInstance() {
		if (instance == null) {
			instance = new Gestore_connessioniUscita();
		}
		return instance;
	}
	
	public void updateConfig(int id_dispositivo){
		new Thread(new ThredAggConfigurazione(id_dispositivo)).start();	
	}
}
