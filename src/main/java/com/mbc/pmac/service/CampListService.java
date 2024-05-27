package com.mbc.pmac.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mbc.pmac.domain.CampFilteredVO;
import com.mbc.pmac.domain.CampImageVO;
import com.mbc.pmac.domain.CampReviewVO;
import com.mbc.pmac.domain.CampVO;
import com.mbc.pmac.repository.CampFilteredRepository;
import com.mbc.pmac.repository.CampImageRepository;
import com.mbc.pmac.repository.CampRepository;
import com.mbc.pmac.repository.CampReviewRepository;

@Service
public class CampListService {
	
	@Autowired
	CampFilteredRepository campFilteredRepository;

    @Autowired
    private CampImageRepository campImageRepository;
    
    @Autowired
    private CampReviewRepository campReviewRepository;
	
	@Transactional(readOnly = true)
	public int selectCampsCountBySearching(String themeSearch, String addressSearch, String searchWord) {

		if(addressSearch.equals("전체") && searchWord == null) {
			return campFilteredRepository.countByThemeContaining(themeSearch);
		}else if(addressSearch.equals("전체")) {
			return campFilteredRepository.countByThemeAndCampNameContaining(themeSearch, searchWord);
		}else if(searchWord == null) {
			return campFilteredRepository.countByThemeAndSidoNameContaining(themeSearch, addressSearch);
		}else {
			return campFilteredRepository.countByThemeAndSidoNameAndCampNameContaining(themeSearch, addressSearch, searchWord);
		}
		
	}
	
	@Transactional(readOnly = true)
	public List<CampFilteredVO> selectCampsBySearching(int currPage, int limit, String themeSearch, String addressSearch, String searchWord) {
		
		Pageable pageable = PageRequest.of(currPage-1, limit, Sort.by(Direction.DESC, "id"));
		
		if(addressSearch.equals("전체") && searchWord == null) {
			return campFilteredRepository.findByThemeContaining(themeSearch, pageable).getContent();
		}else if(addressSearch.equals("전체")) {
			return campFilteredRepository.findByThemeAndCampNameContaining(themeSearch, searchWord, pageable).getContent();
		}else if(searchWord == null) {
			return campFilteredRepository.findByThemeAndSidoNameContaining(themeSearch, addressSearch, pageable).getContent();
		}else {
			return campFilteredRepository.findByThemeAndSidoNameAndCampNameContaining(themeSearch, addressSearch, searchWord, pageable).getContent();
		}
	}
	
	@Transactional(readOnly = true)
    public List<CampImageVO> findCampImagesByCampName(String campName) {
        return campImageRepository.findByCampName(campName);
    }
	

}
