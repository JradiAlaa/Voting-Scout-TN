package tn.scout.vote.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "voters")
public class VOTERS  {


	@Id 
	@Column(name = "login")
	private String login ; 
	
	@Column(name = "password")
	private String password; 
	
	
	@Column(name = "etat")
	private String etat ;



	public String getLogin() {
		return login;
	}


	public void setLogin(String login) {
		this.login = login;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getEtat() {
		return etat;
	}


	public void setEtat(String etat) {
		this.etat = etat;
	}



	public VOTERS(String login, String password, String etat) {
		super();

		this.login = login;
		this.password = password;
		this.etat = etat;
	}


	public VOTERS() {
		super();
		// TODO Auto-generated constructor stub
	} 
	
	
}
