package fi.metropolia.ereading;

import java.net.UnknownHostException;
import com.mongodb.MongoClient;

/**
 * Class responsible for getting mongoDB client connections
 * @author Artem Moskalev
 *
 */
public class MongoResource {

	private static MongoClient client;

	static {
		try {
			client = new MongoClient("localhost", 27017);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}		
	}
	
	/* retrieves Mongo client */
	public static MongoClient getClient() {
		return client;
	}
	
}
