package com.mbc.pmac.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.mbc.pmac.domain.SessionUser;
import com.mbc.pmac.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
	
	@Autowired
	UserRepository userRepo;
	
	@Transactional(rollbackFor=Exception.class)
	public boolean updateNickname(String nickname, String email, String provider) {
		
		boolean result = false;
		
		try {
			// 이메일과 provider 둘 모두 일치해야 해당 유저로 판명!
			userRepo.addNickname(nickname, email, provider);
			
			result = true;
		} catch(Exception e) {
			
			log.error("유저 닉네임 업데이트(추가) 실패!!");
			e.printStackTrace();
		}
		
		return result;
		
	}// updateNickname()

}
