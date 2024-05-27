package com.mbc.pmac.CRUDTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mbc.pmac.domain.CampFilteredVO;
import com.mbc.pmac.repository.CampFilteredRepository;
import com.mbc.pmac.repository.CampRepository;
import com.mbc.pmac.service.CampService;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class CampInfoInsertTest {

	@Autowired
	CampService campService;

	@Autowired
	CampFilteredRepository cFR;

	@Test
	public void test() {
		
		String campUrlRiver = "https://gocamping.or.kr/bsite/camp/info/list.do?pageUnit=10&searchKrwd=&listOrdrTrget=c_rdcnt&searchLctCl=52&pageIndex=";
		
		String campUrlMountain = "https://gocamping.or.kr/bsite/camp/info/list.do?pageUnit=10&searchKrwd=&listOrdrTrget=c_rdcnt&searchLctCl=49&pageIndex=";
		
		List<CampFilteredVO> campListFinal = new ArrayList<>();
		
		campListFinal = campService.loadCampDataCustomVersion(campUrlRiver, 28, "강");
//		campListFinal = campService.loadCampDataCustomVersion(campUrlMountain, 30, "산");
		log.info("데이터 크기 : {}", campListFinal.size());

		for (CampFilteredVO camp : campListFinal) {

			 cFR.save(camp);
		} //

	} //


}
