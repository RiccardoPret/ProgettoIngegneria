package controller;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

import util.Configurazione;
import util.Dispositivo;
import util.User;

/**
 * Questa classe mette a disposizione i metodi per effettuare interrogazioni
 * sulla base di dati.
 */
public class ClientDataSource implements Serializable {

	// === Properties

	// dati di identificazione dell'utente (da personalizzare)
	private DatabaseDriver driver;

	// -- definizione delle query
	private String css = "SELECT DISTINCT CS.id, CS.codice, CS.nome "
			+ "FROM InsErogato IE, CorsoStudi CS, Docente D, Facolta F, Persona P "
			+ "WHERE IE.id_corsostudi=CS.id AND IE.id_facolta=F.id AND D.id_inserogato=IE.id AND D.id_persona=P.id ";
	// +"AND IE.annoaccademico='2013/2014' AND IE.hamoduli='0' AND P.cognome='Segala' AND CS.nome ILIKE '%i%' AND F.nome='Scienze matematiche fisiche e naturali'";

	private String cs = "SELECT id,nome,codice,abbreviazione,durataanni,sede,informativa "
			+ "FROM corsostudi " + "WHERE id=?";

	private String csf = "SELECT DISTINCT f.nome "
			+ "FROM facolta f INNER JOIN corsoinfacolta csf "
			+ "ON (f.id=csf.id_facolta) " + "WHERE csf.id_corsostudi=?";

	// === Methods

	public ClientDataSource() {
		driver = DatabaseDriver.getInstance();
	}

