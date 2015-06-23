package Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import controller.DataSource;

public class ThreadInvioMail implements Runnable {
	Socket socket;
	int id;

	public ThreadInvioMail(Socket socket) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
	}

	@Override
	public void run() {

		try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));) {
			while (!in.ready()) {
			}
			id = Integer.parseInt((in.readLine()));

			String mail = (new DataSource()).getMail(id);
			// TODO Auto-generated method stub
			// Recipient's email ID needs to be mentioned.

			Email email = new SimpleEmail();
			email.setHostName("smtp.gmail.com");
			email.setSmtpPort(587);
			email.setAuthenticator(new DefaultAuthenticator("driveitunivr@gmail.com ", "pifsoldi"));
			email.setStartTLSEnabled(true);
			email.setFrom("driveitunivr@gmail.com ");
			email.setSubject("Notifica DriveIT");
			email.setMsg("test Mail");
			email.addTo(mail);
			email.send();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
