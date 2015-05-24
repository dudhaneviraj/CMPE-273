package main.java.hello;

import org.springframework.context.annotation.Bean;
import javax.validation.Valid;
import javax.validation.*;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;


public class ModeratorRequest3 {
	
	@Email(message="Enter A Valid Email")
	@NotEmpty(message="Email Field Cannot Be Empty")
	public String email;
	@NotNull(message="Password Field Cannot be Null")
	@NotEmpty(message="Password Field Cannot Be Empty")
	public String password;
	public ModeratorRequest3(){}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
