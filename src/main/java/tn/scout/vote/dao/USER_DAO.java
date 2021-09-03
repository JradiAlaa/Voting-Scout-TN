package tn.scout.vote.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tn.scout.vote.model.USER;


@Repository
@Transactional
public interface USER_DAO extends CrudRepository<USER, String> {

		@Query(value =" select * from user  where login= :login and password= :password", nativeQuery = true)
	public USER getLogin( @Param("login") String login , @Param("password") String password) ;
		@Query(value =" select * from user  where login= :login", nativeQuery = true)
	public USER getUser( @Param("login") String login) ;
		@Modifying
		@Query(value =" update user set nom=:nom , prenom=:prenom  where login= :login", nativeQuery = true)
	public void upUser( @Param("login") String login, @Param("nom") String nom, @Param("prenom") String prenom) ;
	
		@Modifying
		@Query(value =" update user set password=:password  where login= :login", nativeQuery = true)
	public void upPassword( @Param("login") String login, @Param("password") String password) ;
	
	

}
