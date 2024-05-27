package com.mbc.pmac.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 네아로

@Getter
@RequiredArgsConstructor
public enum Role {

	USER("ROLE_USER", "회원"),
	ADMIN("ROLE_ADMIN", "관리자");
	
	private final String key;
	
	private final String title;
	
}
