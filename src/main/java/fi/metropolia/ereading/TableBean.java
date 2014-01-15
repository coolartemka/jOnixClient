package fi.metropolia.ereading;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.faces.bean.*;
import javax.xml.bind.*;
import javax.xml.bind.JAXBContext;

import org.eclipse.persistence.jaxb.*;
import org.editeur.ns.onix._3_0.reference.*;

import com.mongodb.*;
import com.mongodb.util.JSON;

/**
 * Backing bean used in the browse.xhtml. Finds and retrieves the ONIX message headers, targeted at the user of the 
 * platform, from the mongoDB database
 * @author artemka
 */
@ManagedBean
@ViewScoped
public class TableBean {
	
	private JAXBContext jsonContext;
	private Message selectedMessage;
	@ManagedProperty(value = "#{clientSession}")
	private UserSession clientSession;
	
	public void setClientSession(UserSession clientSession) {
		this.clientSession = clientSession;
	}
	
	{
		try {
			jsonContext = JAXBContextFactory.createContext(new Class[] {ONIXMessage.class}, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public Message getSelectedMessage() {
		return selectedMessage;
	}
	public void setSelectedMessage(Message selectedMessage) {
		this.selectedMessage = selectedMessage;
	} 
	public List<Record> getRecords() {
		List<Record> records = new LinkedList<Record>();
		if(selectedMessage == null) {			
			return records;
		}
		for (Product product : selectedMessage.getFullMessage().getProduct()) {
			Record record = new Record();
			record.setReference(product.getRecordReference().getValue().trim());
			record.setNotificationType(product.getNotificationType().getValue().trim());
			for(ProductIdentifier id : product.getProductIdentifier()) {
				record.addId(id);
			}
			records.add(record);
		}
		return records;		
	}
	public List<Message> getHeaders() {
	
		DBCollection userCollection = MongoResource.getClient().getDB("jOnix").getCollection("users");
		java.lang.Object temp = userCollection.findOne(new BasicDBObject("login", clientSession.getLogin())).get("addresseeName");
		String addressee = null;
		
		if(temp != null) {
			addressee = temp.toString();
		} else {
			return null;
		}		
		List<ONIXMessage> messages = new ArrayList<ONIXMessage>();
				
		/* first find the headers for the addressee */
		DBCollection headersCollection = MongoResource.getClient().getDB("jOnix").getCollection("headers");
		Unmarshaller unmarshaller = null;
		try {
			unmarshaller = jsonContext.createUnmarshaller();
			unmarshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
		} catch (JAXBException e) {
			e.printStackTrace();
		}		
		
		BasicDBList nameList = new BasicDBList();
		nameList.put(0, addressee);
		BasicDBObject name = new BasicDBObject("$all", nameList);		
		DBCursor cursor = headersCollection.find(new BasicDBObject("item.Header.Addressee.AddresseeName", name));
		
		/* now look for product information associated with every employee */
		
		DBCollection productsCollection = MongoResource.getClient().getDB("jOnix").getCollection("products");
		while(cursor.hasNext()) {
			ONIXMessage message = new ONIXMessage();
			DBObject tempHeader = cursor.next();
			
			Header header = null;
			try {
				header = (Header)unmarshaller.unmarshal(new StringReader(JSON.serialize(tempHeader.get("item"))));
			} catch (JAXBException e) {
				e.printStackTrace();
			}
			message.setHeader(header);
			
			java.lang.Object key = tempHeader.get("_id");
			DBCursor productCursor = productsCollection.find(new BasicDBObject("refer", key));
			
			while(productCursor.hasNext()) {
				DBObject tempProduct = productCursor.next();
				try {
					Product product = (Product)unmarshaller.unmarshal(new StringReader(JSON.serialize(tempProduct.get("item"))));
					message.getProduct().add(product);
				} catch (JAXBException e) {
					e.printStackTrace();
				}
			}
			messages.add(message);
		}				
		
		/* parsing ONIX messages to receive the required information*/
		List<Message> entries = new ArrayList<Message>();
		
		for(ONIXMessage message : messages) {
			Message entry = new Message();
			java.util.Date time = null;
			SimpleDateFormat format = new SimpleDateFormat();
			format.applyPattern("yyyyMMdd'T'HHmm");
			try {
				String result = message.getHeader().getSentDateTime().getValue().split("-")[0];
				time = format.parse(result);
			} catch (ParseException e) {
				e.printStackTrace();
			}			
			String note = message.getHeader().getMessageNote().get(0).getValue();
			String sender = null;
			String email = null;
			for(java.lang.Object o : message.getHeader().getSender().getContent()) {
				if(o instanceof SenderName) {
					sender = ((SenderName)o).getValue();
				}
				if(o instanceof EmailAddress) {
					email = ((EmailAddress)o).getValue();
				}
			}
			entry.setEmail(email);
			entry.setSender(sender);
			entry.setNote(note);
			entry.setTime(time);
			entry.setFullMessage(message);
			entries.add(entry);
		}
		
		return entries;
	}	
	
}
