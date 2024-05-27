package com.mbc.pmac.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.mbc.pmac.domain.SessionUser;
import com.mbc.pmac.domain.User;
import com.mbc.pmac.repository.UserRepository;
import com.mbc.pmac.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class WelcomeRestController {
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	UserService userService;
	/**
	 * 유저가 OAtuh로 로그인을 성공한 직후 해당 유저가 nickname을 생성했는지 여부를 체크하는 곳
	 * 
	 * @param someMessage
	 * @param user
	 * @return ResponseEntity<String>
	 */
	@PostMapping("/hasNickname")
	public ResponseEntity<String> hasNickname(@RequestBody String someMessage, @SessionAttribute(name="user") SessionUser user) {
		
		log.info("＠＠＠ 인자 확인 부분");
		
		log.info("＠＠＠ 넘어왔는가? {}", someMessage);

		ResponseEntity<String> responseEntity = null;
		
		String result = someMessage;
		
		String provider = user.getProvider();
		
		// nickname이 있나/없나
		boolean hasNick = true;

		String msg = "";

		try {
			
			
			//TODO: DB 체크
			
			if (userRepo.findByUserEmailAndProvider(user.getEmail(), provider) != null) {
				
				User currentUser = userRepo.findByUserEmailAndProvider(user.getEmail(), provider);
				// 현재 로그인한 유저 정보의 nickname이 null이라면
				if (currentUser.getNickname() == null) {
					
					hasNick = false;
				}
				
			}// if

			log.info("--- hasNickname result : {}", result);
			
			// axios 통신에 성공하고, 닉네임이 존재하지 않는다면
			if (result.equals(someMessage) && (hasNick == false)) {
				msg = "우리 사이트 고유 닉네임이 없는 유저";

				responseEntity = new ResponseEntity<>(msg, HttpStatus.OK);

			// axios 통신에 성공하고, 닉네임이 이미 존재한다면
			} else if (result.equals(someMessage) && (hasNick == true)) {
				msg = "이미 우리 사이트 닉네임을 만든 유저";

				responseEntity = new ResponseEntity<>(msg, HttpStatus.OK);

			}

			log.info("전송되는 메시지: {}", msg);

		} catch (Exception e) {
			log.error("hasNickname Error : {}", e);
			e.printStackTrace();

			String errMsg = "에러 났어 슈발!";

			// 실패 코드(417) : 내부 서버 에러
			responseEntity = new ResponseEntity<>(errMsg, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return responseEntity;
	}// hasNickname()
	
	/**
	 * OAuth로 로그인한 유저가 우리 사이트 닉네임이 없을때 모달창을 띄워 input에 원하는 닉네임을 입력하게 하고
	 * submit 받아서 유저 정보에 nickname을 업데이트(추가) 처리하는 곳
	 * 
	 * 
	 * @param user
	 * @param nickname
	 * @return ResponseEntity<String>
	 */
	@PostMapping("/addNickname")
	public ResponseEntity<String> addNickname(@SessionAttribute("user") SessionUser user, @RequestParam("nickname") String nickname) {
		
		log.info("＠＠＠ addNickname 인자 확인 부분");
		
		log.info("＠＠＠ addNickname 넘어왔는가? {}", nickname);

		ResponseEntity<String> responseEntity = null;
		
		// 통신 확인용
		String result = nickname;
		
		String nick = nickname;
		// 세션에서 획득
		String email = user.getEmail();
		// 세션에서 획득
		String provider = user.getProvider();
		
		log.info("＠＠＠ session에서 email잘 가져왔나? :: {}", email);
		log.info("＠＠＠ session에서 provider잘 가져왔나? :: {}", provider);
		
		// nickname 업데이트 성공 여부 확인용
		boolean updateResult = false;

		String msg = "";

		try {
			
			// TODO: DB 체크
			
			// 닉네임 업데이트에 성공하면 updateResult = true;
			updateResult = userService.updateNickname(nick, email, provider);

			log.info("--- addNickname result : {}", updateResult);
			
			// axios 통신에 성공하고, 닉네임 업데이트에 성공했을때
			if (result.equals(nickname) && (updateResult == true)) {
				// 유저가 submit한 닉네임을 바로 ResponseEntity에 태워서 보내버림(alert 출력용)
				msg = nickname;

				responseEntity = new ResponseEntity<>(msg, HttpStatus.OK);

			// axios 통신에 성공하고, 닉네임 업데이트에 실패했을때
			} else if (result.equals(nickname) && (updateResult == false)) {
				msg = "통신에는 성공했으나, 닉네임 업데이트 실패!!";

				responseEntity = new ResponseEntity<>(msg, HttpStatus.OK);

			}

			log.info("전송되는 메시지: {}", msg);

		} catch (Exception e) {
			log.error("addNickname Error : {}", e);
			e.printStackTrace();

			String errMsg = "분명 RestController에 접근은 했지만, addNickname 에러 났어 슈발!";

			// 실패 코드(417) : 내부 서버 에러
			responseEntity = new ResponseEntity<>(errMsg, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return responseEntity;
	}// addNickname()

	
	
	

}
