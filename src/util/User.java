package util;

public class User {
	//Non modificabili
	private Dispositivo dispositivo;
	private String username;
	//modificabili
	private String email;
	private String telefono;
	private String video;
	
	public User(String user){
		this.username=user;
		//default value
		this.dispositivo=null;
		this.email = "NA";
		this.telefono = "NA";
		this.video="NA";
	}
	
	public String getUsername() {
		return username;
	}
	
	public Dispositivo getDispositivo(){
		return this.dispositivo;
	}
	
	public void setDispositivo(Dispositivo device){
		this.dispositivo=device;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getVideo() {
		return video;
	}
	public void setVideo(String video) {
		this.video = video;
	}
	
}
