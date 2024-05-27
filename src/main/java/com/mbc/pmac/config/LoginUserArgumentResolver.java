package com.mbc.pmac.config;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.mbc.pmac.domain.LoginUser;
import com.mbc.pmac.domain.SessionUser;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

// 네아로
@RequiredArgsConstructor
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

	
	private final HttpSession httpSession;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		
		boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;
		// 구글 추가
		boolean isUserClass = SessionUser.class.equals(parameter.getParameterType());
		
		return isLoginUserAnnotation && isUserClass;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		// 구글 추가
		return httpSession.getAttribute("user");
	}
	
	
	
}
