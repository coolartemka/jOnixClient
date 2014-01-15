package fi.metropolia.ereading;

import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 * Backing bean responsible for storing client session information. 
 * Stores the login information, and current page
 * @author artemka
 */
@ManagedBean(name="clientSession")
@SessionScoped
public class UserSession {
	
	private String page = "login";
	private boolean logged = false;
	private String login = null;
	
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public boolean isLogged() {
		return logged;
	}
	public void setLogged(boolean logged) {
		this.logged = logged;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}	
	
	public void logout(ActionEvent event) {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	}
	
}
