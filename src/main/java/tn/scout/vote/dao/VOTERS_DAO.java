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
	
	@Query(value =" select count(*) from voters  where etat='1' ", nativeQuery = true)
	public Long getVoteOK() ; 

	@Modifying
	@Query(value =" update voters set etat='1' where   login= :login ", nativeQuery = true)
	public void votEtat( @Param("login") String login) ;
}
