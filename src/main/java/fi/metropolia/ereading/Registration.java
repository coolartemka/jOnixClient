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
	@ManagedProperty(value="#{navigation}")
	private Navigation navigation;
	
	public void register(ActionEvent event) {
		DBCollection usersCollection = MongoResource.getClient().getDB("jOnix").getCollection("users");
		DBObject user = usersCollection.findOne(new BasicDBObject("login", login));
		if(user == null) {
			usersCollection.insert(new BasicDBObject("login", login).append("password", password));
			navigation.setLogged(true);
			navigation.setLogin(login);
			navigation.setPage("home");
		} else {
			FacesContext.getCurrentInstance()
				.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Such user already exists", "Login taken"));
		}	
	}
	public void login(ActionEvent event) {
		DBCollection usersCollection = MongoResource.getClient().getDB("jOnix").getCollection("users");
		DBObject user = usersCollection.findOne(new BasicDBObject("login", login).append("password", password));
		if(user == null) {
			FacesContext.getCurrentInstance()
			.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong Credentials", "Login-Passowrd pair wrong"));
		} else {
			navigation.setLogged(true);
			navigation.setPage("home");
			navigation.setLogin(login);
		}		
	}
	public String proceed() {
		if(navigation.isLogged()) {
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
	public Navigation getNavigation() {
		return navigation;
	}
	public void setNavigation(Navigation navigation) {
		this.navigation = navigation;
	}	

}
