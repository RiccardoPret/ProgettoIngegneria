package model;

import java.sql.Timestamp;

import org.postgresql.geometric.PGpoint;


public class Posizione {
	Dispositivo dispositivo;
	PGpoint coordinate;
	Timestamp timestamp;
	
	public Posizione(Dispositivo device){
		this.dispositivo=device;
	}
	
	public Dispositivo getDispositivo() {
		return dispositivo;
	}
	public PGpoint getCoordinate() {
		return coordinate;
	}
	public double getX(){
		return this.coordinate.x;
	}
	public double getY(){
		return this.coordinate.y;
	}
	public void setCoordinate(PGpoint coordinate) {
		this.coordinate = coordinate;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	
}
