package com.mbc.pmac.UtilTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mbc.pmac.repository.CampReviewRepository;

import lombok.extern.slf4j.Slf4j;


@SpringBootTest
@Slf4j
class MapTest {
	
	@Autowired
	CampReviewRepository campReviewRepo;

	@Test
	void test() {
		
		
		List<Map<String, Integer>> myMap = new ArrayList<>();
		
		
		myMap = campReviewRepo.findByTopRanking();
		
		log.info("해줘: {}", myMap);
		
		List<String> campNameList = new ArrayList<>();
		
		log.info("List<Map> 찢기 1단계: {}", myMap.get(0));
		
		Map<String, Integer> miniMap1 = myMap.get(0);
		
		log.info("List<Map> 찢기 2단계: {}", miniMap1.keySet().toArray());
		
//		for (int i = 0; i < myMap.size(); i++) {
//			
//			
//			campNameList.add(myMap.get(i).keySet().)
//			
//		}
		
		
	}

}
