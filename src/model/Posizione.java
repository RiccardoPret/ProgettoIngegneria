package model;

import java.sql.Timestamp;

import util.Coordinate;


public class Posizione {
	Dispositivo dispositivo;
	Coordinate coordinate;
	Timestamp timestamp;
	
	public Posizione(Dispositivo device){
		this.dispositivo=device;
	}
	
	public Dispositivo getDispositivo() {
		return dispositivo;
	}
	public Coordinate getCoordinate() {
		return coordinate;
	}
	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	
}
