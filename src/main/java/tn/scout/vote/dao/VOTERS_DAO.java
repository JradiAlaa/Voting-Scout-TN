package tn.scout.vote.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tn.scout.vote.model.VOTERS;
@Repository
@Transactional
public interface VOTERS_DAO extends CrudRepository<VOTERS, Long>{

	@Query(value =" select * from voters  where login= :login and password= :password", nativeQuery = true)
	public VOTERS getLogin( @Param("login") String login , @Param("password") String password) ;
	
	@Query(value =" select * from voters  where login= :login ", nativeQuery = true)
	public VOTERS getLogin1( @Param("login") String login) ;
	
	@Query(value =" select count(*) from voters  where login= :login ", nativeQuery = true)
	public int getLoginEX( @Param("login") String login) ;
	
	@Query(value =" select count(*) from voters  where etat='1' or etat='5' ", nativeQuery = true)
	public Long getVoteAll() ; 
	@Query(value =" select count(*) from voters  where etat='1'  ", nativeQuery = true)
	public Long getVoteOK() ; 
	@Query(value =" select count(*) from voters  where  etat='5' ", nativeQuery = true)
	public Long getVoteKO() ; 
	@Query(value =" select etat from voters where login= :login ", nativeQuery = true)
	public String getEt(@Param("login") String login) ; 
	@Modifying
	@Query(value =" update voters set etat='1' where   login= :login ", nativeQuery = true)
	public void votEtat( @Param("login") String login) ;
	
	@Modifying
	@Query(value =" update voters set etat= :et where   login= :login ", nativeQuery = true)
	public void voteEncours(@Param("et") String et, @Param("login") String login);
	
}
