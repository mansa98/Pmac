package com.mbc.pmac.domain;

import java.util.Map;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

// 네아로
@Getter
@Setter // 추가
@Slf4j
public class OAuthAttributes {

	private Map<String, Object> attributes;

	private String nameAttributeKey;
	
	private String name;
	
	private String email;
	
	private String provider;
	
	@Builder
	public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email) {
		this.attributes = attributes;
		this.nameAttributeKey = nameAttributeKey;
		this.name = name;
		this.email = email;
		
	}// OAuthAttributes()
	
//	public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
	public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {	
		
		OAuthAttributes result = null;
		
		if ("naver".equals(registrationId)) {
			
			result = ofNaver("id", attributes);
			
		} else if ("google".equals(registrationId)) {
			
			result = ofGoogle(userNameAttributeName, attributes);
		}
		
		log.info("★★★ registrationId AKA provider: {}", registrationId);
		
		result.setProvider(registrationId);
		
		return result;
		
	}// of()
	
	@SuppressWarnings("unchecked")
	private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
		
		Map<String, Object> response = (Map<String, Object>) attributes.get("response");
		
		return OAuthAttributes.builder()
							  .name((String) response.get("name"))
							  .email((String) response.get("email"))
							  .attributes(response)
							  .nameAttributeKey(userNameAttributeName)
							  .build();
		
	}// ofNaver
	
	private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
		
		return OAuthAttributes.builder()
							  .name((String) attributes.get("name"))
							  .email((String) attributes.get("email"))
							  .attributes(attributes)
							  .nameAttributeKey(userNameAttributeName)
							  .build();
		
	}// ofGoogle()
	
	
	public User toEntity() {
		
		return User.builder()
				   .name(name)
				   .email(email)
				   .role(Role.USER)
				   .provider(provider)
				   .build();
		
	}// toEntity()
	
	
}// OAuthAttributes
