package main.java.hello;


import javax.validation.Valid;
import javax.validation.*;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;



class ModeratorRequest 
{
	@NotEmpty(message="Name Field Cannot Be Left Empty")
	public String name;
	@Email(message="Enter Valid Email ID")
	@NotEmpty(message="Email Field Cannot Be Empty")
	public String email;
	@NotNull(message="Password Cannot Be Empty")
	public String password;
	public ModeratorRequest(){}
		public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
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
