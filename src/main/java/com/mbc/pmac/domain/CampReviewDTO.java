package com.mbc.pmac.domain;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.Getter;

@Getter
public class CampReviewDTO {

	private String campName;
	private String campImageMain;
	
	public CampReviewDTO(String campName, String campImageMain) {
		
		this.campName = campName;
		this.campImageMain = campImageMain;
		
	}
	
}
