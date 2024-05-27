package com.mbc.pmac.UtilTest;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;


@SpringBootTest
@Slf4j
class LocalDateTimeTest {

	@Test
	void test() {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		
		
		LocalDateTime createdDate = LocalDateTime.parse(currentTime, formatter);
		
		log.info("몇시고, {}", createdDate.toString());
		
		
		
		
	}

}
