package model;

public class PersonaleAzienda {
	private String username;
	private String ruolo;
	private String nome;
	private String cognome;
	
	public PersonaleAzienda(String username){
		this.username=username;
	}
	
	public String getUsername(){
		return this.username;
	}
	
	public String getRuolo() {
		return ruolo;
	}
	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	
}
