package com.mbc.pmac.CampServiceTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
		
		List<CampVO> customCampingList = new ArrayList<>();
		
		customCampingList = cs.loadCampDataCustomVersion();
		
		log.info("customCampingList의 크기는:: {}", customCampingList.size());
		
		for (CampVO cv : customCampingList) {
			
			log.info("제목만: {}", cv.getCampName());
			
		}
		
	}

}
