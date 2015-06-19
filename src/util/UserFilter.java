package util;

public class UserFilter {
	private String username;
	private Integer idDispositivo;
	private String email;
	private String telefono;

	public UserFilter() {
		this.username = "";
		this.idDispositivo = null;
		this.email = "";
		this.telefono = "";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getIdDispositivo() {
		return idDispositivo;
	}

	public void setIdDispositivo(Integer idDispositivo) {
		this.idDispositivo = idDispositivo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public boolean isSetted() {
		return this.username != "" || this.idDispositivo != null
				|| this.email != "" || this.telefono != "";
	}

}