	/*
	 * private CorsoStudi makeCorsoStudiBean(ResultSet rs) throws SQLException {
	 * CorsoStudi bean = new CorsoStudi(); bean.setId(rs.getInt("id"));
	 * bean.setNomeCorsoStudi(rs.getString("Nome"));
	 * bean.setCodice(rs.getString("Codice"));
	 * bean.setAbbreviazione(rs.getString("Abbreviazione"));
	 * bean.setDurataAnni(rs.getInt("Durataanni"));
	 * bean.setSede(rs.getString("Sede"));
	 * bean.setInformativa(rs.getString("Informativa")); return bean; }
	 * 
	 * private CorsoStudi makeCSBean(ResultSet rs) throws SQLException {
	 * CorsoStudi bean = new CorsoStudi(); bean.setId(rs.getInt("id"));
	 * bean.setNomeCorsoStudi(rs.getString("Nome"));
	 * bean.setCodice(rs.getString("Codice")); return bean; }
	 * 
	 * //
	 * ========================================================================
	 * ===
	 * 
	 * 
	 * public CorsoStudi getCorsoStudi(int id) { // Dichiarazione delle
	 * variabili necessarie Connection con = null; PreparedStatement pstmt =
	 * null; ResultSet rs = null; CorsoStudi result = null; try { // tentativo
	 * di connessione al database con = DriverManager.getConnection(url, user,
	 * passwd); // connessione riuscita, ottengo l'oggetto per l'esecuzione //
	 * dell'interrogazione. pstmt = con.prepareStatement(cs);
	 * pstmt.clearParameters(); // imposto i parametri della query
	 * pstmt.setInt(1, id); // eseguo la query rs = pstmt.executeQuery(); //
	 * memorizzo il risultato dell'interrogazione in Vector di Bean if
	 * (rs.next()) { result = makeCorsoStudiBean(rs); }
	 * 
	 * } catch (SQLException sqle) { // Catturo le eventuali eccezioni
	 * sqle.printStackTrace();
	 * 
	 * } finally { // alla fine chiudo la connessione. try { con.close(); }
	 * catch (SQLException sqle1) { sqle1.printStackTrace(); } } return result;
	 * }
	 * 
	 * // +
	 * "AND IE.annoaccademico='2013/2014' AND IE.hamoduli='0' AND P.cognome='Segala' AND CS.nome ILIKE '%i%' AND F.nome='Scienze matematiche fisiche e naturali'"
	 * ;
	 * 
	 * 
	 * public List<CorsoStudi> getCorsiStudi(String aa, String facolta, String
	 * nome, String hamoduli, String docente) { // dichiarazione delle variabili
	 * Connection con = null; Statement stmt = null; ResultSet rs = null;
	 * List<CorsoStudi> result = new ArrayList<CorsoStudi>();
	 * 
	 * if (!aa.isEmpty()) css += "AND IE.annoaccademico='" + aa + "' "; if
	 * (!facolta.isEmpty()) css += "AND F.nome='" + facolta + "' "; if
	 * (!nome.isEmpty()) css += "AND CS.nome ILIKE '%" + nome + "%' "; if
	 * (!hamoduli.isEmpty()) css += "AND IE.hamoduli='" + hamoduli + "' "; if
	 * (!docente.isEmpty()) css += "AND P.cognome='" + docente + "' ";
	 * 
	 * try { PrintWriter writer = new PrintWriter("the-file-name.txt", "UTF-8");
	 * writer.println(css); writer.close(); } catch (Exception e) {
	 * 
	 * }
	 * 
	 * try { // tentativo di connessione al database con =
	 * DriverManager.getConnection(url, user, passwd); // connessione riuscita,
	 * ottengo l'oggetto per l'esecuzione // dell'interrogazione. stmt =
	 * con.createStatement(); // eseguo l'interrogazione desiderata rs =
	 * stmt.executeQuery(css); // memorizzo il risultato dell'interrogazione nel
	 * Vector while (rs.next()) { result.add(makeCSBean(rs)); }
	 * 
	 * } catch (SQLException sqle) { // catturo le eventuali eccezioni!
	 * sqle.printStackTrace();
	 * 
	 * } finally { // alla fine chiudo la connessione. try { con.close(); }
	 * catch (SQLException sqle1) { sqle1.printStackTrace(); } }
	 * 
	 * // reset css = "SELECT DISTINCT CS.id, CS.codice, CS.nome " +
	 * "FROM InsErogato IE, CorsoStudi CS, Docente D, Facolta F, Persona P " +
	 * "WHERE IE.id_corsostudi=CS.id AND IE.id_facolta=F.id AND D.id_inserogato=IE.id AND D.id_persona=P.id "
	 * ;
	 * 
	 * return result; }
	 * 
	 * 
	 * public String getFacoltaCorso(int id) { // dichiarazione delle variabili
	 * Connection con = null; PreparedStatement pstmt = null; ResultSet rs =
	 * null; String result = null;
	 * 
	 * try { // tentativo di connessione al database con =
	 * DriverManager.getConnection(url, user, passwd); // Connessione riuscita,
	 * ottengo l'oggetto per l'esecuzione // dell'interrogazione. pstmt =
	 * con.prepareStatement(csf); pstmt.clearParameters(); pstmt.setInt(1, id);
	 * rs = pstmt.executeQuery();
	 * 
	 * // memorizzo il risultato dell'interrogazione nel Bean if (rs.next()) {
	 * result = rs.getString("Nome"); }
	 * 
	 * } catch (SQLException sqle) { // catturo le eventuali eccezioni!
	 * sqle.printStackTrace();
	 * 
	 * } finally { // alla fine chiudo la connessione. try { con.close(); }
	 * catch (SQLException sqle1) { sqle1.printStackTrace(); } } return result;
	 * }
	 */

	/*
	 * ritorna il dispositivo a partire dall'username
	 
	public Dispositivo getDispositivoFromUser(String username) {
		driver.openConnection();
		Dispositivo device= driver.getDispositivo(username);
		driver.closeConnection();
		return device;
	}
*/
	public Configurazione getConfigurazione(Dispositivo dispositivo) {
		driver.openConnection();
		Configurazione conf= driver.getConfigurazione(dispositivo);
		driver.closeConnection();
		return conf;
	}

	public User getUser(String utente) {
		driver.openConnection();
		User user= driver.getUser(utente);
		driver.closeConnection();
		return user;
	}

	public void updateDbInstance(User client, Configurazione config) {
		driver.openConnection();
		driver.updateProfile(client);
		driver.updateSetting(config);
		driver.closeConnection();
	}

}
