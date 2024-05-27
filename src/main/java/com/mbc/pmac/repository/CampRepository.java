package com.mbc.pmac.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mbc.pmac.domain.CampVO;

public interface CampRepository extends JpaRepository<CampVO, Integer> {
	
	int countByThemeContaining(String theme);
	int countByThemeAndSidoNameContaining(String theme, String sidoName);
	int countByThemeAndCampNameContaining(String theme, String campName);
	int countByThemeAndSidoNameAndCampNameContaining(String theme, String sidoName, String campName);
	
	Page<CampVO> findByThemeContaining(String theme, Pageable pageable);
	Page<CampVO> findByThemeAndSidoNameContaining(String theme, String sidoName, Pageable pageable);
	Page<CampVO> findByThemeAndCampNameContaining(String theme, String campName, Pageable pageable);
	Page<CampVO> findByThemeAndSidoNameAndCampNameContaining(String theme, String sidoName, String campName, Pageable pageable);
}
