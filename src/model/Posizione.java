package model;

import java.sql.Timestamp;

import org.postgis.PGgeometry;


public class Posizione {
	Dispositivo dispositivo;
	PGgeometry coordinate;
	Timestamp timestamp;
	
	public Posizione(Dispositivo device){
		this.dispositivo=device;
	}
	
	public Dispositivo getDispositivo() {
		return dispositivo;
	}
	public PGgeometry getCoordinate() {
		return coordinate;
	}
	public void setCoordinate(PGgeometry coordinate) {
		this.coordinate = coordinate;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	
}
