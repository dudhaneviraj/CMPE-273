package main.java.hello;
import java.util.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
public class PollRequest4 {
	@NotEmpty(message="Question Cannot Be Empty")
	public String question;
	@NotEmpty(message="Started_At Cannot Be Empty")
	public String started_at;
	@NotEmpty(message="Expired_At Cannot Be Empty")
	public String expired_at;
	@NotEmpty(message="Choice Cannot Be Empty")
	public ArrayList<String> choice=new ArrayList<String>();
	public PollRequest4(){}
	
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getStarted_at() {
		return started_at;
	}
	public void setStarted_at(String started_at) {
		this.started_at = started_at;
	}
	public String getExpired_at() {
		return expired_at;
	}
	public void setExpired_at(String expired_at) {
		this.expired_at = expired_at;
	}

	public ArrayList<String> getChoice() {
		return choice;
	}

	public void setChoice(ArrayList<String> choice) {
		this.choice = choice;
	}
	
}