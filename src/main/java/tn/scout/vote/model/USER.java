package tn.scout.vote.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class USER {
	
	@Id
	@Column(name = "login")
	private String login ;
	
	@Column(name = "password")
	private String password; 
	
	
	@Column(name = "role")
	private String role ;
	
	@Column(name = "nom")
	private String nom ;
	
	@Column(name = "prenom")
	private String prenom ;

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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public USER(String login, String password, String role, String nom, String prenom) {
		super();
		this.login = login;
		this.password = password;
		this.role = role;
		this.nom = nom;
		this.prenom = prenom;
	}

	public USER() {
		super();
		// TODO Auto-generated constructor stub
	} 
	
	
	

}
