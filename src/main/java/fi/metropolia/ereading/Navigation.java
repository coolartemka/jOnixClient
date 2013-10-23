package fi.metropolia.ereading;

import javax.faces.bean.*;

@ManagedBean
@SessionScoped
public class Navigation {

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
	
}
