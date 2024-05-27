package com.mbc.pmac.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mbc.pmac.domain.CampFilteredVO;
import com.mbc.pmac.domain.CampImageVO;
import com.mbc.pmac.domain.CampReviewVO;
import com.mbc.pmac.domain.PageVO;
import com.mbc.pmac.repository.CampImageRepository;
import com.mbc.pmac.repository.CampReviewRepository;
import com.mbc.pmac.service.CampListService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/camp")
@Slf4j
public class CampSearchListController {
	
	@Autowired
	CampListService campListService;
	
	@Autowired
	CampImageRepository campImageRepo;
	
	@Autowired
	CampReviewRepository campReviewRepo;
	
	@GetMapping("searchList.do")
	public String list(@RequestParam(value="currPage", defaultValue="1") int currPage,
					   @RequestParam(value="limit", defaultValue="10") int limit,
					   @RequestParam(value="themeSearch") String themeSearch,
					   @RequestParam(value="searchWord") String searchWord,
					   @RequestParam(value="addressSearch") String addressSearch,
					   Model model) {
		
		log.info("캠핑장 검색 목록");
		log.info("검색 구분 : {}", themeSearch);
		log.info("검색어 : {}", addressSearch);
		log.info("검색어 : {}", searchWord);
		
		List<CampFilteredVO> campList = new ArrayList<>();
		
		// 검색시는 "댓글"도 검색에 반영 (기존 대비 변경 없음)
		// 총 "검색" 게시글 수
		int listCount = campListService.selectCampsCountBySearching(themeSearch, addressSearch, searchWord.trim());
		
		campList = campListService.selectCampsBySearching(currPage, limit, themeSearch, addressSearch, searchWord.trim());	
		

	    List<CampImageVO> campImageList = new ArrayList<>();
	    for (CampFilteredVO camp : campList) {
	        List<CampImageVO> images = campListService.findCampImagesByCampName(camp.getCampName());
	        if (!images.isEmpty()) {
	            campImageList.addAll(images);
	            log.info("왜냐 {}",campImageList.size());
	        }
	    }
	    
	    Map<String, Integer[]> campReviewStats = new HashMap<>();
        for (CampFilteredVO camp : campList) {
            List<CampReviewVO> reviewList = campReviewRepo.findAllByCampName(camp.getCampName());
            log.info("reviewList 크기 for camp {}: {}", camp.getCampName(), reviewList.size());

            int positiveDegree = 0;
            int negativeDegree = 0;

            if (!reviewList.isEmpty()) {
                positiveDegree = (int) reviewList.stream().filter(x -> x.getPredict() == 1).count();
                negativeDegree = (int) reviewList.stream().filter(x -> x.getPredict() == 0).count();
            }

            campReviewStats.put(camp.getCampName(), new Integer[]{positiveDegree, negativeDegree});
        }
	    
		// 총 페이지 수
		int maxPage = PageVO.getMaxPage(listCount, limit);
		// 현재 페이지에 보여줄 시작 페이지 수 (1, 11, 21,...)
		int startPage = PageVO.getStartPage(currPage);
   	    int endPage = PageVO.getEndPage(currPage);
   	    
   	    if (endPage> maxPage) endPage = maxPage;
   	    if (currPage > maxPage) currPage = maxPage;
   	    
   	    PageVO pageVO = new PageVO();
		pageVO.setEndPage(endPage);
		pageVO.setListCount(listCount);
		pageVO.setMaxPage(maxPage);
		pageVO.setCurrPage(currPage);
		pageVO.setStartPage(startPage);
		
		pageVO.setPrePage(pageVO.getCurrPage()-1 < 1 ? 1 : pageVO.getCurrPage()-1);
		// pageVO.setNextPage(pageVO.getCurrPage()+1 > pageVO.getEndPage() ? pageVO.getEndPage() : pageVO.getCurrPage()+1);
		pageVO.setNextPage(pageVO.getCurrPage()+1);
	
		model.addAttribute("pageVO", pageVO);
		model.addAttribute("campList", campList);
		model.addAttribute("listCount", listCount);
		model.addAttribute("campImageList", campImageList);
		
		model.addAttribute("themeSearch", themeSearch);
		model.addAttribute("addressSearch", addressSearch);
		model.addAttribute("searchWord", searchWord);
		
		model.addAttribute("campReviewStats", campReviewStats);
		
		return "campList/searchCampList";
	}
}
