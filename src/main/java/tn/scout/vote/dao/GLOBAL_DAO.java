package tn.scout.vote.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tn.scout.vote.model.GLOBAL;

@Repository
@Transactional
public interface GLOBAL_DAO  extends CrudRepository<GLOBAL,Long>{
	@Query(value =" select * from global  where id_voter= :id ", nativeQuery = true)
	public List<GLOBAL> getALLID( @Param("id") String id) ; 
	
	
	@Query(value ="select nom,count(*) from global g , condidat c where g.id_cond=c.id and id_cond in (select id from `condidat` where position=:pos) group by  nom ORDER by count(*) desc ,date_nais asc ", nativeQuery = true)
	public List<List<String>> getResultVote(@Param("pos") String pos) ; 
	
	@Modifying
	@Query(value =" delete  from global  where id_voter= :v", nativeQuery = true)
public void reset( @Param("v") String v) ;

	
}
