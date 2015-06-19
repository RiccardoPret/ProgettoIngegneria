package util;

public class Coordinate {
	private double lat;
	private double lon;
	
	public Coordinate(double lat, double lon){
		this.setLatitude(lat);
		this.setLongitude(lon);
	}

	public double getLatitude() {
		return lat;
	}

	public void setLatitude(double lat2) {
		this.lat = lat2;
	}

	public double getLongitude() {
		return lon;
	}

	public void setLongitude(double lon) {
		this.lon = lon;
	}
	
	public boolean equals(Object obj){
		if(!(obj instanceof Coordinate)){
			return false;
		}
		Coordinate coor=(Coordinate) obj;
		return this.getLatitude()==coor.getLatitude() || this.getLongitude()==coor.getLongitude();
	}
}
