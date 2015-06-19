package model;

public class Configurazione {
	
	private Dispositivo dispositivo;
	private Integer fPos;	//secondi
	private Integer fSms;	//secondi
	private boolean smsEnabled;
	private boolean emailEnabled;
	private Integer speedAlarm;		//km/h
	
	public Configurazione(Dispositivo dispositivo){
		this.dispositivo=dispositivo;
	}
	
	public Dispositivo getDispositivo(){
		return this.dispositivo;
	}

	public Integer getfPos() {
		return fPos;
	}

	public void setfPos(Integer fPos) {
		this.fPos = fPos;
	}

	public Integer getfSms() {
		return fSms;
	}

	public void setfSms(Integer fSms) {
		this.fSms = fSms;
	}

	public boolean isSmsEnabled() {
		return smsEnabled;
	}

	public void setSmsEnabled(boolean smsEnabled) {
		this.smsEnabled = smsEnabled;
	}

	public boolean isEmailEnabled() {
		return emailEnabled;
	}

	public void setEmailEnabled(boolean emailEnabled) {
		this.emailEnabled = emailEnabled;
	}

	public Integer getspeedAlarm() {
		return speedAlarm;
	}

	public void setspeedAlarm(Integer speed) {
		this.speedAlarm = speed;
	}
	
	//Customized for our view
	public String getFormattedFpos(){
		int s, m, h;
		h=this.fPos/3600;
		m=(this.fPos%3600)/60;
		s=(this.fPos%3600)%60;
		return h+":"+m+":"+s;
	}
	
	public void setFormattedFpos(String str){
		int h=Integer.parseInt(str.split(":")[0]);
		int m=Integer.parseInt(str.split(":")[1]);
		int s=Integer.parseInt(str.split(":")[2]);
		
		this.fPos=s+m*60+h*3600;
	}
	
	public String getFormattedFsms(){
		int s, m, h;
		h=this.fSms/3600;
		m=(this.fSms%3600)/60;
		s=(this.fSms%3600)%60;
		return h+":"+m+":"+s;
	}
	
	public void setFormattedFsms(String str){
		int h=Integer.parseInt(str.split(":")[0]);
		int m=Integer.parseInt(str.split(":")[1]);
		int s=Integer.parseInt(str.split(":")[2]);
		
		this.fSms=s+m*60+h*3600;
	}
}
