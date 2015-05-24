package main.java.hello;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.HttpServletBean;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.stereotype.Component;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import java.util.*;
import javax.print.attribute.standard.DateTimeAtCreation;
import javax.validation.Valid;

@RestController
@Configuration
@ComponentScan
//@EnableAutoConfiguration(exclude=MongoAutoConfiguration.class)

public class Controller {
	
		static int m_id=123456;
		
	    //1.Create Moderator
	    
	    @RequestMapping(value="/api/v1/moderators" ,method=RequestMethod.POST)
	    @ResponseBody
	    @ResponseStatus(value=HttpStatus.CREATED)
	    public Moderator controller1(@Valid @RequestBody ModeratorRequest m)
	    {
	    	SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");	    	 
	    	Date curDate = new Date();
	    	String date = format.format(curDate);      
            
	    	try
	    	{   
	    		//Connection code
	    		String textUri = "mongodb://viraj:12345@ds043981.mongolab.com:43981/cmpe273";
	    		MongoClientURI uri = new MongoClientURI(textUri);
	    		MongoClient mongo = new MongoClient(uri);
	    	    DB db = mongo.getDB(uri.getDatabase());	
	    	    DBCollection moderator = db.getCollection("moderator");
	    	    
	    	    //Insert Code
	    	    BasicDBObject record = new BasicDBObject();
	            record.put("id", m_id);
	            record.put("name", m.name);
	            record.put("email", m.email);
	            record.put("password", m.password);
	            record.put("created_at", date);
	            moderator.insert(record); 
	           	}
	    	catch(Exception e)
	    	{
	    	     System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	    	}  

        	Moderator mod=new Moderator();
	    	mod.id=m_id;
	    	m_id++;
	    	mod.name=m.name;
	    	mod.email=m.email;
	    	mod.password=m.password;
	        mod.created_at=date ;
	    	return mod;
    
	    	
	    }
	    
