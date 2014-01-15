package fi.metropolia.ereading;

import java.util.UUID;

import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.faces.event.*;
import com.mongodb.*;

/**
 * Backing bean used in the account setting page of the JSF application
 * @author Artem Moskalev
 */
@ManagedBean
@RequestScoped
public class AccountSettings {
	
	/* organization part */
	private String addressee;
	@ManagedProperty(value = "#{clientSession}")
	private UserSession clientSession;
	
	/* password part */
	private String newPassword;
	private String newPasswordRepeat;
		
	/* registering the organization name */
	public String getAddressee() {
		return addressee;
	}
	public void setAddressee(String addressee) {
		this.addressee = addressee;
	}
	/* method to show tool tip with data about current addressee name */
	public String getAddresseeShow() {
		DBCollection users = MongoResource.getClient().getDB("jOnix").getCollection("users");
		DBObject temp = users.findOne(new BasicDBObject("login", clientSession.getLogin()));
		if (temp.get("addresseeName") != null) {
			return temp.get("addresseeName").toString();
		} else {
			return null;
		}
	}
		
	public void setClientSession(UserSession navigation) {
		this.clientSession = navigation;
	}	
	public void registerContact(ActionEvent event) {
		DBCollection users = MongoResource.getClient().getDB("jOnix").getCollection("users");
		users.update(new BasicDBObject("login", clientSession.getLogin()), new BasicDBObject("$set", 
																			new BasicDBObject("addresseeName", addressee)));
		FacesContext.getCurrentInstance()
					.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Organization Name has been saved!", "Saved!"));
	}
	
	/* registering new password for the private space */
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getNewPasswordRepeat() {
		return newPasswordRepeat;
	}
	public void setNewPasswordRepeat(String newPasswordRepeat) {
		this.newPasswordRepeat = newPasswordRepeat;
	}	
	public void changePassword(ActionEvent event) {
		if (newPassword.equals(newPasswordRepeat)) {
			DBCollection users = MongoResource.getClient().getDB("jOnix").getCollection("users");
			users.update(new BasicDBObject("login", clientSession.getLogin()), new BasicDBObject("$set", 
																				new BasicDBObject("password", newPassword)));
			FacesContext.getCurrentInstance()
				.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Your password has been updated", "Updated!"));
		} else {
			FacesContext.getCurrentInstance()
			.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Your passwords in check do not match", "No Match!"));
		}
	}
	
	/* part which deals with generating random key for the service */
	public String getKey() {
		DBCollection users = MongoResource.getClient().getDB("jOnix").getCollection("users");
		DBObject user = users.findOne(new BasicDBObject("login", clientSession.getLogin())); 
		if (user == null || user.get("key") == null) {
			return "none";
		} else {
			return user.get("key").toString();
		}
	}
	public void generateKey(ActionEvent event) {		
		String key = UUID.randomUUID().toString();
		DBCollection users = MongoResource.getClient().getDB("jOnix").getCollection("users");
		users.update(new BasicDBObject("login", clientSession.getLogin()),  
					 new BasicDBObject("$set", new BasicDBObject("key", key)));
	}		
	
}
