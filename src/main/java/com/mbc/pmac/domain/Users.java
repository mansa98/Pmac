package com.mbc.pmac.domain;

import lombok.Data;

@Data
public class Users {

	private String id;
	private String pw;
	private String memberNick;
	private int enabled;
	
}
