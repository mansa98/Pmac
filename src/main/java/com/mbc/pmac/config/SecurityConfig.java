package com.mbc.pmac.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.mbc.pmac.service.CustomOAuth2UserService;
//import com.mbc.pmac.service.CustomProvider;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

//	@Autowired
//	private CustomProvider customProvider;	
	
	
	@SuppressWarnings("unused")
	//private UserDetailsService userDetailsService;

	private DataSource dataSource;
	
	private final CustomOAuth2UserService customOAuth2UserService;

//	public SecurityConfig(UserDetailsService userDetailsService, 
//						  DataSource dataSource,
//						  CustomOAuth2UserService customOAuth2UserService) {
	public SecurityConfig(DataSource dataSource,
				  CustomOAuth2UserService customOAuth2UserService) {

		log.info("생성자 주입");
		this.customOAuth2UserService = customOAuth2UserService;
		this.dataSource = dataSource;
//		this.userDetailsService = userDetailsService;
	}

//	@Bean
//	public BCryptPasswordEncoder bCryptPasswordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
	
	
    
    // security 적용 예외 URL 등록
	// bootstrap-icons/** 추가
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//    	
//    	// 게시판 : summernote 추가
//    	// swagger 항목 예외(열외) 추가 : 
//    	// 참고) /v2/api-docs : swagger의 전체적인 환경설정 정보를 JSON 형식으로 보여주는 페이지
//    	// /v2/api-docs, /swagger-resources/**, /swagger/**, swagger-ui.html
//    	// axios 항목 예외 추가
//    	return (web) -> web.ignoring().requestMatchers("/css/**", "/webjars/**", 
//    				"/images/**", "/js/**", "/v2/api-docs", "/swagger-resources/**", "/swagger/**", "/swagger-ui.html",
//    				"/axios/**", "/bootstrap-icons/**", "/bootstrap/**",
//    				"/summernote/**");    	
//    }

    @Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	
    	http.csrf((csrfCustomizer)->csrfCustomizer.disable()); // csrf 토큰 미사용
    	
        //http.userDetailsService(userDetailsService);
            
        //http.authenticationProvider(customProvider);
            
//        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions
//									   .sameOrigin()
//									)
//								);
    	
    	// 네아로
    	http.headers(headersCustomizer -> headersCustomizer.frameOptions(Customizer.withDefaults()).disable());
    	
					        
        http.authorizeHttpRequests((authorizeHttpRequests) ->  
										authorizeHttpRequests
										// axios 추가
						                // security 적용 예외 URL 등록와의 중복 부분 제외 => "/"만 적용
						                // .requestMatchers("/", "/css/**", "/webjars/**", "/images/**", "/js/**", "/axios/**", "/bootstrap-icons/**").permitAll()
										
												/* .requestMatchers("/**").permitAll() */
						                .requestMatchers("/**").permitAll()
						                //.requestMatchers("/").permitAll()
						                //.requestMatchers("/signin/").permitAll()
						                //.requestMatchers("/**").permitAll()
						                //.requestMatchers("/Camp/detailpage/").authenticated()
//						                .requestMatchers("/findId").permitAll()
//						                .requestMatchers("/ranking/").permitAll()
//						                .requestMatchers("/member/login.do").permitAll()
//						                .requestMatchers("/swagger-resources/**", "/swagger/**", "/swagger-ui.html").permitAll()
//						                .requestMatchers("/member/hasFld/**").permitAll()
//						                .requestMatchers("/member/view.do", "/member/hasFldForUpdate/**").authenticated()
//						                .requestMatchers("/member/update.do", "/member/updateProc.do").authenticated()
//						                .requestMatchers("/member/updateSess.do", "/member/updateSessProc.do").authenticated()
//						                .requestMatchers("/member/join", "/member/joinproc", "/member/joinProcRest", "/member/nickCount", "/member/idCount").permitAll()
//						                .requestMatchers("/member/updateRoles/**", "/member/changeMemberState/**", 
//						                			     "/member/updateMemberByAdmin/**", "/member/deleteMemberByAdmin/**").authenticated()
//						                .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
//						                .requestMatchers("/content1", "/content2").permitAll()	
//						                // 레시피 관련 링크 추가
//						                .requestMatchers("/recipe/gallerylist.do").permitAll()
//						                .requestMatchers("/recipe/detailpage/**").permitAll()
//						                .requestMatchers("/recipe/searchList.do/**").permitAll()
//						                // 게시판 관련 링크 추가
//						                .requestMatchers("/board/list.do", "/board/listProc.do",
//						                				 "/inq/list.do", "/inq/listProc.do",
//						                				 "/ntc/list.do", "/ntc/listProc.do").permitAll()
//						                .requestMatchers("/board/write.do","/board/writeProc.do",
//						                				 "/board/image", "/board/image/**",  
//							                			 "/board/view.do", "/board/searchList.do",
//							                			 "/board/update.do", "/board/updateProc.do",
//							                			 "/board/replyWrite.do", "/board/replyUpdate.do", 
//							                			 "/board/getRepliesAll.do", "/board/replyDelete.do",
//							                			 "/board/deleteProc.do").authenticated()
						                .anyRequest().authenticated());
        
        http.oauth2Login(oauth2LoginCustomizer ->
        	oauth2LoginCustomizer
        	.defaultSuccessUrl("/afternealo")
    			.userInfoEndpoint(userInfoEndpointCustomizer ->
	    							userInfoEndpointCustomizer
	    							.userService(customOAuth2UserService)));
        
                
//           http.formLogin(formLogin -> formLogin
//				                .loginPage("/member/login")
//				                .usernameParameter("username")
//				    			.passwordParameter("password")
//				    			.defaultSuccessUrl("/")                
//				                .failureUrl("/loginError")
//				                //.successHandler(new CustomAuthenticationSuccess()) // 로그인 성공 핸들러 
//				                //.failureHandler(new CustomAuthenticationFailure()) // 로그인 실패 핸들러 
//		                		.permitAll());
          /** 로그아웃 커스터마이징 */ 		
          http.logout((logout) -> logout.logoutUrl("/logout")
        		  						.logoutSuccessUrl("/")
        		  						.permitAll());
//          http.logout((logout) -> logout.permitAll());
           
           
          http.exceptionHandling(handler -> handler.accessDeniedPage("/403")); 	 
//            .and()
//                .logout()
//                    .logoutSuccessUrl("/")
                    
//            .and()
//                .oauth2Login()
//                	.defaultSuccessUrl(null)
//                    	.userInfoEndpoint()
//                        	.userService(customOAuth2UserService);
          
          
//          http.rememberMe((remember) -> remember
//					.key("mbc")
//					.userDetailsService(userDetailsService)
//					.tokenRepository(getJDBCRepository())
//					.tokenValiditySeconds(60 * 60 * 24)); // 24시간(1일)
    	
    	return http.build();
    } //
    

//	// 추가된 remember-me 관련 메서드
//	private PersistentTokenRepository getJDBCRepository() {
//
//		JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
//		repo.setDataSource(dataSource);
//
//		return repo;
//	} //
	
}