package fi.metropolia.ereading;

import java.util.*;

import org.editeur.ns.onix._3_0.reference.ProductIdentifier;

/**
 * Bean storing the data about a particular ONIX product. 
 * Displayed in the browse.xhtml.
 * @author Artem Moskalev
 */
public class Record {

	private String reference;
	private String notificationType;
	private List<ProductIdentifier> ids = new ArrayList<ProductIdentifier>();
	
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getNotificationType() {
		return notificationType;
	}
	public List<ProductIdentifier> getIds() {
		return this.ids;
	}
	public void addId(ProductIdentifier id) {
		ids.add(id);
	}
	public void setNotificationType(String notificationType) {
		if(notificationType.equals("01")) {
			this.notificationType = "Early Update (01)";
		} else if(notificationType.equals("02")) {
			this.notificationType = "Confirmed Notification Update (02)";
		} else if(notificationType.equals("03")) {
			this.notificationType = "Confirmed Publication Update (03)";
		} else if(notificationType.equals("04")) {
			this.notificationType = "Partial Update (04)";
		} else {
			this.notificationType = "Message Purpose Not Supported";
		}
	}
	
}
