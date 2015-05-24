package main.java.hello;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

//import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerConnector;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import scala.collection.immutable.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

@Component
public class Scheduler {

	  
	@Scheduled(fixedRate = 1000*60*5)
	public void schedule()
	{
  		try
  		{
  			//Kafka Connectivity Code
  			Properties properties = new Properties();
  			properties.put("metadata.broker.list", "54.149.84.25:9092");
  		    properties.put("serializer.class", "kafka.serializer.StringEncoder");
  		    properties.put("request.required.acks", "1");
  		    Producer<Integer, String>  producer=new Producer<>(new ProducerConfig(properties));
  			String topic="cmpe273-topic";
  		    KeyedMessage<Integer, String> data;		
  			
  		    
  		    //Mongo Connectivity Code
  		    String textUri = "mongodb://viraj:12345@ds043981.mongolab.com:43981/cmpe273";
			MongoClientURI uri = new MongoClientURI(textUri);
			MongoClient mongo = new MongoClient(uri);
			DB db = mongo.getDB(uri.getDatabase());	
			DBCollection polls = db.getCollection("polls");
			DBCursor docs = polls.find();
			while(docs.hasNext())
			{
				DBObject doc=docs.next();
				String expiredAt=doc.get("expired_at").toString();
				expiredAt=expiredAt.substring(0,19);
				expiredAt=expiredAt.replaceAll("-","/");
				expiredAt=expiredAt.replaceAll("T", " ");
				Date exp=new Date(expiredAt);
				Date current=new Date();
				if(exp.before(current))
				{
					String id=doc.get("id").toString().substring(0,6);
					System.out.println("The id is"+id);
				  	String email=getEmail(id);
				  	ArrayList<String> choice=(ArrayList<String>)doc.get("choice");
				  	ArrayList<Integer> results=(ArrayList<Integer>)doc.get("results");
				  	String msg="dudhaneviraj@gmail.com"+":010027654:Poll Result["+choice.get(0)+"="+results.get(0)+","+choice.get(1)+"="+results.get(1)+"]";				
				  	data= new KeyedMessage<>(topic, msg);
				  	producer.send(data);
				  	
				}
			}
  			producer.close(); 
  		}
  		catch(Exception e)
  		{
  			System.err.println( "The Exception is:"+e.getClass().getName() + ": " + e.getMessage() );
  		}  	
	}		
	
	
	
	



	
	
	
	
	
	
	
	
	
	public static String getEmail(String id)
	{
			
	    try
	    {
	    		//Connectivity Code
	    	String textUri = "mongodb://viraj:12345@ds043981.mongolab.com:43981/cmpe273";
	   		MongoClientURI uri = new MongoClientURI(textUri);
	   		MongoClient mongo = new MongoClient(uri);
	   		DB db = mongo.getDB(uri.getDatabase());	
	   		DBCollection moderator = db.getCollection("moderator");
   	    
	    		//Search Code	    	
    		DBCursor docs = moderator.find();
	    
	    	while(docs.hasNext())
	    	{
	    		DBObject doc = docs.next();
	    		
	    		if(doc.get("id").toString().equals(id))
	    		{
	    			
	    			return doc.get("email")+"";
	    	
	    		}
	    	}
	      }
	     catch(Exception e)
	     {
	    		System.err.println( e.getClass().getName() + ": " + e.getMessage());
	      }
		   return null;
		}	
}
