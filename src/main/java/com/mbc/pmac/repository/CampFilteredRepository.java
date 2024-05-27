package com.mbc.pmac.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.mbc.pmac.domain.CampFilteredVO;
import com.mbc.pmac.domain.CampVO;

public interface CampFilteredRepository extends JpaRepository<CampFilteredVO, Integer>{
	
	int countByThemeContaining(String theme);
	int countByThemeAndSidoNameContaining(String theme, String sidoName);
	int countByThemeAndCampNameContaining(String theme, String campName);
	int countByThemeAndSidoNameAndCampNameContaining(String theme, String sidoName, String campName);
	
	Page<CampFilteredVO> findByThemeContaining(String theme, Pageable pageable);
	Page<CampFilteredVO> findByThemeAndSidoNameContaining(String theme, String sidoName, Pageable pageable);
	Page<CampFilteredVO> findByThemeAndCampNameContaining(String theme, String campName, Pageable pageable);
	Page<CampFilteredVO> findByThemeAndSidoNameAndCampNameContaining(String theme, String sidoName, String campName, Pageable pageable);
	
	List<CampFilteredVO> findByCampNameContaining(String campName);
	
	CampFilteredVO findById(int id);
	
	CampFilteredVO findByJibunAddress(String JibunAddress);
	
	List<CampFilteredVO> findAllByTheme(String theme); // 0525 재욱
}


