package tn.scout.vote.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tn.scout.vote.model.CONDIDAT;
@Repository
public interface CONDIDAT_DAO extends CrudRepository<CONDIDAT, Long>{

	@Query(value =" select * from condidat  where id= :id ", nativeQuery = true)
	public CONDIDAT getID( @Param("id") Long id) ;
	
	@Query(value =" select count(*) from condidat  where position= :pos ", nativeQuery = true)
	public Long CountbyType( @Param("pos") String pos) ;
	
	
	@Query(value =" select * from condidat  where position= :pos order by nom asc,date_nais asc", nativeQuery = true)
	public List<CONDIDAT> GetByPost( @Param("pos") String pos) ;
}
