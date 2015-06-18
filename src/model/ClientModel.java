package model;

import controller.ClientDataSource;

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
