package com.mbc.pmac.CRUDTest;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mbc.pmac.service.CampImageService;

@SpringBootTest
class CampListInsertTest {


	@Autowired
	CampImageService campImageService;
	
	@Test
	// 강테마 크롤링
	public void testRiverCrawling() throws IOException {
	        // 크롤링 메소드 호출
	        campImageService.riverCrawling();
	    }
	@Test
	// 산테마 크롤링
	public void testMountainCrawling() throws IOException {
	        // 크롤링 메소드 호출
	        campImageService.mountainCrawling();
	    }
}
