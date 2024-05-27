package com.mbc.pmac.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.registration.ClientRegistration.ProviderDetails;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.mbc.pmac.domain.CampFilteredVO;
import com.mbc.pmac.domain.CampImageVO;
import com.mbc.pmac.domain.CampReviewDTO;
import com.mbc.pmac.domain.CampReviewVO;
import com.mbc.pmac.domain.SessionUser;
import com.mbc.pmac.domain.User;
import com.mbc.pmac.repository.CampImageRepository;
import com.mbc.pmac.repository.CampReviewRepository;
import com.mbc.pmac.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class WelcomeController {

	@Autowired
	UserRepository userRepo;

	// 0526 재욱
	@Autowired
	CampImageRepository campImageRepo;

	@Autowired
	CampReviewRepository campReviewRepo;

	// 웹사이트 접속시 최초로 보이는 화면
	@GetMapping("/")
	public String home(Model model) {

//		User notSessionUser = new User();
//		
//		if (userRepo.findByUserEmailAndProvider(user.getEmail(), user.getProvider()) != null) {
//			
//			notSessionUser = userRepo.findByUserEmailAndProvider(user.getEmail(), user.getProvider());
//		}
//		
//		model.addAttribute("user", notSessionUser);

		// 0526 재욱
		// 인기 캠핑장 부분
		///////////////////////////////////////////////////////////////////////////////////////////////

		// 필요한거: 캠프이미지테이블, 캠프리뷰테이블, 둘을 합쳐서 넘길 DTO
		// List<String> popularNameList = new ArrayList<>();
		List<String> popularList = campReviewRepo.sortCampReviewByAvgRating().stream().limit(4).toList();
		List<CampImageVO> popularImageList = new ArrayList<>();
		List<CampReviewDTO> finalPopularList = new ArrayList<>();

//				for (int i = 0; i < popularList.size(); i++) {
		//
//					popularNameList.add(popularList.get(i).getCampName());
		//
//				}

		for (String str : popularList) {
			log.info("★ 네이버 평점이 가장 높은 top4 캠핑장 이름: {}", str);
		}

		for (int i = 0; i < popularList.size(); i++) {

			popularImageList.add(campImageRepo.findByCampNameCustom(popularList.get(i)));

		}

		for (int x = 0; x < popularList.size(); x++) {

			finalPopularList.add(new CampReviewDTO(popularList.get(x), popularImageList.get(x).getCampImageMain()));

		}

		// model.addAttribute("popularList", popularList);
		// model.addAttribute("popularImageList", popularImageList);

		finalPopularList.forEach(System.out::println);

		model.addAttribute("finalPopularList", finalPopularList);
		// popularList.forEach(System.out::println);
		// popularImageList.forEach(System.out::println);

		return "index";
	}

	@GetMapping("/signin")
	public String signin() {

		return "login";
	}
	
	// 0527 재욱
	@GetMapping("/loginError")
	public String loginError(Model model, HttpSession session) {

		// Spring CustomProvider 인증(Auth) 에러 메시지 처리
		Object secuSess = session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");

		log.info("#### 인증 오류 메시지-1 : " + secuSess);
		log.info("#### 인증 오류 메시지-2 : " + secuSess.toString());

		model.addAttribute("error", "true");
		model.addAttribute("msg", secuSess);

		return "login";
	}
	

	@GetMapping("/oauth2/authorization/naver")
	public String welcomeNaver() {

		return "welcome";
	}

	@GetMapping("/oauth2/authorization/google")
	public String welcomeGoogle() {

		return "welcome";
	}

	// null 오류 테스트용
	@GetMapping("/statusLogout")
	public String statusLogout() {

		return "afterlogin";
	}

	// 네아로 로그인 성공시 이용되는 url
	@GetMapping("/afternealo")
	public String afternealo(@SessionAttribute("user") SessionUser user, Model model, HttpSession session) {

		// 0525 재욱
		SessionUser newUser = SessionUser.builder().name(user.getName()).email(user.getEmail())
				.provider(user.getProvider()).nickname(user.getNickname()).build();

		// CustomOAuth2UserService 이후 세션 재정의
		session.setAttribute("user", newUser);

//		User notSessionUser = new User();
//		
//		if (userRepo.findByUserEmailAndProvider(user.getEmail(), user.getProvider()) != null) {
//			
//			notSessionUser = userRepo.findByUserEmailAndProvider(user.getEmail(), user.getProvider());
//		}
//		
		model.addAttribute("user", newUser);

		// 0526 재욱
		// 인기 캠핑장 부분
		///////////////////////////////////////////////////////////////////////////////////////////////

		// 필요한거: 캠프이미지테이블, 캠프리뷰테이블, 둘을 합쳐서 넘길 DTO
		// List<String> popularNameList = new ArrayList<>();
		List<String> popularList = campReviewRepo.sortCampReviewByAvgRating().stream().limit(4).toList();
		List<CampImageVO> popularImageList = new ArrayList<>();
		List<CampReviewDTO> finalPopularList = new ArrayList<>();

//		for (int i = 0; i < popularList.size(); i++) {
//
//			popularNameList.add(popularList.get(i).getCampName());
//
//		}

		for (String str : popularList) {
			log.info("★ 네이버 평점이 가장 높은 top4 캠핑장 이름: {}", str);
		}

		for (int i = 0; i < popularList.size(); i++) {

			popularImageList.add(campImageRepo.findByCampNameCustom(popularList.get(i)));

		}

		for (int x = 0; x < popularList.size(); x++) {

			finalPopularList.add(new CampReviewDTO(popularList.get(x), popularImageList.get(x).getCampImageMain()));

		}

		// model.addAttribute("popularList", popularList);
		// model.addAttribute("popularImageList", popularImageList);

		finalPopularList.forEach(System.out::println);

		model.addAttribute("finalPopularList", finalPopularList);
		// popularList.forEach(System.out::println);
		// popularImageList.forEach(System.out::println);

		return "index";
	}// afternealo()

	@PostMapping("/addNickname.do")
	public String addNicknameDo(@RequestParam("nickname") String nickname, Model model) {

		log.info("★★★ 신규 유저가 저장 요청한 닉네임:: {}", nickname);

		model.addAttribute("nickname", nickname);

		return "index";
	}// addNicknameDo()

}
