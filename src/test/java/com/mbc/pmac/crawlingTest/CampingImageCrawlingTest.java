package com.mbc.pmac.crawlingTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;

@SpringBootTest
@Slf4j
class CampingImageCrawlingTest {
	//강
	//@Test
	void test() throws IOException, InterruptedException{
		
		String river_url ="https://gocamping.or.kr/bsite/camp/info/list.do?pageUnit=10&searchKrwd=&listOrdrTrget=c_rdcnt&searchLctCl=52&pageIndex=";
		
		List<String> riverList = new ArrayList<>();
		
		List<String> campNameList = new ArrayList<>();
		
		List<String> thumbList = new ArrayList<>();
		
		
		
		int pageNum = 28;
		
		for (int page = 1; page <= pageNum; page++) {
			
			Document doc = Jsoup.connect(river_url + page).get();
			
			Elements id_elems = doc.select("h2[class=\"camp_tt\"] a");
			
			
			String id = "";
			
			for (Element elem : id_elems) {
				
				id = elem.attr("href").replace("/bsite/camp/info/read.do?c_no=", "");
				
				riverList.add(id);
			}
			
			
			// 고캠핑 지뢰 제거
			for (int i = 0; i < riverList.size(); i++) {
				
				if (riverList.get(i).equals("100367&viewType=read01&listOrdrTrget=c_rdcnt&searchLctCl=52") ||
						riverList.get(i).equals("100646&viewType=read01&listOrdrTrget=c_rdcnt&searchLctCl=52") ) {
					
					riverList.remove(i);
				}
				
			}// for
			
		} // for
		
		
		
		log.info("총 몇개의 food id를 획득하였지? {} 개", riverList.size());
		log.info("예시 한개 : " + riverList.get(237));
		//https://gocamping.or.kr/bsite/camp/info/read.do?c_no=100367&viewType=read01&listOrdrTrget=c_rdcnt&searchLctCl=52
		String detail_url = "https://gocamping.or.kr/bsite/camp/info/read.do?c_no=";
		
		
		for (int riverNum = 0; riverNum < riverList.size(); riverNum++) {
			Document doc = Jsoup.connect(detail_url + riverList.get(riverNum)).get();
			
			List<Element> river_name = doc.select("div[class=\"s_title2\"] p[class=\"camp_name\"]");
			
			List<Element> river_thumb = doc.select("div[class=\"camp_info_box\"] div[class=\"img_b\"] img");
			
			List<Element> river_elems = doc.select("div[class=\"camp_intro\"] li[class=\"col_03 img_box\"] img");
			
			List<String> imageList = new ArrayList<>();
			
			String campName = "";
			
			for (int i=0; i< river_name.size(); i++) {
				campName = river_name.get(i).text().trim();
				campNameList.add(campName);
			}
			
			log.info("캠핑장 이름: {}", campName);
			
			for (Element thumb : river_thumb) {
				String thumbImg = thumb.attr("src");
				thumbList.add(thumbImg);
			}
			
			for (Element img : river_elems) {
			    String imageUrl = img.attr("src");
			    imageList.add(imageUrl);
			}
			
			String imageString = "";
			
			for(int i =0; i <imageList.size(); i++) {
				imageString += "<div class=\"image_intro\"><img src=\"" + imageList.get(i) + "\"></div>";
			}
			
			log.info("캠프이름 개수 {}", campNameList.size());
			log.info("캠프섬네일 개수 {}", thumbList.size());
			log.info("캠프상세사진 형태 {} ", imageString);
			
			// (int) Math.random() * (최댓값-최소값+1) + 최소값
			
//			long startTime = System.currentTimeMillis();
//			int randomNum = (int) (Math.random() * (5000-1000+1) + 1000);
//			
//			Thread.sleep(randomNum);
//			long endTime = System.currentTimeMillis();
//			
//			log.info("잠든시간: {}ms", endTime - startTime);
			
		}// for
		
		
		log.info("캠프이름 :"+ campNameList.get(3));
		log.info("캠프섬네일 :"+ thumbList.get(3));
		
	}
	
	
	// 산
	@Test
	void mountainTest() throws IOException, InterruptedException{
		
		String mountain_url ="https://gocamping.or.kr/bsite/camp/info/list.do?pageUnit=10&searchKrwd=&listOrdrTrget=c_rdcnt&searchLctCl=49&pageIndex=";
		
		List<String> mountainList = new ArrayList<>();
		
		List<String> campNameList = new ArrayList<>();
		
		List<String> thumbList = new ArrayList<>();
		
		
		
		int pageNum = 30;
		
		for (int page = 1; page <= pageNum; page++) {
			
			Document doc = Jsoup.connect(mountain_url + page).get();
			
			Elements id_elems = doc.select("h2[class=\"camp_tt\"] a");
			
			
			String id = "";
			
			for (Element elem : id_elems) {
				
				id = elem.attr("href").replace("/bsite/camp/info/read.do?c_no=", "");
				
				mountainList.add(id);
			}
			
			
			// 고캠핑 지뢰 제거
			for (int i = 0; i < mountainList.size(); i++) {
				
				if (mountainList.get(i).equals("2474&viewType=read01&listOrdrTrget=c_rdcnt&searchLctCl=49")) {
					
					mountainList.remove(i);
				}
				
			}// for
			
		} // for
		
		
		
		log.info("총 몇개의 food id를 획득하였지? {} 개", mountainList.size());
		log.info("예시 한개 : " + mountainList.get(237));
		//https://gocamping.or.kr/bsite/camp/info/read.do?c_no=100367&viewType=read01&listOrdrTrget=c_rdcnt&searchLctCl=52
		String detail_url = "https://gocamping.or.kr/bsite/camp/info/read.do?c_no=";
		
		
		for (int mountainNum = 0; mountainNum < mountainList.size(); mountainNum++) {
			Document doc = Jsoup.connect(detail_url + mountainList.get(mountainNum)).get();
			
			List<Element> mountain_name = doc.select("div[class=\"s_title2\"] p[class=\"camp_name\"]");
			
			List<Element> mountain_thumb = doc.select("div[class=\"camp_info_box\"] div[class=\"img_b\"] img");
			
			List<Element> mountain_elems = doc.select("div[class=\"camp_intro\"] li[class=\"col_03 img_box\"] img");
			
			List<String> imageList = new ArrayList<>();
			
			String campName = "";
			
			for (int i=0; i< mountain_name.size(); i++) {
				campName = mountain_name.get(i).text().trim();
				campNameList.add(campName);
			}
			
			log.info("캠핑장 이름: {}", campName);
			
			for (Element thumb : mountain_thumb) {
				String thumbImg = thumb.attr("src");
				thumbList.add(thumbImg);
			}
			
			for (Element img : mountain_elems) {
			    String imageUrl = img.attr("src");
			    imageList.add(imageUrl);
			}
			
			String imageString = "";
			
			for(int i =0; i <imageList.size(); i++) {
				imageString += "<div class=\"image_intro\"><img src=\"" + imageList.get(i) + "\"></div>";
			}
			
			log.info("캠프이름 개수 {}", campNameList.size());
			log.info("캠프섬네일 개수 {}", thumbList.size());
			log.info("캠프상세사진 형태 {} ", imageString);
			
			// (int) Math.random() * (최댓값-최소값+1) + 최소값
			
//			long startTime = System.currentTimeMillis();
//			int randomNum = (int) (Math.random() * (5000-1000+1) + 1000);
//			
//			Thread.sleep(randomNum);
//			long endTime = System.currentTimeMillis();
//			
//			log.info("잠든시간: {}ms", endTime - startTime);
			
		}// for
		
		
		log.info("캠프이름 :"+ campNameList.get(3));
		log.info("캠프섬네일 :"+ thumbList.get(3));
		
	}

	
	
	
	
	
	
	
	
}
