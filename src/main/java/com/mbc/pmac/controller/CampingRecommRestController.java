package com.mbc.pmac.controller;

// 0525 재욱
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mbc.pmac.domain.CampFilteredVO;
import com.mbc.pmac.domain.SessionUser;
import com.mbc.pmac.domain.User;
import com.mbc.pmac.domain.UserRequestVO;
import com.mbc.pmac.domain.UserResultVO;
import com.mbc.pmac.service.CampRecommService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/campingRecomm")
@Slf4j
public class CampingRecommRestController {
	
	@Autowired
	CampRecommService campRecommService;
	
	
	
	@PostMapping("/getRecomm")
	public ResponseEntity<List<UserResultVO>> getRecomm(@RequestParam("location") String location, // 셀렉트박스
											@RequestParam("theme") String theme, // 체크박스 => 라디오버튼으로 변경
											@RequestParam("facil") List<String> facils, // 체크박스
											@RequestParam("dateString") String dateString, // 텍스트input(JQuery datePicker)
											HttpSession session) {
		
		// 유저가 원하는 모든 정보를 담을 곳
		UserRequestVO userRequest = new UserRequestVO();
		
		// 세션에서 nickname 추출해오기
		SessionUser user = new SessionUser();
		
		if (session.getAttribute("user") != null) {
			user = (SessionUser)session.getAttribute("user");
		}
		
		String nickname = user.getNickname();
		
		// 현재 로그인 유저의 nickname
		userRequest.setNickname(nickname);
		
		// 최종적으로 index로 리턴할 값
		ResponseEntity<List<UserResultVO>> responseEntity = null;
		
		List<UserResultVO> recommResultList = new ArrayList<>();
		
		log.info("인자 확인 들어가자!!!!!");
		
		log.info("＃＃＃ 닉네임: {}", nickname);

		log.info("＃＃＃ 날짜: {}", dateString);
		
		// String => LocalDate 로 치환
		LocalDate userDate = LocalDate.parse(dateString);
		
		log.info("●●● LocalDate로 변환한 날짜 값: {}", userDate);
		
		// 유저가 원하는 날짜
		userRequest.setTime(userDate);
		
		log.info("＃＃＃ 지역: {}", location);
		
		// 유저가 원하는 지역
		userRequest.setDestination(location);
		
		log.info("### 테마: {}", theme);
		
		// 유저가 원하는 테마
		userRequest.setTheme(theme);
		
		// 유저가 원하는 시설들
		// index에서 넘어오는 value를 추천시스템 로직에 맞도록 변형
		for (String facil : facils) {
			log.info("### 시설: {}", facil);
			
			if (facil.equals("전기")) {
				userRequest.setIsElectricity(1);
			}
			
//			int electricity = facil.equals("전기") ? 1 : 0;
//			userRequest.setIsElectricity(electricity);
			
			if (facil.equals("와이파이")) {
				userRequest.setIsWifi(1);
			}
			
			if (facil.equals("캠프파이어")) {
				userRequest.setIsCampfire(1);
			}
			
			if (facil.equals("히터")) {
				userRequest.setIsHeater(1);
			}
		
			if (facil.equals("풀장")) {
				userRequest.setIsPool(1);
			}
			
			if (facil.equals("운동장")) {
				userRequest.setIsPlayground(1);
			}
			
			if (facil.equals("싱크대")) {
				userRequest.setIsSink(1);
			}
			
			if (facil.equals("산책로")) {
				userRequest.setIsTrail(1);
			}
			
			if (facil.equals("수상레저")) {
				userRequest.setIsMaritimeLeisure(1);
			}
			
			if (facil.equals("낚시터")) {
				userRequest.setIsFishing(1);
			}
			
		}// for
		
		
		boolean dbResult = false;
		String result = "success";
		String msg = "";
		
		log.info("$$$ 일단 여기까지 찍어본 userRequest: {}", userRequest.toString());
		
		
		try {
			
			try {
				recommResultList = campRecommService.predictCampWithNaver(userRequest)
													.stream()
													.sorted((o1, o2) -> o2.getSatisfaction() - o1.getSatisfaction())
													.limit(10)
													.sorted((o1, o2) -> Integer.parseInt(o2.getReviewPositive()) - Integer.parseInt(o1.getReviewPositive()))
													.limit(5)
													.toList();
				
				for (UserResultVO userResult : recommResultList) {
					
					log.info("▶▶▶▶▶ 최종 추천 캠핑장 리스트 캠핑장 이름: {}, 만족도: {}, 긍정평가 갯수(평판): {}", 
							userResult.getCampName(), userResult.getSatisfaction(), userResult.getReviewPositive());
				}
				
				
				dbResult = true;
				
			} catch(Exception e) {
				log.error("추천 캠핑장 리스트 얻기 실패");
				e.printStackTrace();
			}
			
			//TODO: DB 체크

			log.info("＃＃＃ getRecomm result : {}", result);
			
			// axios 통신에 성공하고, 닉네임이 존재하지 않는다면
			if (result.equals("success") && dbResult == true) {
				msg = "axios 통신 성공 + 캠핑장 추천 리스트 얻기 성공!";

				responseEntity = new ResponseEntity<>(recommResultList, HttpStatus.OK);

			// axios 통신에 성공하고, 닉네임이 이미 존재한다면
			} else if (!result.equals("success") && dbResult == false) {
				msg = "axios 통신은 성공했지만 - 캠핑장 추천 리스트 얻는데는 실패하였다...";

				responseEntity = new ResponseEntity<>(HttpStatus.OK);

			}

			log.info("＃＃＃ 전송되는 메시지: {}", msg);

		} catch (Exception e) {
			log.error("＃＃＃ getRecomm Error : {}", e);
			e.printStackTrace();

			// String errMsg = "getRecomm 에러 났어 슈발!";

			// 실패 코드(417) : 내부 서버 에러
			responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		
		return responseEntity;
	}

}
