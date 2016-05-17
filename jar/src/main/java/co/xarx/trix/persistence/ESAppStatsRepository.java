package co.xarx.trix.persistence;

import co.xarx.trix.domain.ESAppStats;

import java.util.List;

public interface ESAppStatsRepository extends ESRepository<ESAppStats> {
//	@Query("{\"bool\" : {\"must\" : {\"field\" : {\"packageName\" : \"?\"}}}}")
	List<ESAppStats> findByPackageName(String packageName);

//	@Query("{\"bool\" : {\"must\" : {\"field\" : {\"sku\" : \"?\"}}}}")
	List<ESAppStats> findBySku(String sku);
}
