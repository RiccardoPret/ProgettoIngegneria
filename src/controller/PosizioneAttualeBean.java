package controller;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import Socket.Gestore_connessioniUscita;
import model.Dispositivo;
import model.Posizione;
import model.User;
import model.Configurazione;


@ManagedBean
@ViewScoped
public class PosizioneAttualeBean {
	
	@ManagedProperty(value="#{clientBean}")
	private ClientBean clientBean;
	
	DataSource ds;
	Posizione pos;
	int freqPosOriginale;
	User client;
	Configurazione config;
	Dispositivo dis;
	MapModel model;
	
	public PosizioneAttualeBean() {
		ds= new DataSource();
		
	}
	
	@PostConstruct
	public void init(){
		client=clientBean.getUser();
		config=clientBean.getConfigurazione();
		freqPosOriginale=config.getfPos();
		dis=client.getDispositivo();
		aggiornaPos();
	}
	@PreDestroy
	public void stopRealTime(){
		this.config.setfPos(freqPosOriginale);
		ds.updateDbInstance(this.client, this.config);
		Gestore_connessioniUscita.getInstance().updateConfig(client.getDispositivo().getId());
	}
	
	public void startRealTime(){
		System.out.println("cambio freq pos");
		this.config.setfPos(2);
		ds.updateDbInstance(this.client, this.config);
		Gestore_connessioniUscita.getInstance().updateConfig(client.getDispositivo().getId());
	}
	
	public void aggiornaPos() {
		// TODO Auto-generated method stub
		pos=ds.getUltimaPosizione(dis);
		updateModel();
	}

	public ClientBean getClientBean(){
		return this.clientBean;
	}
	
	public void setClientBean(ClientBean cb){
		this.clientBean=cb;
	}
	
	public boolean existPosition(){
		if(pos!=null)
			return true;
		return false;
	}
	
	public void updateModel(){
		model = new DefaultMapModel();
		model.addOverlay(new Marker(new LatLng(pos.getY(),pos.getX())));
	}
	
	public MapModel getModel(){
		return model;
	}
	public Posizione getPos(){
		return pos;
	}

}
