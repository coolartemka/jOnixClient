package fi.metropolia.ereading;

import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import com.mongodb.*;

@ManagedBean
@RequestScoped
public class Registration {

	private String login;
	private String password;
	private boolean logged = false;
	
	public void register(ActionEvent arg0) {
		DBCollection usersCollection = MongoResource.getClient().getDB("jOnix").getCollection("users");
		DBObject user = usersCollection.findOne(new BasicDBObject("login", login));
		if(user == null) {
			usersCollection.insert(new BasicDBObject("login", login).append("password", password));
			logged = true;
			Navigation nav = (Navigation)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("navigation");
			nav.setLogged(logged);
			nav.setPage("home");
			nav.setLogin(login);
		} else {
			FacesContext.getCurrentInstance()
				.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Such user already exists", "Login taken"));
		}	
	}
	public void login(ActionEvent arg0) {
		DBCollection usersCollection = MongoResource.getClient().getDB("jOnix").getCollection("users");
		DBObject user = usersCollection.findOne(new BasicDBObject("login", login).append("password", password));
		if(user == null) {
			FacesContext.getCurrentInstance()
			.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong Credentials", "Login-Passowrd pair wrong"));
		} else {
			logged = true;
			Navigation nav = (Navigation)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("navigation");
			nav.setLogged(logged);
			nav.setPage("home");
			nav.setLogin(login);
		}		
	}
	public String proceed() {
		if(logged) {
			return "personal";
		} else {
			return null;
		}		
	}
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isRegistered() {
		return logged;
	}
	public void setRegistered(boolean logged) {
		this.logged = logged;
	}	

}
