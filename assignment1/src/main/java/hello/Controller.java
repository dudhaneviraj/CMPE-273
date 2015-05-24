package main.java.hello;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

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






import java.util.*;

import javax.print.attribute.standard.DateTimeAtCreation;
import javax.validation.Valid;

@RestController
public class Controller {
	
		public static ArrayList<Moderator> moderator=new ArrayList<Moderator>();
		public static ArrayList<Poll> poll=new ArrayList<Poll>();
		static int m_id=123456;
		
	   //1.Create Moderator
	    
	    @RequestMapping(value="/api/v1/moderators" ,method=RequestMethod.POST)
	    @ResponseBody
	    @ResponseStatus(value=HttpStatus.CREATED)
	    public Moderator controller1(@Valid @RequestBody ModeratorRequest m)
	    {
	    	Moderator mod=new Moderator();
	    	mod.id=m_id;
	    	m_id++;
	    	mod.name=m.name;
	    	mod.email=m.email;
	    	mod.password=m.password;
	    	SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");	    	 
	    	Date curDate = new Date();
	    	String DateToStr = format.format(curDate);
	    	mod.created_at=DateToStr ;
	    	moderator.add(mod);
	    	return mod;
	    	
	    	
	    }
	    
	    //2.View Moderators
	    @RequestMapping(value="/api/v1/moderators/{moderator_id}" ,method=RequestMethod.GET)
	    @ResponseBody
	    @ResponseStatus(value=HttpStatus.OK)
	    public Moderator controller2(@PathVariable(value="moderator_id")int id)
	    {
	    		for(Moderator m:moderator)
	    		{
	    			if(m.id==id)
	    			{	
	    				return m;
	    			}
	    		}
	    		return null;
	    }
	    	
	    
	    
	    
	    
	    
	    //3. Update Moderator
	    @RequestMapping(value="/api/v1/moderators/{moderator_id}" ,method=RequestMethod.PUT)
	    @ResponseBody
	    @ResponseStatus(value=HttpStatus.OK)
	    public Moderator controller3(@PathVariable(value="moderator_id")int id,@Valid @RequestBody ModeratorRequest3 m)
	    {
	        for(Moderator mod:moderator)
	        {
	        	if(m.email.equals(mod.email))
	        	{
	        		mod.password=m.password;
	        		return mod;
	        	}	
	        }
	        	return null;
	     }
	    
	    
	    //4.Create a Poll
	    @RequestMapping(value="/api/v1/moderators/{moderator_id}/polls" ,method=RequestMethod.POST)
	    @ResponseBody
	    @ResponseStatus(value=HttpStatus.CREATED)
	    public PollWithoutResult controller4(@PathVariable(value="moderator_id")int id,@Valid @RequestBody PollRequest4 p)
	    {
	    	
	    	int temp=1;
	    	for(Poll po:poll)
	    	{
	    		if(po.id.startsWith(id+""))
	    		{
	    			temp++;
	    		}
	    	}
	    	
	    	String str=id+""+temp;
	    	Poll pollTemp=new Poll();
	    	PollWithoutResult pollWithoutResultTemp=new PollWithoutResult();
	    	
	    	pollTemp.id=str;
	    	pollTemp.question=p.question;
	    	pollTemp.started_at=p.started_at;
	    	pollTemp.expired_at=p.expired_at;
	        pollTemp.choice=p.choice;
	        for(int i=0;i<p.choice.size();i++)
	        {	
	        	pollTemp.results.add(0);
	        }
	    			
	    	pollWithoutResultTemp.id=str;
	    	pollWithoutResultTemp.question=p.question;
	    	pollWithoutResultTemp.started_at=p.started_at;
	    	pollWithoutResultTemp.choice=p.choice;
	    	pollWithoutResultTemp.expired_at=p.expired_at;
	    	poll.add(pollTemp);
	    	return pollWithoutResultTemp;
	    }	
	    
	   
	  //5.View A Poll Without Result
	    @RequestMapping(value="/api/v1/polls/{poll_id}" ,method=RequestMethod.GET)
	    @ResponseBody
	    @ResponseStatus(value=HttpStatus.OK)
	    public PollWithoutResult controller5(@PathVariable(value="poll_id")String id)
	    {
	    	for(Poll p:poll)
	    	{
	    		if(p.id.equals(id))
	    		{
	    			PollWithoutResult pw=new PollWithoutResult();
	    			pw.id=p.id;
	    			pw.question=p.question;
	    			pw.started_at=p.started_at;
	    			pw.expired_at=p.expired_at;
	    			pw.choice=p.choice;
	    			return pw;
	    		}
	    	}
	    	return null;
	    }	
	    
	   //6.View A Poll With Result
	    @RequestMapping(value="/api/v1/moderators/{moderator_id}/polls/{poll_id}" ,method=RequestMethod.GET)
	    @ResponseBody
	    @ResponseStatus(value=HttpStatus.OK)
	    public Poll controller6(@PathVariable(value="poll_id")String pid,@PathVariable(value="moderator_id") int mid)
	    {
	    	for(Poll p:poll)
	    	{
	    		if(p.id.equals(pid))
	    		{
	    			if(pid.startsWith(mid+""))
	    			{
	    				return p;
	    			}
	    		}
	    	}
	    	return null;
	    }	
	    
	    
	    //7.List All Polls Generated By A Moderator
	    @RequestMapping(value="/api/v1/moderators/{moderator_id}/polls" ,method=RequestMethod.GET)
	    @ResponseBody
	    @ResponseStatus(value=HttpStatus.OK)
	    public ArrayList<Poll> controller7(@PathVariable(value="moderator_id") String moderator_id)
	    {
	    		ArrayList<Poll> ap=new ArrayList<>();
	    		for(Poll p:poll)
	    		{
	    			if(p.id.startsWith(moderator_id))
	    			{
	    				ap.add(p);
	    			}
	    		}
	    		return ap;
	    }	
	    
	    //8.Delete A Poll
	    @RequestMapping(value="/api/v1/moderators/{moderator_id}/polls/{poll_id}" ,method=RequestMethod.DELETE)
	    @ResponseBody
	    @ResponseStatus(value=HttpStatus.NO_CONTENT)
	    public void controller8(@PathVariable(value="moderator_id") String moderator_id,@PathVariable(value="poll_id")String poll_id)
	    {
	    	if(poll_id.startsWith(moderator_id))
	    	{
	    		int i=1;
	    		Poll temp = null;
	    		for(Poll p:poll)
	    		{
	    			if(poll_id.equals(p.id))
	    			{
	    				temp = p;
	    			}
	    			
	    		}
	    		poll.remove(temp);
	    		
	    	}
	    }	
	    //9.Vote A Poll
	    @RequestMapping(value="/api/v1/polls/{poll_id}" ,method=RequestMethod.PUT)
	    @ResponseStatus(value=HttpStatus.NO_CONTENT)
	    @ResponseBody
	    public void controller9(@PathVariable(value="poll_id")String poll_id,@RequestParam(value="choice")Integer choice)
	    {
	    
	    		for(Poll p:poll)
	    		{
	    			if(p.id.equals(poll_id))
	    			{
	    				p.results.set(choice, p.results.get(choice)+1);
	    			}
	    		}
	    	
	    }	    
	    


	    
	    
	    
	    
}
