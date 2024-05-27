package com.mbc.pmac.crawlingTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class CampingNamesCrawlingTest {

	//@Test
	void test() {
		
		// 고캠핑에서 "강" 테마로 검색했을때 나오는 갤러리
		String campUrlRiver = "https://gocamping.or.kr/bsite/camp/info/list.do?pageUnit=10&searchKrwd=&listOrdrTrget=c_rdcnt&searchLctCl=52&pageIndex=";
		
		String campUrlMountain = "https://gocamping.or.kr/bsite/camp/info/list.do?pageUnit=10&searchKrwd=&listOrdrTrget=c_rdcnt&searchLctCl=49&pageIndex=";
		
		// 캠핑장 이름들을 담아볼까
		List<String> campNameList = new ArrayList<>();
		
		for (int pageNum=1; pageNum <= 30; pageNum++) {
		
			try {
				
				// 1페이지 분량(접속)
				Document doc = Jsoup.connect(campUrlMountain + pageNum).get();
				
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
					
//					if (campName.equals("[경기도 가평군] 일칠일관광농원") 
//							|| campName.equals("[강원도 정선군] 우니메이카 정선") 
//							|| campName.equals("[강원도 화천군] 동지화캠핑장")
//							|| campName.equals("[경기도 연천군] 준삼형제글램핑"
//									){
//						
//						campName = "";
//					}
					
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
		
	}// test()

}
