package model;

public class Configurazione {

	private Dispositivo dispositivo;
	private Integer fPos; // secondi
	private Integer fSms; // secondi
	private boolean smsEnabled;
	private boolean emailEnabled;
	private Integer speedAlarm; // km/h

	public Configurazione(Dispositivo dispositivo) {
		this.dispositivo = dispositivo;
	}

	public Dispositivo getDispositivo() {
		return this.dispositivo;
	}

	public Integer getfPos() {
		return fPos;
	}

	public void setfPos(Integer fPos) {
		if (fPos > 0)
			this.fPos = fPos;
	}

	public Integer getfSms() {
		return fSms;
	}

	public void setfSms(Integer fSms) {
		if (fPos > 0)
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
		if (fPos > 0)
			this.speedAlarm = speed;
	}

}
