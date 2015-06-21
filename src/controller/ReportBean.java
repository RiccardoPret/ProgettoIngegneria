package controller;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SlideEndEvent;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.Marker;

import model.Posizione;

@ManagedBean
@ViewScoped
public class ReportBean implements Serializable{
	
	@ManagedProperty(value="#{clientBean}")
	private ClientBean clientBean;
	
	DataSource ds;
	Posizione currentPosition;
	int currentIndex;
	List<Posizione> reportPosizioni;
	private MapModel model;
	
	public ReportBean(){
		ds= new DataSource();
	}
	
	@PostConstruct
	public void init(){
		currentPosition=null;
		reportPosizioni=null;
		posizioniUtente();
		if(!reportPosizioni.isEmpty())
		setCurrentIndex(0);
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
	
	
	public int getNumeroReport(){
		return this.reportPosizioni.size()-1;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		System.out.println(currentIndex);
		this.currentIndex = currentIndex;
		currentPosition=reportPosizioni.get(currentIndex);
		updateModel();
	}
	
	public void updateModel(){
		model = new DefaultMapModel();
		model.addOverlay(new Marker(new LatLng(currentPosition.getY(),currentPosition.getX())));
	}
	
	public MapModel getModel(){
		return model;
	}
    public void onSlideEnd(SlideEndEvent event) {
      //  FacesMessage message = new FacesMessage("Slide Ended", "Value: " + event.getValue());
        setCurrentIndex(event.getValue());
    } 

    public boolean hasPositions(){
    	return !reportPosizioni.isEmpty();
    }
}
