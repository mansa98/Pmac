package com.mbc.pmac.CRUDTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mbc.pmac.domain.CampReviewVO;
import com.mbc.pmac.repository.CampReviewRepository;
import com.mbc.pmac.service.CampService;
// 0525 재욱
@SpringBootTest
class CampReviewDataInsertTest {

	@Autowired
	CampReviewRepository campReviewRepo;
	
	@Autowired
	CampService campService;
	
	@Test
	void test() {
	
		
		List<CampReviewVO> reviewList = campService.loadNaverReivewDatas();
		
		for (CampReviewVO review : reviewList) {
			
			campReviewRepo.save(review);
			
		}// for
		
	}// test()

}
