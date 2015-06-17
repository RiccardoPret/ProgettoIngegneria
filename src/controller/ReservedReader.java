package controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class ReservedReader {

	private HashMap<String, String> map;
	private Object startingPointClass;
	private String fileName;
	private String separator;

	public ReservedReader(Object obj, String fileName) {
		//TODO evitare il primo parametro e far riferimento sempre a questo file
		this.startingPointClass=obj;
		this.fileName=fileName;
		this.separator = "=>";
		
		mapInstantiate();
	}

	public ReservedReader(Object obj, String fileName, String separator) {
		this.startingPointClass=obj;
		this.fileName=fileName;
		this.separator = separator;
		
		mapInstantiate();
	}
	
	/*
	 * Carica nell'hashmap tutte le coppie chiave valore nel file
	 */
	private void mapInstantiate() {
		map= new HashMap<String, String>();
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(getReservedPath()));
			String line = "";
			while ((line = in.readLine()) != null) {
			    String parts[] = line.split(this.separator);
			    map.put(parts[0], parts[1]);
			}
			in.close();
		} catch (FileNotFoundException e) {
			//apertura del file fallita
			e.printStackTrace();
		} catch (IOException e) {
			//lettura del file o chiusura del file fallita
			e.printStackTrace();
		}
	}

	/*
	 * Restituisce il nome della classe senza il package completo e con il .class
	 */
	private String getObjectClassName() {
		String s = this.startingPointClass.getClass().getName();

		s = s.substring(s.lastIndexOf(".") + 1);
		s += ".class";
		return s;
	}
	
	/*
	 * Da il percorso completo del file riservato da dove leggere i dati
	 */
	
	private String getReservedPath() {
		Path path = null;

		try {
			path = Paths.get(startingPointClass.getClass().getResource(getObjectClassName()).toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		//Vado indietro fino alla cartella WEB-INF, la quale contiene la cartella reserved
		while (!(path = path.getParent()).getFileName().toString()
				.equals("WEB-INF"));
		
		return path.toString()+"/reserved/"+this.fileName;
	}

	protected String getValue(String key){
		return map.get(key);
	}
}