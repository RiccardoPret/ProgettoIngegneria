package controller;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import model.Posizione;

@ManagedBean
@SessionScoped
public class ReportBean implements Serializable{
	
	@ManagedProperty(value="#{clientBean}")
	private ClientBean clientBean;
	
	DataSource ds;
	Posizione currentPosition;
	List<Posizione> reportPosizioni;
	
	public ReportBean(){
		ds= new DataSource();
	}
	
	@PostConstruct
	public void init(){
		posizioniUtente();
	}
	
	public ClientBean getClientBean(){
		return this.clientBean;
	}
	
	public void setClientBean(ClientBean cb){
		this.clientBean=cb;
	}
	
	public void posizioniUtente() {
		this.reportPosizioni=ds.getPosizioni(clientBean.getUser().getDispositivo());
	}

	public Posizione getCurrentPosition(){
		return this.currentPosition;
	}
	
	public List<Posizione> getLastPositions(){
		System.out.println(clientBean.getUser().getUsername());
		return this.reportPosizioni;
	}
	public Timestamp getTimestamp(){
		return this.currentPosition.getTimestamp();
	}
}
