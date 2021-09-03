package tn.scout.vote.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Ref implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id; 
	
	
	@Column(name = "dec")
	private String dec ; 
	@Column(name = "valeur")
	private String valeur ; 
}
