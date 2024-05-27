package com.mbc.pmac.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.mbc.pmac.domain.CampReviewVO;

// 캠핑장추천
public interface CampReviewRepository extends CrudRepository<CampReviewVO, String> {

	List<CampReviewVO> findAllByCampName(String campName);
	
	@Query(value="SELECT DISTINCT camp_name FROM camp_review_tbl WHERE avg_rating=5 ORDER BY avg_rating DESC", nativeQuery=true)
	List<String> sortCampReviewByAvgRating();
	
   @Query(value = "SELECT camp_name, count(*) FROM camp_review_tbl GROUP BY camp_name, avg_rating HAVING avg_rating=5 ORDER BY count(*) DESC",  nativeQuery=true)
   List<Map<String, Integer>> findByTopRanking();
	

}