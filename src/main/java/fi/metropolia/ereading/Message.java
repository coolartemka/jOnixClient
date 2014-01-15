package fi.metropolia.ereading;

import java.util.Date;

import org.editeur.ns.onix._3_0.reference.ONIXMessage;


/**
 * Bean to store the data about a particular ONIX message.
 * Displayed in the browse.xhtml.
 * @author Artem Moskalev
 *
 */
public class Message {

	private String sender;
	private String email;
	private String note;
	private Date time;
	private ONIXMessage fullMessage;
	
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public ONIXMessage getFullMessage() {
		return fullMessage;
	}
	public void setFullMessage(ONIXMessage fullMessage) {
		this.fullMessage = fullMessage;
	}
	
}
