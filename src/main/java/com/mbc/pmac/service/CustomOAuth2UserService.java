package com.mbc.pmac.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.mbc.pmac.domain.OAuthAttributes;
import com.mbc.pmac.domain.SessionUser;
import com.mbc.pmac.domain.User;
import com.mbc.pmac.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


// 네아로

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final UserRepository userRepository;
	
	private final HttpSession httpSession;
	
	
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		
		OAuth2UserService delegate = new DefaultOAuth2UserService();
		
		OAuth2User oAuth2User = delegate.loadUser(userRequest);
		
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		
		String userNameAttributeName = userRequest.getClientRegistration()
												  .getProviderDetails()
												  .getUserInfoEndpoint()
												  .getUserNameAttributeName();
		
		//String provider = userRequest.getClientRegistration().getRegistrationId();
		
		// 여기서 네이버 / 구글 검사
		// 네이버 / 구글 로직이 조금 다름
		OAuthAttributes attributes = OAuthAttributes.of(registrationId, 
														userNameAttributeName,
														oAuth2User.getAttributes());
		
		User user = saveOrUpdate(attributes);
		
		user.setProvider(attributes.getProvider());
		
		log.info("◆◆◆ 토큰을 받아서 완성시킨 우리의 유저는:: {}", user.toString());
		
		httpSession.setAttribute("user", new SessionUser(user));
		
		return new DefaultOAuth2User(
				Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
				attributes.getAttributes(),
				attributes.getNameAttributeKey());
		
	}
	
	
	private User saveOrUpdate(OAuthAttributes attributes) {
		// 업데이트 로직 추가 (존재하는 data의 provider와 새로 받아온 provider를 비교해서 일치하면 update, 불일치하면 insert)
		User user = userRepository.findByEmailAndProviderAndIsUser(attributes.getEmail(), attributes.getProvider())
				// 업데이트 부분(이름과 마지막 로그인(modifiedDate)만 업데이트)
				.map(entity -> entity.update(attributes.getName()))
				// findByEmailAndIsUSer의 return값이 null일때 실행
				.orElse(attributes.toEntity());
		
		return userRepository.save(user);
		
	}

}
