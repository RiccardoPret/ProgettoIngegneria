package controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class Query {

	private static Query instance;
	private HashMap<String, String> queries;
	private String queryFile="query.txt";
	private String separator = "=>";

	private Query(){
		queries= new HashMap<String, String>();
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(getQueryPath()));
			String line = "";
			while ((line = in.readLine()) != null) {
			    String parts[] = line.split(separator);
			    queries.put(parts[0], parts[1]);
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

	public static Query getInstance() {
		if (instance == null) {
			instance = new Query();
		}
		System.out.println(instance.getQuery("query_checkUser"));
		return instance;
	}


	private String getClassName() {
		String s = this.getClass().getName();

		s = s.substring(s.lastIndexOf(".") + 1);
		s += ".class";
		return s;
	}

	private String getQueryPath() {
		Path path = null;

		try {
			path = Paths.get(this.getClass().getResource(getClassName())
					.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		while (!(path = path.getParent()).getFileName().toString()
				.equals("WEB-INF"));
		return path.toString()+"/reserved/"+this.queryFile;
	}

	
	public String getQuery(String queryName) {
		return queries.get(queryName);
	}
}
