package com.mbc.pmac.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mbc.pmac.domain.CampFilteredVO;
import com.mbc.pmac.domain.CampReviewVO;
import com.mbc.pmac.domain.CampVO;
import com.mbc.pmac.repository.CampFilteredRepository;
import com.mbc.pmac.repository.CampRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CampService {
	
	@Autowired
	CampRepository campRepo;

	@Autowired
	CampFilteredRepository cFR;
	
	@Transactional(readOnly = true)
	public CampFilteredVO selectCamp(int id) {
		
//		return campRepo.findById(id);
		return cFR.findById(id);
	}
	@Transactional(readOnly = true)
	public CampFilteredVO selectJibunAddress(String JibunAddress) {
		
//		return campRepo.findById(id);
		return cFR.findByJibunAddress(JibunAddress);
	}

	@Transactional
	public List<CampFilteredVO> saveDatas(List<CampFilteredVO> campDataList) {

		List<CampFilteredVO> campList = new ArrayList<>();
		
		for (CampFilteredVO camp : campDataList) {
			
			cFR.save(camp);
			campList.add(cFR.save(camp));
		}
		
		return campList;
		
	}// saveDatas()
	
	@Transactional
	public CampFilteredVO saveData(CampFilteredVO campData) {
		
		CampFilteredVO campFilteredVO = null;
		
		try {
			campFilteredVO = cFR.save(campData);
			
		} catch(Exception e) {
			log.error("saveDataError : " + e);
			e.printStackTrace();
		}
		
		return campFilteredVO;
	}
	
	@Transactional
	public List<CampFilteredVO> getDatasFromDB() {
		
		List<CampFilteredVO> campList = new ArrayList<>();
		
		campList = (List<CampFilteredVO>)cFR.findAll();
		
		return campList;
		
	}// getDatasFromDB()
	
	
	
	public List<CampVO> loadCampData() {

		List<CampVO> campings = new ArrayList<>();
		String filename = "한국문화정보원_전국 문화 여가 활동 시설(캠핑) 데이터_20221130.CSV";

		String[] campInfo;
		CampVO campVO;

		try {

			ClassPathResource resource = new ClassPathResource("/csv/" + filename);

			CSVReader csvReader
				= new CSVReader(new InputStreamReader(new FileInputStream(resource.getFile()), "EUC-KR"));

			csvReader.readNext(); // 컬럼명 제외

			while ((campInfo = csvReader.readNext()) != null) {

				// log.info("campName :{}", campInfo[0]);

				campVO = CampVO.builder()
										.campName(campInfo[0])
										.cate1(campInfo[1])
										.cate2(campInfo[2])
										.cate3(campInfo[3])
										.sidoName(campInfo[4])
										.sigugunName(campInfo[5])
										.eupmyundongName(campInfo[6])
										.ryName(campInfo[7])
										.bunjiName(campInfo[8])
										.roadName(campInfo[9])
										.buildingNum(campInfo[10])
										.latitude(campInfo[11])
										.longitude(campInfo[12])
										.zip(campInfo[13])
										.roadAddress(campInfo[14])
										.jibunAddress(campInfo[15])
										.phone(campInfo[16])
										.homepage(campInfo[17])
										.vendor(campInfo[18])
										.weekdayOpStatus(campInfo[19])
										.weekendOpStatus(campInfo[20])
										.springOpStatus(campInfo[21])
										.summerOpStatus(campInfo[22])
										.fallOpStatus(campInfo[23])
										.winterOpStatus(campInfo[24])
										.facilElectricity(campInfo[25])
										.facilHotWater(campInfo[26])
										.facilWifi(campInfo[27])
										.facilCampfire(campInfo[28])
										.facilTrail(campInfo[29])
										.facilPool(campInfo[30])
										.facilPlayground(campInfo[31])
										.facilMart(campInfo[32])
										.facilRestroom(campInfo[33])
										.facilShowerroom(campInfo[34])
										.facilSink(campInfo[35])
										.facilExtinguisher(campInfo[36])
										.surrFacilFishing(campInfo[37])
										.surrFacilTrail(campInfo[38])
										.surrFacilBeach(campInfo[39])
										.surrFacilMaritimeLeisure(campInfo[40])
										.surrFacilValley(campInfo[41])
										.surrFacilStream(campInfo[42])
										.surrFacilPool(campInfo[43])
										.surrFacilYouthExperience(campInfo[44])
										.surrFacilRuralExperience(campInfo[45])
										.surrFacilChildrensPlay(campInfo[46])
										.glamBed(campInfo[47])
										.glamTv(campInfo[48])
										.glamFreezer(campInfo[49])
										.glamInternet(campInfo[50])
										.glamRestroom(campInfo[51])
										.glamAircon(campInfo[52])
										.glamHeater(campInfo[53])
										.glamCookware(campInfo[54])
										.facilCharacteristics(campInfo[55])
										.facilDetail(campInfo[56])
										.regDate(Date.valueOf("2024-05-09").toString()) // 등록일은 임의 일자로 생성
										.build();

				campings.add(campVO);

			} // while

		} catch (IOException e) {
			log.error("readCampingsFromCSV : {}", e);
			e.printStackTrace();
		} catch (CsvValidationException e) {
			log.error("readCampingsFromCSV : {}", e);
			e.printStackTrace();
		}

		log.info("camping size : {}", campings.size());

		return campings;
	}// loadCampData()
	
	
	public List<CampFilteredVO> loadCampDataCustomVersion(String url, int pageIndex, String theme) {

		List<CampFilteredVO> campings = new ArrayList<>();
		List<CampFilteredVO> campingsCustom = new ArrayList<>();
		List<CampFilteredVO> campingsFinal = new ArrayList<>();
		String filename = "한국문화정보원_전국 문화 여가 활동 시설(캠핑) 데이터_20221130.CSV";

		String[] campInfo;
		CampFilteredVO campFilteredVO;
		
		// 고캠핑에서 "강" 테마로 검색했을때 나오는 갤러리
		String campUrlRiver = "https://gocamping.or.kr/bsite/camp/info/list.do?pageUnit=10&searchKrwd=&listOrdrTrget=c_rdcnt&searchLctCl=52&pageIndex=";
		
		// 고캠핑에서 "산" 테마로 검색했을때 나오는 갤러리
		String campUrlMountain = "https://gocamping.or.kr/bsite/camp/info/list.do?pageUnit=10&searchKrwd=&listOrdrTrget=c_rdcnt&searchLctCl=49&pageIndex=";
		
		// 캠핑장 이름들을 담아볼까
		List<String> campNameList = new ArrayList<>();
		
		for (int pageNum=1; pageNum <= pageIndex; pageNum++) {
		
			try {
				
				// 1페이지 분량(접속)
				Document doc = Jsoup.connect(url + pageNum).get();
				
				// Document의 하위 계층
				Elements elems = doc.select("div[id=\"wrap\"]");
				
				// campNameList에 add될 낱개의 캠핑장 이름을 담을 String 변수
				String campName = "";
				
				// Elements는 결국 Element를 요소로 하는 List입니다.
				// div[class=\"camp_cont\"] h2[class=\"camp_tt\"] a
				// 위 태그에 해당하는 모든 html 요소를 페이지 위에서부터 아래로 훑으면서 리스트에 담아서 리스트를 구성합니다.
				List<Element> elemList = elems.select("div[class=\"camp_cont\"] h2[class=\"camp_tt\"] a");
				
				for (int i = 0; i < elemList.size(); i++) {
					
					// html 요소에서 text만 뽑아낸다음에 ] 기준으로 잘라서(return이 배열입니다) 
					// return된 배열의 1번째 요소( ] 뒤 텍스트) 추출 후 마지막 trim()으로 혹시 있을 공백을 제거합니다
					campName = elemList.get(i).text().split("]")[1].trim();
					
					// 위에서 정제해서 만든 campName을 캠핑장 이름 리스트(campNameList)에 추가~!
					campNameList.add(campName);
					
				}
				
				//log.info("elemList: {}", elemList.get(0));
				
				
			} catch(Exception e) {
				
				log.error("고캠핑 크롤링 대 실패!!!!!!!!!!");
				e.printStackTrace();
			}// try catch
		
		}// for
		
		log.info("§§§ campNameList의 크기: {}", campNameList.size());
		
		campNameList.forEach(System.out::println);

		
		
		

		try {

			ClassPathResource resource = new ClassPathResource("/csv/" + filename);

			CSVReader csvReader
				= new CSVReader(new InputStreamReader(new FileInputStream(resource.getFile()), "EUC-KR"));

			csvReader.readNext(); // 컬럼명 제외

			while ((campInfo = csvReader.readNext()) != null) {

				// log.info("campName :{}", campInfo[0]);

				campFilteredVO = CampFilteredVO.builder()
										.campName(campInfo[0])
										.cate1(campInfo[1])
										.cate2(campInfo[2])
										.cate3(campInfo[3])
										.sidoName(campInfo[4])
										.sigugunName(campInfo[5])
										.eupmyundongName(campInfo[6])
										.ryName(campInfo[7])
										.bunjiName(campInfo[8])
										.roadName(campInfo[9])
										.buildingNum(campInfo[10])
										.latitude(campInfo[11])
										.longitude(campInfo[12])
										.zip(campInfo[13])
										.roadAddress(campInfo[14])
										.jibunAddress(campInfo[15])
										.phone(campInfo[16])
										.homepage(campInfo[17])
										.vendor(campInfo[18])
										.weekdayOpStatus(campInfo[19])
										.weekendOpStatus(campInfo[20])
										.springOpStatus(campInfo[21])
										.summerOpStatus(campInfo[22])
										.fallOpStatus(campInfo[23])
										.winterOpStatus(campInfo[24])
										.facilElectricity(campInfo[25])
										.facilHotWater(campInfo[26])
										.facilWifi(campInfo[27])
										.facilCampfire(campInfo[28])
										.facilTrail(campInfo[29])
										.facilPool(campInfo[30])
										.facilPlayground(campInfo[31])
										.facilMart(campInfo[32])
										.facilRestroom(campInfo[33])
										.facilShowerroom(campInfo[34])
										.facilSink(campInfo[35])
										.facilExtinguisher(campInfo[36])
										.surrFacilFishing(campInfo[37])
										.surrFacilTrail(campInfo[38])
										.surrFacilBeach(campInfo[39])
										.surrFacilMaritimeLeisure(campInfo[40])
										.surrFacilValley(campInfo[41])
										.surrFacilStream(campInfo[42])
										.surrFacilPool(campInfo[43])
										.surrFacilYouthExperience(campInfo[44])
										.surrFacilRuralExperience(campInfo[45])
										.surrFacilChildrensPlay(campInfo[46])
										.glamBed(campInfo[47])
										.glamTv(campInfo[48])
										.glamFreezer(campInfo[49])
										.glamInternet(campInfo[50])
										.glamRestroom(campInfo[51])
										.glamAircon(campInfo[52])
										.glamHeater(campInfo[53])
										.glamCookware(campInfo[54])
										.facilCharacteristics(campInfo[55])
										.facilDetail(campInfo[56])
										.regDate(Date.valueOf("2024-05-09").toString()) // 등록일은 임의 일자로 생성
										.theme(theme)
										.build();
				

				campings.add(campFilteredVO);

			} // while
			
			// 2447개에서 우리가 필요한 280개만 추출해내기
			for (int i = 0; i < campings.size(); i++) {
				
				// 280개의 캠핑장 이름과 비교해서 일치하는게 있는지 검색
				for (int j = 0; j < campNameList.size(); j++) {
					
					if (campings.get(i).getCampName().contains(campNameList.get(j))) {
						
						campingsCustom.add(campings.get(i));
						
					}// if
					
				}// for
				
			}// for
			
			// 중복 제거를 위해 Set으로 일시 전환
			Set<CampFilteredVO> campingCustomSet = new HashSet<>(campingsCustom);
			
			campingsFinal = List.copyOf(campingCustomSet);
			
			
			
			
			log.info("◆◆◆ campingsFinal의 크기는? {}", campingsFinal.size());
			
			for (CampFilteredVO cmp : campingsFinal) {
				
				log.info("새로운 리스트의 캠핑장 제목들: {}", cmp.getCampName());
				
			}

		} catch (IOException e) {
			log.error("readCampingsFromCSV : {}", e);
			e.printStackTrace();
		} catch (CsvValidationException e) {
			log.error("readCampingsFromCSV : {}", e);
			e.printStackTrace();
		}

		//log.info("camping size : {}", campings.size());

		return campingsFinal;
	}// loadCampDataCustomVersion()
	
	// 0525 재욱
	// 네이버 리뷰 평점 및 긍정/부정 데이터 csv 파일로부터 가져오기
	public List<CampReviewVO> loadNaverReivewDatas() {
		
		List<CampReviewVO> reviewList = new ArrayList<>();
		
		String filename = "camp_review_tb_new.csv";

		String[] campReviewInfo;
		
		CampReviewVO campReviewVO;
		
		try {

			ClassPathResource resource = new ClassPathResource("/csv/" + filename);

			CSVReader csvReader
				= new CSVReader(new InputStreamReader(new FileInputStream(resource.getFile()), "EUC-KR"));

			csvReader.readNext(); // 컬럼명 제외

			while ((campReviewInfo = csvReader.readNext()) != null) {

				// log.info("campName :{}", campInfo[0]);

				campReviewVO = CampReviewVO.builder()
										   .campName(campReviewInfo[1])
										   .avgRating(campReviewInfo[2])
										   .reviews(campReviewInfo[3])
										   .predict(Integer.parseInt(campReviewInfo[4]))
										   .build();

				reviewList.add(campReviewVO);

			} // while

		} catch (IOException e) {
			log.error("readCampingReviewsFromCSV : {}", e);
			e.printStackTrace();
		} catch (CsvValidationException e) {
			log.error("readCampingReviewsFromCSV : {}", e);
			e.printStackTrace();
		}

		
		
		return reviewList;
	}// loadNaverReivewDatas()

	
	
	
	
	

}