	    //2.View Moderators
	    @RequestMapping(value="/api/v1/moderators/{moderator_id}" ,method=RequestMethod.GET)
	    @ResponseBody
	    @ResponseStatus(value=HttpStatus.OK)
	    public Moderator controller2(@PathVariable(value="moderator_id")int id)
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
		    		BasicDBObject findQuery = new BasicDBObject("id", id);	    	
		    		DBCursor docs = moderator.find(findQuery);
		    		DBObject doc = docs.next();
		    		if(doc.containsField("id"))
		    		{
		    			Moderator mod=new Moderator();
		    			mod.id= Integer.parseInt(doc.get("id")+"");
		    			mod.name=doc.get("name")+"";
		    			mod.email=doc.get("email")+"";
		    			mod.password=doc.get("password")+"";
		    			mod.created_at=doc.get("created_at")+"";
		    			return mod;
		    		}
		    	  
		    	}
		    	catch(Exception e)
		    	{
		    		System.err.println( e.getClass().getName() + ": " + e.getMessage());
		    	}
	    	return null;
	    }
	    	
	    //3. Update Moderator
	    @RequestMapping(value="/api/v1/moderators/{moderator_id}" ,method=RequestMethod.PUT)
	    @ResponseBody
	    @ResponseStatus(value=HttpStatus.OK)
	    public Moderator controller3(@PathVariable(value="moderator_id")int id,@Valid @RequestBody ModeratorRequest3 m)
	    {
	    		try
    		{
	    		//Connectivity Code
	    		String textUri = "mongodb://viraj:12345@ds043981.mongolab.com:43981/cmpe273";
    			MongoClientURI uri = new MongoClientURI(textUri);
    			MongoClient mongo = new MongoClient(uri);
    			DB db = mongo.getDB(uri.getDatabase());	
    			DBCollection moderator = db.getCollection("moderator");
	    
    			//Update Code
    			BasicDBObject updateQuery = new BasicDBObject("id",id);
    			moderator.update(updateQuery, new BasicDBObject("$set", new BasicDBObject("password",m.password)));
    		
    			//Return Result Object
    			BasicDBObject findQuery = new BasicDBObject("id", id);	    	
    			DBCursor docs = moderator.find(findQuery);
    			DBObject doc = docs.next();
    			if(doc.containsField("id"))
    			{
    				Moderator mod=new Moderator();
    				mod.id= Integer.parseInt(doc.get("id")+"");
    				mod.name=doc.get("name")+"";
    				mod.email=doc.get("email")+"";
    				mod.password=doc.get("password")+"";
    				mod.created_at=doc.get("created_at")+"";
    				return mod;
    			}
    		
    		}
    		catch(Exception e)
    		{
    			System.err.println( e.getClass().getName() + ": " + e.getMessage());
    		}
    		
    		
	   	    	return null;
	     }
	    
	    
	    //4.Create a Poll
	    @RequestMapping(value="/api/v1/moderators/{moderator_id}/polls" ,method=RequestMethod.POST)
	    @ResponseBody
	    @ResponseStatus(value=HttpStatus.CREATED)
	    public PollWithoutResult controller4(@PathVariable(value="moderator_id")int id,@Valid @RequestBody PollRequest4 p)
	    {
	    	String str=new String();
	    	try
	    	{
	    		//Connectivity Code
	    		String textUri = "mongodb://viraj:12345@ds043981.mongolab.com:43981/cmpe273";
    			MongoClientURI uri = new MongoClientURI(textUri);
    			MongoClient mongo = new MongoClient(uri);
    			DB db = mongo.getDB(uri.getDatabase());	
    			DBCollection polls = db.getCollection("polls");
    			
    			//Count code For Unique Id	    	
    			int count=(int)polls.count();
    			count++;
    			str=id+""+count;
    			
    			
    			Integer[] i=new Integer[p.choice.size()];
    			for(int j=0;j<i.length;j++)
    			{
    				i[j]=0;
    			}
    			//Insert Code
    		    BasicDBObject record = new BasicDBObject();
	            record.put("id", str);
	            record.put("question", p.question);
	            record.put("started_at", p.started_at);
	            record.put("expired_at", p.expired_at);
	            record.put("choice", p.choice);
	            record.put("results", i);
	            polls.insert(record); 
	    	}
	    	catch(Exception e)
	    	{
	    		System.err.println( e.getClass().getName() + ": " + e.getMessage());
	    	}
	    	
	        PollWithoutResult pollWithoutResultTemp=new PollWithoutResult();
	    	
	    			
	    	pollWithoutResultTemp.id=str;
	    	pollWithoutResultTemp.question=p.question;
	    	pollWithoutResultTemp.started_at=p.started_at;
	    	pollWithoutResultTemp.choice=p.choice;
	    	pollWithoutResultTemp.expired_at=p.expired_at;
	    	return pollWithoutResultTemp;
	    }	
	    
	   
	  //5.View A Poll Without Result
	    @RequestMapping(value="/api/v1/polls/{poll_id}" ,method=RequestMethod.GET)
	    @ResponseBody
	    @ResponseStatus(value=HttpStatus.OK)
	    public PollWithoutResult controller5(@PathVariable(value="poll_id")String id)
	    {
	    	PollWithoutResult pw=new PollWithoutResult();
		    
	    	try
	    	{
	    		//Connectivity Code
	    		String textUri = "mongodb://viraj:12345@ds043981.mongolab.com:43981/cmpe273";
    			MongoClientURI uri = new MongoClientURI(textUri);
    			MongoClient mongo = new MongoClient(uri);
    			DB db = mongo.getDB(uri.getDatabase());	
    			DBCollection polls = db.getCollection("polls");
    			
    			
    			// Find Code 
    			BasicDBObject findQuery = new BasicDBObject("id",id);	    	
    			DBCursor docs = polls.find(findQuery);
    			DBObject doc=docs.next();
    			pw.id=doc.get("id").toString();
    			pw.question=doc.get("question").toString();
    			pw.started_at=doc.get("started_at").toString();
    			pw.expired_at=doc.get("expired_at").toString();
    			pw.choice=(ArrayList<String>)doc.get("choice");
    			return pw;
	    	}
	    	catch(Exception e)
	    	{
	    		System.err.println( e.getClass().getName() + ": " + e.getMessage());
	    	}
	    	return null;
	    }	
	    
	   //6.View A Poll With Result
	    @RequestMapping(value="/api/v1/moderators/{moderator_id}/polls/{poll_id}" ,method=RequestMethod.GET)
	    @ResponseBody
	    @ResponseStatus(value=HttpStatus.OK)
	    public Poll controller6(@PathVariable(value="poll_id")String pid,@PathVariable(value="moderator_id") int mid)
	    {
	    	try
	    	{
	    		//Connectivity Code
	    		String textUri = "mongodb://viraj:12345@ds043981.mongolab.com:43981/cmpe273";
				MongoClientURI uri = new MongoClientURI(textUri);
				MongoClient mongo = new MongoClient(uri);
				DB db = mongo.getDB(uri.getDatabase());	
				DBCollection polls = db.getCollection("polls");
				
				
				// Find Code 
				BasicDBObject findQuery = new BasicDBObject("id",pid);	    	
				DBCursor docs = polls.find(findQuery);
				DBObject doc=docs.next();
				Poll p=new Poll();
				p.id=doc.get("id").toString();
				p.question=doc.get("question").toString();
				p.started_at=doc.get("started_at").toString();
		    	p.expired_at=doc.get("expired_at").toString();
		    	p.choice=(ArrayList<String>)doc.get("choice");
		    	p.results=(ArrayList<Integer>)doc.get("results");
		    	return p;
	    	}
	    	catch(Exception e)
	    	{
	    		System.err.println( e.getClass().getName() + ": " + e.getMessage());
	    	}
	    	return null;
	    }	
	    
	   
	    //7.List All Polls Generated By A Moderator
	    @RequestMapping(value="/api/v1/moderators/{moderator_id}/polls" ,method=RequestMethod.GET)
	    @ResponseBody
	    @ResponseStatus(value=HttpStatus.OK)
	    public ArrayList<Poll> controller7(@PathVariable(value="moderator_id") String moderator_id)
	    {
	    	ArrayList<Poll> ap=new ArrayList<Poll>();
	    	try
	    	{
	    		//Connectivity Code
	    		String textUri = "mongodb://viraj:12345@ds043981.mongolab.com:43981/cmpe273";
	    		MongoClientURI uri = new MongoClientURI(textUri);
	    		MongoClient mongo = new MongoClient(uri);
	    		DB db = mongo.getDB(uri.getDatabase());	
	    		DBCollection polls = db.getCollection("polls");
			
			
	    		// Find Code 
	    		Pattern pat=Pattern.compile("^"+moderator_id);
	    		BasicDBObject findQuery = new BasicDBObject("id",pat);	    	
	    		DBCursor docs = polls.find(findQuery);
	    		while(docs.hasNext())
	    		{
	    			DBObject a=docs.next();
	    			Poll p=new Poll();
	    			p.id=a.get("id").toString();
	    			p.question=a.get("question").toString();
	    			p.started_at=a.get("started_at").toString();
	    			p.expired_at=a.get("expired_at").toString();
	    			p.choice=(ArrayList<String>)a.get("choice");
	    			p.results=(ArrayList<Integer>)a.get("results");
	    			ap.add(p);
	    		}
	    	}
	    	catch(Exception e)
	    	{
	    		System.err.println( e.getClass().getName() + ": " + e.getMessage());
	    	}
	    	
	    	return ap;
	    }	
	   
	    //8.Delete A Poll
	    @RequestMapping(value="/api/v1/moderators/{moderator_id}/polls/{poll_id}" ,method=RequestMethod.DELETE)
	    @ResponseBody
	    @ResponseStatus(value=HttpStatus.NO_CONTENT)
	    public void controller8(@PathVariable(value="moderator_id") String moderator_id,@PathVariable(value="poll_id")String poll_id)
	    {
	    	try
	    	{
	    		//Connectivity Code
	    		String textUri = "mongodb://viraj:12345@ds043981.mongolab.com:43981/cmpe273";
	    		MongoClientURI uri = new MongoClientURI(textUri);
	    		MongoClient mongo = new MongoClient(uri);
	    		DB db = mongo.getDB(uri.getDatabase());	
	    		DBCollection polls = db.getCollection("polls");
	    		
	    		//Delete Poll
	    		BasicDBObject deleteQuery = new BasicDBObject("id",poll_id);	    	
	    		polls.remove(deleteQuery);
	    	}
	    	catch(Exception e)
	    	{
	    		System.err.println( e.getClass().getName() + ": " + e.getMessage());
	    	}
    	
	    	
	    }
	   
	    //9.Vote A Poll
	    @RequestMapping(value="/api/v1/polls/{poll_id}" ,method=RequestMethod.PUT)
	    @ResponseStatus(value=HttpStatus.NO_CONTENT)
	    @ResponseBody
	    public void controller9(@PathVariable(value="poll_id")String poll_id,@RequestParam(value="choice")Integer choice)
	    {
	    	try
	    	{
	    		//Connectivity Code
	    		String textUri = "mongodb://viraj:12345@ds043981.mongolab.com:43981/cmpe273";
	    		MongoClientURI uri = new MongoClientURI(textUri);
	    		MongoClient mongo = new MongoClient(uri);
	    		DB db = mongo.getDB(uri.getDatabase());	
	    		DBCollection polls = db.getCollection("polls");
	    		
	    		//Casting the vote 		
	    		BasicDBObject findQuery = new BasicDBObject("id",poll_id);
	    		DBCursor docs = polls.find(findQuery);
	    		DBObject doc=docs.next();
	    		ArrayList<Integer> al=new ArrayList<Integer>();
	    		al=(ArrayList<Integer>)doc.get("results");
	    		al.set(choice, al.get(choice)+1);
    			polls.update(findQuery, new BasicDBObject("$set", new BasicDBObject("results",al)));
	    
	    	}
	    	catch(Exception e)
	    	{
	    		System.err.println( e.getClass().getName() + ": " + e.getMessage());
	    	}
    				    	
	    }	    
	    

	    
	    
	 
	    
}
