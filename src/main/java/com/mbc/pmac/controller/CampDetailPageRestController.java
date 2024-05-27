package com.mbc.pmac.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mbc.pmac.domain.MemberReviewVO;
import com.mbc.pmac.service.MemberReviewService;

import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/Camp")

@Slf4j
public class CampDetailPageRestController {
	
	@Autowired
	MemberReviewService memberReviewService;
	
	
	// 댓글을 작성하면서 즉시 현재까지의 댓글들 현황을 집계하여 리턴
	@PostMapping("/Review.do")
	public ResponseEntity<List<MemberReviewVO>> CampDetailPageReview(@RequestBody Map<String, Object> map) {
		
		log.info("잘왔니?");
		
		log.info("Review.do : memberReviewContent={}, userNicknameKey={}, campId={} ", map.get("memberReviewContent"), map.get("userNicknameKey"), map.get("campIdKey"));
		
		List<MemberReviewVO> memberReviewList = new ArrayList<>();
		
		ResponseEntity<List<MemberReviewVO>> responseEntity = null;
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
		
		try {
			MemberReviewVO memberReviewVO = new MemberReviewVO();
		
			// 주의사항)
			// 여기서 댓글의 고유 아이디는 DB를 통해서 생성되므로 원글의 아이디(boardNum)는 다른 필드에 입력됩니다.
			memberReviewVO.setUserNickname(map.get("userNicknameKey").toString());
			memberReviewVO.setMemberReviewContent(map.get("memberReviewContent").toString());
			memberReviewVO.setCampId(Integer.parseInt(map.get("campIdKey").toString()));
			memberReviewVO.setMemberReviewImage((String) map.get("memberReviewImage"));
			memberReviewVO.setMemberReviewOgImage((String) map.get("memberReviewOgImage"));
			
			log.info("Review.do : ", memberReviewVO.toString());
			
			// 댓글의 현황을 보면서 댓글 시퀀스 결정
			memberReviewVO = memberReviewService.insertReview(memberReviewVO);
			
			log.info("--- result : {}", memberReviewVO);
			
			if (memberReviewVO != null) {
				
				// 원글에 따른 전체 댓글 현황 목록(리스트) 가져오기 => 리턴 => Client(웹 브라우저)
				memberReviewList = memberReviewService.selectReplyByCampId(memberReviewVO.getCampId());
				
				// 댓글 등록 성공 : 성공 코드(200)
				// responseEntity = new ResponseEntity<>(true, HttpStatus.OK);
				
				// 원글에 따른 전체 댓글 현황 목록(리스트) 리턴(클라이언트에 전송)
				responseEntity = new ResponseEntity<>(memberReviewList, HttpStatus.OK);
				
			} else {
				// 댓글 등록 실패 : 실패 코드(204)
				// responseEntity = new ResponseEntity<>(true, HttpStatus.NO_CONTENT);
				responseEntity = new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
			}
			
		} catch (Exception e) {
			log.error("Review.do error : {}", e);
			e.printStackTrace();
			
			// 실패 코드(417) : 내부 서버 에러
			responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return responseEntity;
	} // "/detailPage/Review.do"
	

	@GetMapping("/getMemberReview.do")
	public ResponseEntity<MemberReviewVO> getMemberReview(@RequestParam("campId") int campId) {
		
		log.info("getMemberReview.do : campId={}", campId);
		
		MemberReviewVO memberReviewVO = null;
		
		ResponseEntity<MemberReviewVO> responseEntity =null;
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
		
		try {
			// 댓글 현황 목록(리스트) 가져오기 => 리턴 => Client(웹 브라우저)
			// 댓글 현황 목록(리스트) 리턴(클라이언트에 전송)
			memberReviewVO = memberReviewService.selectBymemberReviewNum(campId);
			
			if (memberReviewVO != null) {
				// 댓글 등록 성공 : 성공 코드(200)
				responseEntity = new ResponseEntity<>(memberReviewVO, HttpStatus.OK);
			} else {
				// 댓글 등록 실패 : 실패 코드(204)
				responseEntity = new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
			}
			
		} catch (Exception e) {
			log.error("getMemberReview error : {}", e);
			e.printStackTrace();
			
			// 실패 코드(417) : 내부 서버 에러
			responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return responseEntity;
	}
	
	// 리뷰들을 가져오는 친구
	@GetMapping("/MemberReviewsAll.do")
	public ResponseEntity<List<MemberReviewVO>> getRepliesAll(@RequestParam("campId") int campId) {
		
		log.info("MemberReviewsAll.do : campId={}", campId);
		
		List<MemberReviewVO> memberReviewList = new ArrayList<>();
		ResponseEntity<List<MemberReviewVO>> responseEntity = null;
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
		
		try {
			// 원글에 따른 전체 댓글 현황 목록(리스트) 가져오기 => 리턴 => Client(웹 브라우저)
			// 원글에 따른 전체 댓글 현황 목록(리스트) 리턴(클라이언트에 전송)
			memberReviewList = memberReviewService.selectReplyByCampId(campId);
			
			// memberReviewList.forEach(x-> {log.info("날짜 : {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(x.getBoardDate())); });
			
			// 댓글들이 있다면..
			if (memberReviewList.size() > 0) {
				
				// 댓글 등록 성공 : 성공 코드(200)
				// 원글에 따른 전체 댓글 현황 목록(리스트) 리턴(클라이언트에 전송)
				responseEntity = new ResponseEntity<>(memberReviewList, HttpStatus.OK);
			} else {
				// 댓글 등록 실패 : 실패 코드(204)
				responseEntity = new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
			}
			
		} catch (Exception e) {
			log.error("getMemberReviewsAll error : {}", e);
			e.printStackTrace();
			
			//실패 코드(417) : 내부 서버 에러
			responseEntity = new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
		
		return responseEntity;
	}
	
	@PostMapping("/memberReviewUpdate.do")
	public ResponseEntity<List<MemberReviewVO>> memberReviewUpdate(@RequestBody Map<String, Object> map) {
		
		log.info("$$$잘왔어???");
		
		log.info("memberReviewUpdate.do : memberReviewNum={}, campId={}, userNickname={}, memberReviewContent={}",
					map.get("memberReviewNum"), map.get("campId"), map.get("userNickname"), map.get("memberReviewContent"));
		
		List<MemberReviewVO> memberReviewList = new ArrayList<>();
		
		ResponseEntity<List<MemberReviewVO>> responseEntity = null;
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
		
		try {
			MemberReviewVO memberReviewVO = new MemberReviewVO();
			
			int campId = Integer.parseInt(map.get("campId").toString());
			
			// 주의) 댓글 수정에서는 댓글의 아이디가 이미 등록시 발행이 되어 있으므로 댓글의 실제 아이디 !
			memberReviewVO.setMemberReviewNum(Integer.parseInt(map.get("memberReviewNum").toString()));
			memberReviewVO.setCampId(campId);
			memberReviewVO.setMemberReviewContent(map.get("memberReviewContent").toString());
			memberReviewVO.setUserNickname(map.get("userNickname").toString());
			
			log.info("MemberReviewVO : {}", memberReviewVO);
			
			memberReviewVO = memberReviewService.updateReview(memberReviewVO);
			
			if (memberReviewVO != null) {
				
				// 원글에 따른 전체 댓글 현황 목록(리스트) 가져오기 => 리턴 => Client(웹 브라우저)
				memberReviewList = memberReviewService.selectReplyByCampId(memberReviewVO.getCampId());
				
				// 댓글 등록 성공 : 성공 코드(200)
				//responseEntity = new ResponseEntity<>(true, HttpStatus.OK);
				
				// 원글에 따른 전체 댓글 현황 목록(리스트) 리턴(클라이언트에 전송)
				responseEntity = new ResponseEntity<>(memberReviewList, HttpStatus.OK);
				
			} else {
				// 댓글 등록 실패 : 실패 코드(204)
				// responseEntity = new ResponseEntity<>(true, HttpStatus.OK);
				responseEntity = new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
				
			}
			
		} catch (Exception e) {
			log.error("memberReviewUpdate error : {}", e);
			e.printStackTrace();
			
			// 실패 코드(417) : 내부 서버 에러
			responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return responseEntity;
	}
	
	@PostMapping("/memberReviewDelete.do")
	public ResponseEntity<List<MemberReviewVO>> memberReviewDelete(@RequestBody Map<String, Object> map) {

		log.info("memberReviewDelete.do : memberReviewNum={}, campId={}, userNickname={}",
					map.get("memberReviewNum"), map.get("campId"), map.get("userNickname"));
		
		List<MemberReviewVO> memberReviewList = new ArrayList<>();
		
		ResponseEntity<List<MemberReviewVO>> responseEntity = null;
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
		
		try {
			
			int memberReviewNum = Integer.parseInt(map.get("memberReviewNum").toString());
			int originalCampId = Integer.parseInt(map.get("originalCampId").toString());
			
			boolean result = false; // 삭제 결과
			
			result = memberReviewService.deleteByMemberReviewNum(memberReviewNum);
			
			if (result == true) {
				
				// 원글에 따른 전체 댓글 현황 목록(리스트) 가져오기 => 리턴 => Client(웹 브라우저)
				memberReviewList = memberReviewService.selectReplyByCampId(originalCampId);
				
				// 댓글 등록 성공 : 성공 코드(200)
				//responseEntity = new ResponseEntity<>(true, HttpStatus.OK);
				
				// 원글에 따른 전체 댓글 현황 목록(리스트) 리턴(클라이언트에 전송)
				responseEntity = new ResponseEntity<>(memberReviewList, HttpStatus.OK);
				
			} else {
				// 댓글 등록 실패 : 실패 코드(204)
				// responseEntity = new ResponseEntity<>(true, HttpStatus.NO_CONTENT);
				responseEntity = new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
			}
			
		} catch (Exception e) {
			log.error("memberReviewDelete error : {}", e);
			e.printStackTrace();
		
			// 실패 코드(417) : 내부 서버 에러
			responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
		
		return responseEntity;
	}
	
	
}
