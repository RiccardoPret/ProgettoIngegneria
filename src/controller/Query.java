package controller;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Query {

	private static Query instance;
	private HashMap<String, String> queries;
	private String defaultPath="query.txt";
	private String separator="=>";
	
	private Query(){
		queries= new HashMap<String, String>();
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(defaultPath));
			String line = "";
			while ((line = in.readLine()) != null) {
			    String parts[] = line.split(separator);
			    queries.put(parts[0], parts[1]);
			}
			in.close();
		} catch (FileNotFoundException e1) {
			//apertura del file fallita
			e1.printStackTrace();
		} catch (IOException e) {
			//lettura del file o chiusura del file fallita
			e.printStackTrace();
		}
	}
	
	public static Query getInstance(){
		if(instance==null){
			instance=new Query();
		}
		return instance;
	}
	
	public String getQuery(String queryName){
		return queries.get(queryName);
	}
}
