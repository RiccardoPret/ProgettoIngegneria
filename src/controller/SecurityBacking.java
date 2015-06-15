package controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@ManagedBean
@ViewScoped
public class SecurityBacking {
	
	public String invalidateSession(){
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "/client/index.jsf?faces-redirect=true";
	}
	
	public String getWelcome(){
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		
		return "Buongiorno "+request.getUserPrincipal().getName();
	}
	
	public String logout() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

		try {
			request.logout();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "/client/index.jsf?faces-redirect=true";
	}
}