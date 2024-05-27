package com.mbc.pmac.service;
// 0525 재욱
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mbc.pmac.domain.CampFilteredVO;
import com.mbc.pmac.domain.CampReviewVO;
import com.mbc.pmac.domain.UserRequestVO;
import com.mbc.pmac.domain.UserResultVO;
import com.mbc.pmac.repository.CampFilteredRepository;
import com.mbc.pmac.repository.CampImageRepository;
import com.mbc.pmac.repository.CampReviewRepository;

import lombok.extern.slf4j.Slf4j;

// 캠핑장추천

@Service
@Slf4j
public class CampRecommService {
	
	@Autowired
	CampFilteredRepository campFilteredRepo;
	
	@Autowired
	CampReviewRepository campReviewRepo;
	
	@Autowired
	CampImageRepository campImageRepo;

	/**
	 * 계절 판정
	 */
	public boolean checkEnableSeason(CampFilteredVO campFilteredVO, LocalDate date) {
		
		boolean flag = false;
		
		int month = date.getMonthValue();
		
		flag = month >= 3 && month <= 5 ? campFilteredVO.getSpringOpStatus().equals("봄 운영") :
			   month >= 6 && month <= 8 ? campFilteredVO.getSummerOpStatus().equals("여름 운영") :
			   month >= 9 && month <= 11 ? campFilteredVO.getFallOpStatus().equals("가을 운영") :
			   campFilteredVO.getWinterOpStatus().equals("겨울 운영");
			   
		
		return flag;
	}// checkEnableSeason()
	
	
	/**
	 * 만족도 계산 : 네이버 평점 포함 VO
	 */
	public int calcSatisfaction(UserResultVO userResult) {
		
		log.info("userResult: {}", userResult);
		
		int result = 0;
		int totalFactor = 11; // 총 만족도 조사 요건 ex)지역, ... 낚시 가능 여부
		int factorSum = 1; // 사용자 요청 지역과 일치한다는 전제로 +1으로 시작
		
		// 사용자가 선택한 날짜를 계절로 치환해서 캠핑장이 그 계절 운영 가능이면 factorSum에 +1
		factorSum = userResult.getEnableOps().contains("운영 가능") ? factorSum++ : factorSum;
		
		factorSum += userResult.getIsElectricity();
		factorSum += userResult.getIsWifi();
		factorSum += userResult.getIsCampfire();
		factorSum += userResult.getIsHeater();
		factorSum += userResult.getIsPool();
		factorSum += userResult.getIsPlayground();
		factorSum += userResult.getIsTrail();
		factorSum += userResult.getIsMaritimeLeisure();
		factorSum += userResult.getIsFishing();
		
		
		log.info("factorSum: {}", factorSum);
		log.info("totalFactor: {}", totalFactor);
		
		// 만족도 계산(100% 만땅 기준)
		result = (int)((float)factorSum / (float)totalFactor * 100);
		
		log.info("만족도: {}", result);
		
		return result;
	}// calcSatisfaction()
	
	
	/**
	 * 개인별 캠프 추천
	 */
	public List<UserResultVO> predictCampWithNaver(UserRequestVO userRequestVO) {
		
		List<UserResultVO> recommList = new ArrayList<>();
		// 이 부분을 전체 캠핑장 리스트 => 테마로 찾기로 변경했음
		List<CampFilteredVO> campListByTheme = campFilteredRepo.findAllByTheme(userRequestVO.getTheme());
		
		String dest = userRequestVO.getDestination();
		log.info("행선 예정지: {}", dest);
		
		// 유저가 원하는 지역의 캠핑장들만 추출
		campListByTheme = campListByTheme.stream()
										 .filter(x -> (x.getSigugunName() != null &&
										 (x.getSidoName().contains(dest) || x.getSigugunName().contains(dest))))
										 .toList();
		
		UserResultVO userResult = null;
		
		// 나머지 요구사항 반영 비교
		for (CampFilteredVO campFilteredVO : campListByTheme) {
			
			log.info("나머지 요구사항 반영 for문 진입");
			
			userResult = new UserResultVO();
			
			String campImageMain = "";
			// 여기서 캠프 이미지 추가(만약 존재한다면!!)
			if (campImageRepo.findAllByCampName(campFilteredVO.getCampName()) != null) {
				
				campImageMain = campImageRepo.findAllByCampName(campFilteredVO.getCampName()).get(0).getCampImageMain();
				
				userResult.setCampImageMain(campImageMain);
			}
			// 원하는 날짜(계절)에 운영 여부
			boolean enableOps = this.checkEnableSeason(campFilteredVO, userRequestVO.getTime());
			
			String opsMsg = userRequestVO.getTime().format(DateTimeFormatter.ofPattern("YYYY년 MM월 dd일"))
					+ (enableOps == true ? "운영 가능" : "운영 불가");
			
			userResult.setEnableOps(opsMsg);
			
			// 부대/주변 시설 여부 점검
			userResult.setIsElectricity(campFilteredVO.getFacilElectricity().equals("전기 사용가능") ? 1 : 0);
			userResult.setIsWifi(campFilteredVO.getFacilWifi().equals("wifi 사용가능") ? 1 : 0);
			userResult.setIsCampfire(campFilteredVO.getFacilCampfire().equals("장작판매") ? 1 : 0);
			userResult.setIsHeater(campFilteredVO.getFacilHotWater().equals("온수 사용가능") ? 1 : 0);
			userResult.setIsPool(campFilteredVO.getFacilPool().equals("물놀이장 보유") ? 1 : 0);
			userResult.setIsPlayground(campFilteredVO.getFacilPlayground().equals("놀이터 보유") ? 1 : 0);
			userResult.setIsTrail(campFilteredVO.getFacilTrail().equals("산책로 있음") ? 1 : 0);
			userResult.setIsMaritimeLeisure(campFilteredVO.getSurrFacilMaritimeLeisure().equals("시설 주변 물놀이(수상레저) 있음") ? 1 : 0);
			userResult.setIsFishing(campFilteredVO.getSurrFacilFishing().equals("낚시 시설 있음") ? 1 : 0);
			
			// 기타
			userResult.setCampId(campFilteredVO.getId());
			userResult.setCampName(campFilteredVO.getCampName());
			userResult.setNickname(userRequestVO.getNickname());
			userResult.setDestination(userRequestVO.getDestination());
			
			// 사용자의 요구사항에 대한 만족도(100% 기준) 계산
			int satisfaction = this.calcSatisfaction(userResult);
			
			userResult.setSatisfaction(satisfaction);
			
			// 네이버 별점 및 긍정/부정지수 반영
			List<CampReviewVO> reviewList = campReviewRepo.findAllByCampName(campFilteredVO.getCampName());
			
			log.info("reviewList 크기: {}", reviewList.size());
			
			String avgRating = ""; // 네이버 별점
			int positiveDegree = 0; // 긍정 지수
			int negativeDegree = 0; // 부정 지수
			
			// 해당 캠핑장의 리뷰가 존재한다면
			if (reviewList.size() > 0) {
				// 네이버 리뷰 별점 정보가 있으면 가져오기
				avgRating = (reviewList.get(0).getAvgRating() == null || reviewList.get(0).getAvgRating().equals("")) ?
							"별점정보 없음" : reviewList.get(0).getAvgRating();
				
				// 네이버리뷰 테이블에서 predict가 1인(긍정평가) 행의 갯수 count
				positiveDegree = (int)reviewList.stream().filter(x -> x.getPredict() == 1).count();
				negativeDegree = (int)reviewList.stream().filter(x -> x.getPredict() == 0).count();
			} else {
				
				avgRating = "별점정보 없음";
				positiveDegree = 0;
				negativeDegree = 0;
			}
			
			userResult.setAvgRating(avgRating);
		
			log.info("긍정 지수: {}", positiveDegree);
			log.info("부정 지수: {}", negativeDegree);
			
			// int => String 형 변환?
			userResult.setReviewPositive(positiveDegree + "");
			userResult.setReviewNegative(negativeDegree + "");
			
			log.info("추천 캠핑장: {}", userResult);
			
			// 최종 리스트에 추가
			recommList.add(userResult);
			
			log.info("나머지 요구사항 반영 for문 종료");
			
		}// for
		
		
		log.info("recommList size: {}", recommList.size());
		
		return recommList;
		
	}// predictCamp()
	
}
