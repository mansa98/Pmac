package com.mbc.pmac.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mbc.pmac.domain.CampFilteredVO;
import com.mbc.pmac.domain.CampImageVO;
import com.mbc.pmac.repository.CampFilteredRepository;
import com.mbc.pmac.repository.CampImageRepository;
import com.mbc.pmac.repository.CampRepository;
import com.mbc.pmac.service.CampService;

import lombok.extern.slf4j.Slf4j;
@Controller
@Slf4j
@RequestMapping("/Camp")
public class CampController {

	@Autowired
	CampService CampService;
	
	@Autowired
	CampImageRepository campImageRepo;
	
	@Autowired
    CampFilteredRepository campRepo;
	
	@GetMapping("/gallery")
	public String goGallery(Model model) {
		
		List<CampFilteredVO> campList = new ArrayList<>();
		
		campList = CampService.getDatasFromDB();
		
		model.addAttribute("campList", campList);
		
		return "/Camp/Campgallery";
	}// goGallery()
	
	
	@GetMapping("/detailpage")
	public String goDetailPage(@RequestParam("Id") int Id, 
//							   @RequestParam("JibunAddress") String JibunAddress,
							   Model model) {
		
		CampFilteredVO campFilteredVO = new CampFilteredVO();
		
		campFilteredVO = CampService.selectCamp(Id);
		
		
		
		// 0527 재욱 - 캠핑장 이미지가 없는 캠핑장의 상세 페이지 접근시 null 오류 발생했던 문제 해결
		CampImageVO campImageVO = new CampImageVO();
		
		// 해당 캠핑장의 캠프 이미지가 존재하는 경우에만!
//		if (campImageRepo.findByCampName(campFilteredVO.getCampName()) != null) {
//		
//			campImageVO = campImageRepo.findByCampName(campFilteredVO.getCampName()).get(0);
//		
//		} else if (campImageRepo.findByCampName(campFilteredVO.getCampName()) == null){
//			
//			log.info("해당 캠핑장의 이미지가 존재하지 않습니다.");
//		}
		
		List<CampImageVO> campImageList = campImageRepo.findByCampName(campFilteredVO.getCampName());
		
		campImageVO = campImageList.size() >= 1
				? campImageRepo.findByCampName(campFilteredVO.getCampName()).get(0) : null; 
		

//		campFilteredVO = CampService.selectJibunAddress(JibunAddress);
		
		log.info("CampFilteredVO : {}", campFilteredVO.toString());
		log.info("JibunAddress : {}", campFilteredVO.toString());
		
		model.addAttribute("CampFilteredVO", campFilteredVO);
		model.addAttribute("CampImageVO", campImageVO);
//		model.addAttribute("jibunAddress", JibunAddress);
		model.addAttribute("Id", Id);

		
		/////////////////////////////////////////////////////////////////////////
		
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		
//		String memberId = auth.getName();
//		
//		model.addAttribute("memberId", memberId);
		
//		FavVO favVO = new FavVO();
//		
//		List<FavVO> favList = new ArrayList<>();
//		
//		// 리퀘파람의 Id으로 select해올 데이터가 존재한다면
//		if (favService.getFavById(Id) != null) {
//			
//			favList = favService.getFavById(Id);
//			
//			// 그 데이터 내 요소의 memberId가 현재 로그인한 유저의 id와 일치한다면
//			for (int i = 0; i < favList.size(); i++) {
//				
//				if (favList.get(i).getMemberId().equals(memberId)) {
//					
//					// favVO에 할당해줌!
//					favVO = favList.get(i);
//				}//
//			}//
//			
//		}//
		
//		log.info("★ 모델에 추가 직전의 memberId {}", memberId);
//		log.info("★ 모델에 추가 직전의 favVO {}", favVO.toString());
		
//		model.addAttribute("favVO", favVO);
		
		// 임시 상세페이지를 접속할때마다 해당 레시피의 CampCount를 +1씩 업데이트하는 쿼리 적용
		
//		boolean updateCountResult = false;
//		
//		// 여기서 Camp_count 를 +1
//		updateCountResult = CampService.updateCount(CampFilteredVO);
//		// update 결과를 전송
//		model.addAttribute("updateCountResult", updateCountResult);
//		// update 적용된 데이터를 가져옴
//		CampFilteredVO updatedCampFilteredVO = CampService.selectCamp(CampFilteredVO.getId());
//		// update 적용된 데이터를 전송
//		model.addAttribute("updatedCampFilteredVO", updatedCampFilteredVO);
//		
//		log.info("▼ 업데이트된 상황은: {}", updatedCampFilteredVO.toString());
		
		
		return "Camp/Campdetailpage";
	}
	
	
}
