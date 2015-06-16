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
	
	private void mapInstantiate() {
		map= new HashMap<String, String>();
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(getReservedPath()));
			String line = "";
			while ((line = in.readLine()) != null) {
			    String parts[] = line.split(separator);
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

	private String getObjectClassName() {
		String s = this.startingPointClass.getClass().getName();

		s = s.substring(s.lastIndexOf(".") + 1);
		s += ".class";
		return s;
	}
	
	private String getReservedPath() {
		Path path = null;

		try {
			System.out.println(startingPointClass.getClass().getResource(getObjectClassName()));
			path = Paths.get(startingPointClass.getClass().getResource(getObjectClassName()).toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		while (!(path = path.getParent()).getFileName().toString()
				.equals("WEB-INF"));
		System.out.println(path.toString()+"/reserved/"+this.fileName);
		return path.toString()+"/reserved/"+this.fileName;
	}

	public String getValue(String key){
		return map.get(key);
	}
}