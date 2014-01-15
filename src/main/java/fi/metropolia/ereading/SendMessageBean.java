package fi.metropolia.ereading;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.faces.event.*;

import org.apache.http.client.methods.*;
import org.apache.http.entity.*;
import org.apache.http.impl.client.*;

/**
 * Backing bean used in the send-part of the JSF application. 
 * Contains the details of the message to send to the local instance of the service
 * @author Artem Moskalev
 */
@ManagedBean
@RequestScoped
public class SendMessageBean {

	private String message;
	private static final String JONIX_SERVICE = "http://localhost:8080/jonix/send";
	@ManagedProperty(value="#{accountSettings}")
	private AccountSettings settings;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
		
	public AccountSettings getSettings() {
		return settings;
	}
	public void setSettings(AccountSettings settings) {
		this.settings = settings;
	}
	
	public void sendMessage(ActionEvent event) {
		CloseableHttpClient client = HttpClients.createDefault();		
		try {
			StringEntity data = new StringEntity(message, "utf-8");			
			HttpPost post = new HttpPost(JONIX_SERVICE + "?key=" + settings.getKey());
			post.setHeader("Content-Type", "application/xml");
			post.setEntity(data);					
			CloseableHttpResponse response = client.execute(post);
			int code = response.getStatusLine().getStatusCode();
			switch(code) {
				case 202:
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
							"Your ONIX Message has been sent successfully!", "Sent!"));
					break;
				default:
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
							"Your ONIX Message could not be sent! Check its validity.", "Sent!"));
					break;	
			}			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	
		
}
