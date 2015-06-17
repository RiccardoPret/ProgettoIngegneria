package controller;

public class Dispositivo {

	private Integer id;
	private boolean isActive;
	private String modello;
	
	public Dispositivo(Integer id){
		this.id=id;
		this.isActive=false;
		this.modello="A";
	}
	
	public Dispositivo(Integer id, String model, boolean isActive){
		this.id=id;
		this.isActive=isActive;
		this.modello=model;
	}
	
	public Integer getId(){
		return this.id;
	}
	
	public String getModello(){
		return this.modello;
	}
	
	public boolean isActive(){
		return this.isActive;
	}
	
	public void On(){
		this.isActive=true;
	}
	
	public void Off(){
		this.isActive=false;
	}
}
