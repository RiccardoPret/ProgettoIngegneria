package controller;


public class Query {

	private static Query instance;
	
	private static ReservedReader qReader;

	private static String queryFile="query.txt";
	private static String separator = "=>";

	private Query(){		
		super();
	}

	public static Query getInstance() {
		if (instance == null) {
			instance = new Query();
			qReader= new ReservedReader(Query.instance, queryFile, separator);
		}
		return instance;
	}

	public String getQuery(String queryName) {
		return qReader.getValue(queryName);
	}
}
