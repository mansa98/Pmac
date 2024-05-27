package com.mbc.pmac.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 네아로
@Getter
@Builder // 0525 재욱
@AllArgsConstructor // 0525 재욱
@NoArgsConstructor // 0525 재욱
public class SessionUser implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private String email;
	private String provider;
	private String nickname; // 0525 재욱
	
	public SessionUser(User user) {
		
		this.name = user.getName();
		this.email = user.getEmail();
		this.provider = user.getProvider();
		this.nickname = user.getNickname(); // 0525 재욱
		
	}// SessionUser()
	
}
