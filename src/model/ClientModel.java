package model;

import controller.ClientDataSource;
import controller.Configurazione;
import controller.Dispositivo;

/**
 * Data Bean per tutti i dati del cliente
 */

public class ClientModel {


	private int idDispositivo;
	private String username = "";
	private String password = "";
	private String email = "";
	private String telefono = "";
	
	int fPos;
	int fSms;
	boolean smsEnabled;
	boolean emailEnabled;
	int speedAlarm;
	
	
}
