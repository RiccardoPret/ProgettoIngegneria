# ProgettoIngegneria
Progetto di Ingegneria


Aggiunte:
	Creare jaas.config nella cartella config di Tomcat con dentro il seguente codice:
		Inge{
			login.MyLoginModule required debug=true;
		};
	
	Aggiungere la seguente stringa nel server Tomcat -Djava.security.auth.login.config="path-jaas.config"
