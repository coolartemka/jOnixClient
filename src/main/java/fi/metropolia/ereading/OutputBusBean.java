package fi.metropolia.ereading;

import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import com.mongodb.*;

/**
 * Backing bean used in the output bus JSF page. 
 * Can temporarily store the http-address and method of the destination.
 * @author Artem Moskalev
 */
@ManagedBean
@RequestScoped
public class OutputBusBean {

	private String address;
	private String method;
	@ManagedProperty(value="#{clientSession}")
	private UserSession clientSession;
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public UserSession getClientSession() {
		return this.clientSession;
	}
	public void setClientSession(UserSession clientSession) {
		this.clientSession = clientSession;
	}
	public String getShowMethod() {
		DBCollection users = MongoResource.getClient().getDB("jOnix").getCollection("users");		
		DBObject queryResult = users.findOne(new BasicDBObject("login", clientSession.getLogin()));
		if(queryResult.get("outputBusMethod") != null) {
			return queryResult.get("outputBusMethod").toString();
		} else {
			return null;
		}
	}
	public String getShowAddress() {
		DBCollection users = MongoResource.getClient().getDB("jOnix").getCollection("users");		
		DBObject queryResult = users.findOne(new BasicDBObject("login", clientSession.getLogin()));
		if(queryResult.get("outputBusAddress") != null) {
			return queryResult.get("outputBusAddress").toString();
		} else {
			return null;
		}
	}
	
	public void addAddress(ActionEvent event) {
		DBCollection users = MongoResource.getClient().getDB("jOnix").getCollection("users");
		
		DBObject queryResult = users.findOne(new BasicDBObject("outputBusAddress", address));
		if(queryResult != null && !queryResult.get("login").toString().equals(clientSession.getLogin())) {
			FacesContext.getCurrentInstance()
				.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "This Output Bus Address is already in use!", "Used!"));
			return;
		}		
		users.update(new BasicDBObject("login", clientSession.getLogin()), new BasicDBObject("$set", 
																						  new BasicDBObject("outputBusAddress", address)));
		users.update(new BasicDBObject("login", clientSession.getLogin()), new BasicDBObject("$set", 
				  																		  new BasicDBObject("outputBusMethod", method)));
		users.update(new BasicDBObject("login", clientSession.getLogin()), new BasicDBObject("$set", 
																							new BasicDBObject("outputBusValid", true)));
		FacesContext.getCurrentInstance()
						.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Ouput Bus Address has been saved!", "Saved!"));
	}
	
}
