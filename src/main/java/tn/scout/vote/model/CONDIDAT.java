package tn.scout.vote.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;


@Entity
@Table(name = "condidat")
public class CONDIDAT  implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id; 
	
	
	@Column(name = "nom")
	private String nom ; 
	

	@Lob
    @Column(name = "image", length = Integer.MAX_VALUE, nullable = true)
    private byte[] image;
	
	@Column(name = "position")
	private String position;

	@Column(name = "date_nais")
	private Date date_nais;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}


	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Date getDate_nais() {
		return date_nais;
	}

	public void setDate_nais(Date date_nais) {
		this.date_nais = date_nais;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public CONDIDAT(long id, String nom, byte[] image, String position, Date date_nais) {
		super();
		this.id = id;
		this.nom = nom;
		this.image = image;
		this.position = position;
		this.date_nais = date_nais;
	}

	public CONDIDAT() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CONDIDAT(Long id2, String nom2, String prenom2, String position2, Date date_nais2) {
		
		
		// TODO Auto-generated constructor stub
	}
	
	

	
}
