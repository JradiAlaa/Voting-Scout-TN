package tn.scout.vote.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "global")
public class GLOBAL implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	public GLOBAL() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GLOBAL(long id, String date_vote, VOTERS voter, CONDIDAT condidat) {
		super();
		this.id = id;
		this.date_vote = date_vote;
		this.voter = voter;
		this.condidat = condidat;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDate_vote() {
		return date_vote;
	}

	public void setDate_vote(String date_vote) {
		this.date_vote = date_vote;
	}

	public VOTERS getVoter() {
		return voter;
	}

	public void setVoter(VOTERS voter) {
		this.voter = voter;
	}

	public CONDIDAT getCondidat() {
		return condidat;
	}

	public void setCondidat(CONDIDAT condidat) {
		this.condidat = condidat;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Column(name = "date_vote")
	private String date_vote;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE }, fetch = FetchType.EAGER)
	@JoinColumn(name = "id_voter", referencedColumnName = "login")
	private VOTERS voter;
	
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE }, fetch = FetchType.EAGER)
	@JoinColumn(name = "id_cond", referencedColumnName = "id")
	private CONDIDAT condidat;

}
