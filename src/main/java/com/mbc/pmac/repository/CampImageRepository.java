package com.mbc.pmac.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mbc.pmac.domain.CampImageVO;

@Repository
public interface CampImageRepository extends JpaRepository<CampImageVO, Integer> {

	List<CampImageVO> findByCampName(String campName);
	
	List<CampImageVO> findAllByCampName(String campName); // 0525 재욱
	
	
	@Query(value="SELECT * FROM camp_image_tbl WHERE camp_name=:campName", nativeQuery=true)
	CampImageVO findByCampNameCustom(@Param("campName") String campName);
}
