package com.mbc.pmac.campServiceTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mbc.pmac.domain.CampFilteredVO;
import com.mbc.pmac.domain.CampVO;
import com.mbc.pmac.service.CampService;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class CampServiceTest {
	
	@Autowired
	CampService cs;

	@Test
	void test() {
		
		List<CampFilteredVO> customCampingList = new ArrayList<>();
		
		String campUrlRiver = "https://gocamping.or.kr/bsite/camp/info/list.do?pageUnit=10&searchKrwd=&listOrdrTrget=c_rdcnt&searchLctCl=52&pageIndex=";
		
		String campUrlMountain = "https://gocamping.or.kr/bsite/camp/info/list.do?pageUnit=10&searchKrwd=&listOrdrTrget=c_rdcnt&searchLctCl=49&pageIndex=";
		
		String theme = "강";
		
		customCampingList = cs.loadCampDataCustomVersion(campUrlRiver, 30, theme);
		
		log.info("customCampingList의 크기는:: {}", customCampingList.size());
		
		for (CampFilteredVO cv : customCampingList) {
			
			log.info("내용 전체: {}", cv.toString());
			
		}
		
	}

}
