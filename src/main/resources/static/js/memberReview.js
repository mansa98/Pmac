/**
 * 
 */

// 리뷰 목록 읽어오기
function getAllReplies( originalCampId, userNickname) {
	
	axios.get(`/Camp/MemberReviewsAll.do?campId=${originalCampId}`) 
		 .then(function(response) {
			
			// alert("전체 리뷰 가져오기");
			
			let resData = response.data;
			console.log("response.data : ", resData);

			// 전체 리뷰 현황 리턴 확인
			console.log("전체 리뷰 수 : ", resData.length);
			
			let replyListPnl = document.getElementById("reply_list_pnl");
			let reviewData = "";
			
			for (let review of resData) {

				// 날짜 포매팅(형식화)
				//let replyFormattedBoardDate = reply.boardDate;
				
				/* ------------------------------------------------------------------------------------------------------ */
				
				// 개별 게시글 동적 패널
				reviewData = `<div id="reply_${review.memberReviewNum}" class="border-bottom border-dark-1 bg-light w-100 ps-4">
								
								<div class="d-flex flex-row py-2">
								
									<!-- 사람 아이콘 -->
									<div class="me-2">
									
										<i class="bi bi-person-circle" style="font-size:2.5em; color:#ccc"></i>	
									</div>
									<!--// 사람 아이콘 -->
									
									<!-- 작성자 : bootstrap badge(뱃지) 적용 -->
									<!-- 참고 : https://getbootstrap.com/docs/5.3/components/badge/#positioned -->
									
									<!-- 실제 리뷰 작성자 파악을 위해 id 등록 -->
									<div id="reply_actual_writer_${review.memberReviewNum}" class="d-flex align-items-center ms-1 mt-1">
									
										<!-- 실제 리뷰 작성자 파악을 위해 id 등록 -->	
										<button id="reply_writer_${review.userNickname}" type="button" class="btn position-relative" style="background-color:#eee">
											
											${review.userNickname}
							
										  	
										</button>
																				
									</div>
									<!--// 작성자 -->
									
								</div>
							
								<!-- 리뷰 내용 -->	
								<div class="my-1 d-flex flow">
								
									<!-- 리뷰 제어의 원활하게 하기 위해 div에 ID 할당 -->
									<div id="boardContent_${review.memberReviewNum}">
										${review.memberReviewContent}
									</div>
									
								</div>
								<!--// 리뷰 내용 -->
								
								<!-- ///////////////////////////////////////////////////////////////// -->
								
								<!-- 리뷰 작성일,  리뷰 수정/삭제 메뉴 : 2024.3(이관) -->
								<div class="my-1 d-flex flow row">
								
									<!-- 메뉴 추가(이동) : 리뷰 수정/삭제 : 2024.3 -->
									<div class="col-12 d-flex justify-content-end">
									
										<a href="#" id="reply_update_btn_${review.memberReviewNum}" class="btn btn-sm btn-outline-info me-2 mb-1" 
											   onClick="updateReply(${originalCampId}, ${review.memberReviewNum}, '${userNickname}')">수정</a>
								
											<a href="#" id="reply_delete_btn_${review.memberReviewNum}" class="btn btn-sm btn-outline-info mb-1 me-3"
											   onClick="deleteReply(${originalCampId}, ${review.memberReviewNum}, '${userNickname}')">삭제</a>
						
									</div>
									<!--// 메뉴 추가(이동) : 리뷰 수정/삭제 : 2024.3 -->
									
								</div>
								<!--// 리뷰 작성일 -->
								
								<!-- ///////////////////////////////////////////////////////////////// -->
								
							</div>`;
							
				/* ------------------------------------------------------------------------------------------------------ */
				
				replyListPnl.innerHTML += reviewData;	
				
			} // for
			
		 })	 				 
		 .catch(function(err) {
			console.error("리뷰를 불러오는 중 서버 에러가 발견되었습니다.");
		 }); // axios			  
	
	// 개별글 로딩시 지금까지 집계된 리뷰들 읽어오기 '끝"
	
} //

/* **************************************************************************************************** */

// 리뷰 작성 : 인자 변경
function writeReply(originalCampId, userNickname) {
	
	//alert("리뷰 등록");
	
	console.log("originalCampId : ", originalCampId);
	
	// 개별 게시글 읽어올 때 바로 개별 게시글의 리뷰들 읽어옴(로딩)
												
	let replyWriteBtn = document.getElementById("reply_write_btn_" + originalCampId);
	
	console.log("replyWriteBtn : ", replyWriteBtn);
	
 	// for (replyWriteBtn of replyWriteBtns) {
 	
    // 참고) 간혹 아래와 같이 기존에 함수로 분리되기 전에는 없던 에러가 발생할 수 있습니다.(그러나 동작상에는 문제는 없음 !) 
    // 바로 진입할 경우는 아래와 같은 에러 console 창에서 출력될 수 있습니다. 
    // 
    // 예상 오류) Uncaught TypeError: Cannot set properties of null (setting 'onclick')
	
    // 그럼에도 불구하고 리뷰 전송 등 작동에는 문제가 없습니다만,
    // 괜히 성가신 오류 메시지를 제거하기 위해서는
    // replyWriteBtn.addEventListener('click', function(e) {
	// 와 같이 사용할 것을 권장드립니다.
   
	// 리뷰 작성 
	// replyWriteBtn.onclick = (e) =>  {
	replyWriteBtn.addEventListener('click', function(e) { // 함수 내부에는 이와 같은 표현 권장	
		
		let originalCampId = e.target.id;
		originalCampId = originalCampId.substring('reply_write_btn_'.length);
		
		console.log("리뷰의 원글 아이디 : ", originalCampId);
		
		let replyWriteForm = document.getElementById("reply_write_form");
		
		// 주의) 이 부분에서 매우 주의해야 되는 이유는  JS 파일을 분리하면서 '[[${#authentication.name}]]' thymeleaf 요소가
		// 인식되지 않을 수 있습니다. 이럴 경우는 함수의 인자로 처리하여 코드 결손 현상을  패치해야 됩니다.
		// 참고로 과거 JSP에서도 이러한 현상이 종종 있어서 같은 방식으로 인자 결손 현상을 패치하였습니다.
		
		// alert('[[${#authentication.name}]]');
		// 참고) 위와 같이 조치해보면 로그인된 아이디가 분리된 JS 파일에서는 인식되지 않는 것을 확인할 수 있습니다. 
		
		// 현재 로그인 한 회원
		// let userNickname = '[[${#authentication.name}]]';
		 			
		console.log("리뷰 작성자 : ", userNickname);
		
		// 리뷰 폼점검
		// 리뷰 한계량을 100자 이내로 한정합니다.
		console.log("리뷰 길이 : ", replyWriteForm.value.length);
		
		if (replyWriteForm.value.length > 100) {
 				
 				alert("리뷰은(는) 100자 이내로 작성하셔야 합니다.");
 				
 				// 리뷰을 100자로 잘라서 다시 리뷰값에 표시
 				replyWriteForm.value = replyWriteForm.value.substring(0, 100);
 				replyWriteForm.focus(); // 재입력 대기 				
 			
		} else { 
		 
			console.log("작성할 리뷰의 내용 : ", replyWriteForm.value);
			console.log("작성할 리뷰의 '원글' 아이디 : ", originalCampId);
			console.log("작성자 아이디 : ", userNickname);
			
			const reviewData={
				campIdKey: originalCampId,
				memberReviewContent: replyWriteForm.value,
				userNicknameKey: userNickname,
			};
			// 전송			  
			axios.post('/Camp/Review.do', reviewData)
			 .then(function(response) {
				let resData = response.data;
				console.log("response.data : ", resData);

				// 전체 리뷰 현황 리턴 확인
				console.log("전체 리뷰 수 : ", resData.length);
				
				let replyListPnl = document.getElementById("reply_list_pnl");
				let reviewData = "";
				replyListPnl.innerHTML = ""; // 리뷰 목록 초기화
				
				for (let review of resData) {
					
					// 날짜 포매팅(형식화)
					//let replyFormattedBoardDate = reply.boardDate;
				
					/* ------------------------------------------------------------------------------------------------------ */
								 
					// 개별 게시글 동적 패널
					reviewData = `<div id="reply_${review.memberReviewNum}" class="border-bottom border-dark-1 bg-light w-100 ps-4">
									
									<div class="d-flex flex-row py-2">
									
										<!-- 사람 아이콘 -->
										<div class="me-2">
										
											<i class="bi bi-person-circle" style="font-size:3em; color:#ccc"></i>
											
										</div>
										<!--// 사람 아이콘 -->
										
										<!-- 작성자 : bootstrap badge(뱃지) 적용 -->
										<!-- 참고 : https://getbootstrap.com/docs/5.3/components/badge/#positioned -->
										
										<!-- 실제 리뷰 작성자 파악을 위해 id 등록 -->
										<div id="reply_actual_writer_${review.memberReviewNum}" class="d-flex align-items-center ms-1 mt-1">
										
											<!-- 실제 리뷰 작성자 파악을 위해 id 등록 -->	
											<button id="reply_writer_${review.userNickname}" type="button" class="btn btn-outline-secondary position-relative">
												
												${review.userNickname}
												
											  	
											</button>
																					
										</div>
										<!--// 작성자 -->
										
									</div>
								
									<!-- 리뷰 내용 -->										
									<div class="my-1 d-flex flow">
									
										<!-- 리뷰 제어의 원활하게 하기 위해 div에 ID 할당 -->
										<div id="boardContent_${review.memberReviewNum}">
											${review.memberReviewContent}
										</div>
										
									</div>
									<!--// 리뷰 내용 -->
									
									<!-- ///////////////////////////////////////////////////////////////// -->
							
									<!-- 리뷰 작성일,  리뷰 수정/삭제 메뉴 : 2024.3(이관) -->
									<div class="my-1 d-flex flow row">
										
										<!-- 메뉴 추가(이동) : 리뷰 수정/삭제 : 2024.3 -->
										<div class="col-12 d-flex justify-content-end">
										
											<a href="#" id="reply_update_btn_${review.memberReviewNum}" class="btn btn-sm btn-outline-info me-2 mb-1" 
											   onClick="updateReply(${originalCampId}, ${review.memberReviewNum}, '${userNickname}')">수정</a>
								
											<a href="#" id="reply_delete_btn_${review.memberReviewNum}" class="btn btn-sm btn-outline-info mb-1 me-3"
											   onClick="deleteReply(${originalCampId}, ${review.memberReviewNum}, '${userNickname}')">삭제</a>
							
										</div>
										<!--// 메뉴 추가(이동) : 리뷰 수정/삭제 : 2024.3 -->
										
									</div>
									<!--// 리뷰 작성일 -->
									
									<!-- ///////////////////////////////////////////////////////////////// -->
									
								</div>`;
											
					/* ------------------------------------------------------------------------------------------------------ */
					
					
					replyListPnl.innerHTML += reviewData;	
					
				} // for
				
			 })	 				 
			 .catch(function(err) {
				console.error("리뷰 작성 중 서버 에러가 발견되었습니다.");
			 }); // axios			  
			
			//e.preventDefault();
		} // if (replyWriteForm.value.length > 100)
		
	// } // replyWriteBtn.onclick = (e) => {
		
	}); // replyWriteBtn.addEventListener('click', function(e) { ...		
	
 	// } // for

} //

/* **************************************************************************************************** */

// 리뷰 수정 : 2024.3 : 리뷰 수정 버튼의 리뷰 영역 내로 이관된 조치에 따른 후속 조치(수정)
function updateReply(originalCampId, memberReviewNum, userNickname) {
	
	console.log("원글 아이디, 리뷰 아이디, 글 작성자 : " + originalCampId + "," + memberReviewNum +"," + userNickname);
	
	// 수정할 기존 리뷰 내용
	let boardContent = document.getElementById("boardContent_" + memberReviewNum);
	
	console.log("boardContent : ", boardContent.innerText.trim());
	
	// 리뷰 수정을 위해 게시글 목록 해당 게시글 패널 하단에 입력 패널 생성 및 내용 삽입			
	let replyPnl = document.getElementById("reply_" + memberReviewNum);
	
	// 이전 상태 복원 대비 위해 이전 등록 모드 상태 보전 : "취소" 버튼 클릭시 이전 상태 복원
	let oldReplyPnl = replyPnl.innerHTML;
	
	
	//////////////////////////////////////////////////////////////////////////////////
	
	// 버그 패치 : 2024.3
	// 기존의 리뷰 수정 패널이 있다면 리뷰 수정 패널이 추가되지 않도록 조치
	
	// 리뷰 수정란 생성
	let replyUpdateForm = `<div id="reply_write_update_pnl_${memberReviewNum}" class="my-3">
		 
								<textarea id="reply_update_form_${memberReviewNum}" 
										  name="reply_update_form_${memberReviewNum}" 
										  class="form-control border border-info"
										  placeholder="리뷰을 100자이내로 작성하십시오">${boardContent.innerText.trim()}</textarea>
							   
						   </div>`;
	
	// 리뷰 수정 내용 전송 버튼 생성 : 수정용 패쓰워드 입력란 포함
	let replySubmitBtns = `<div id="reply_submit_btns_${memberReviewNum}" class="d-flex justify-content-end my-2">
	
							 <button type="button" 
						 	 	 id="reply_submit_btn_${memberReviewNum}"
						  		 class="btn btn-sm btn-info me-2">등록</button>
							
							 <button type="button" 
						 	 	 id="reply_reset_btn_${memberReviewNum}"
						  		 class="btn btn-sm btn-info me-2">취소</button>
						
						  </div>`;
	
	
	// 기존의 패널이 없을 경우에만 추가
	if (document.getElementById(`reply_write_update_pnl_${memberReviewNum}`) == null) {
		
		replyPnl.innerHTML += replyUpdateForm;
		replyPnl.innerHTML += replySubmitBtns;
	} //			
				
	
	//////////////////////////////////////////////////////////////////////////////////////
	
	// 리뷰 수정 (전송)등록 버튼을 클릭시
	let replySubmitBtn = document.getElementById("reply_submit_btn_" + memberReviewNum);
				
	replySubmitBtn.onclick = () => {
		
		alert("리뷰 수정 전송")
		
		let replyUpdateForm = document.getElementById(`reply_update_form_${memberReviewNum}`);
		
		// 실제 리뷰 작성자 : 아래의 작성자 패널의 예시에서 처럼 실제 작성자가 
		// selector(선택자)를 활용하여 해당 요소를 검색할 경우
		// "#reply_actual_writer_109 button"와 같이 id값으로  
		// 추출하면 실제 작성자의 값을 조회할 수 있습니다.
		
		// 주의) 실제 리뷰 작성자와 현재 리뷰 작성을 시도하는 회원의 아이디를 
		// 비교하여 다르면 작성하지 못하도록 차단해야 됩니다.
		//        
		/****** ex)
		
		<div id="reply_actual_writer_109" class="d-flex align-items-center ms-1 mt-1">
								
			<button id="reply_writer_springspring1234" type="button" class="btn btn-outline-secondary position-relative">
				
				springspring1234
				
			  	<span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-info">
			    	작성자
			    	<span class="visually-hidden">unread messages</span>
			  	</span>
			  	
			</button>
													
		</div>
		
		*******/
		
		let replyActualWriter = document.querySelector(`#reply_actual_writer_${memberReviewNum} button`).id;
		
		replyActualWriter = replyActualWriter.substring("reply_writer_".length); // 실제 작성자 아이디 추출				
		
		console.log("작성자 아이디 : ", userNickname);
		console.log("실제 작성자 아이디 : ", replyActualWriter);
		console.log("수정할 리뷰의 아이디 : ", memberReviewNum);
		console.log("수정할 리뷰의 '원글' 아이디 : ", originalCampId);
		
		// 리뷰 폼점검 : 비어 있는지 여부 점검				
		if (replyUpdateForm.value.trim() == "") {
			
			alert("리뷰 내용이 없습니다. 다시 입력하십시오.");
			replyUpdateForm.value = ""; // 값 초기화
			replyUpdateForm.focus(); // 입력 대기
			
		} else if (replyActualWriter != userNickname) { // 회원이 실제 리뷰 작성자가 아니라면 차단 !
			
		 	console.log("작성자 아이디 : ", userNickname);
			console.log("실제 작성자 아이디 : ", replyActualWriter);
			
			alert("실제 리뷰 작성자만 리뷰을 수정할 수 있습니다.");
			
			// 2024.3 : 추가) 원상 복구
			replyPnl.innerHTML = oldReplyPnl;
			
			// 리뷰 작성란도 원상 복구
			document.getElementById("reply_write_form").innerHTML = "";
			// document.getElementById(`board_pass_${campId}`).value = "";
			
		} else { // 전송
		
			// let boardUpdatePass = document.getElementById(`board_pass_${memberReviewNum}`);
			
			console.log("수정할 리뷰의 아이디 : ", memberReviewNum);
			console.log("수정할 리뷰의 '원글' 아이디 : ", originalCampId);
			console.log("작성자 아이디 : ", userNickname);
			// console.log("수정할 리뷰 패쓰워드 : ", boardUpdatePass.value);
			console.log("리뷰 내용 : ", replyUpdateForm.value.trim());
			
			alert("최종점검");
			
			/*if (boardUpdatePass.value.trim() == '') { 
				
				alert("리뷰 패쓰워드를 입력하십시오");
				boardUpdatePass.focus();
				
			} else {*/	
			
			// 전송	
			// 주의) 여기서 campId 리뷰 자체의 아이디입니다.		  
			axios.post('/Camp/memberReviewUpdate.do', 
				{
					memberReviewNum : memberReviewNum,
				    campId : originalCampId,
					memberReviewContent : replyUpdateForm.value,
					userNickname : userNickname
					//boardPass : boardUpdatePass.value,
					//boardReRef : campId,
				}
			)
			 .then(function(response) {
				
				let resData = response.data;
				console.log("response.data : ", resData);

				// 전체 리뷰 현황 리턴 확인
				console.log("전체 리뷰 수 : ", resData.length);
				
				if (response.data && response.data.length > 0) {
					// 데이터가 존재하고 배열의 길이가 0보다 큰 경우
					console.log("데이터가 존재한다!");
					console.log(response.data);
				} else {
					// 데이터가 존재하지 않는 경우
					console.log("데이터가 없어요!")
				}
								
				let replyListPnl = document.getElementById("reply_list_pnl");
				let reviewData = "";
				replyListPnl.innerHTML = ""; // 리뷰 목록 초기화
				
				for (let review of resData) {
					
					// 날짜 포매팅(형식화)
					//let replyFormattedBoardDate = reply.boardDate;
				
					///////////////////////////////////////////////////////////////////////////////////////////////////////
								 
					// 개별 게시글 동적 패널
					reviewData = `<div id="reply_${review.memberReviewNum}" class="border-bottom border-dark-1 bg-light w-100 ps-4">
									
									<div class="d-flex flex-row py-2">
									
										<!-- 사람 아이콘 -->
										<div class="me-2">
										
											<i class="bi bi-person-circle" style="font-size:3em; color:#ccc"></i>
											
										</div>
										<!--// 사람 아이콘 -->
										
										<!-- 작성자 : bootstrap badge(뱃지) 적용 -->
										<!-- 참고 : https://getbootstrap.com/docs/5.3/components/badge/#positioned -->
										<!-- 실제 리뷰 작성자 파악을 위해 id 등록 -->
										<div id="reply_actual_writer_${review.memberReviewNum}" class="d-flex align-items-center ms-1 mt-1">
										
											<!-- 실제 리뷰 작성자 파악을 위해 id 등록 -->	
											<button id="reply_writer_${review.userNickname}" type="button" class="btn btn-outline-secondary position-relative">
												
												${review.userNickname}
												
											  	
											</button>
																					
										</div>
										<!--// 작성자 -->
										
									</div>
								
									<!-- 리뷰 내용 -->										
									<div class="my-1 d-flex flow">
									
										<!-- 리뷰 제어의 원활하게 하기 위해 div에 ID 할당 -->
										<div id="boardContent_${review.memberReviewNum}">
											${review.memberReviewContent}
										</div>
										
									</div>
									<!--// 리뷰 내용 -->
									
									<!-- ///////////////////////////////////////////////////////////////// -->
							
									<!-- 리뷰 작성일,  리뷰 수정/삭제 메뉴 : 2024.3(이관) -->
									<div class="my-1 d-flex flow row">
									
										<!-- 메뉴 추가(이동) : 리뷰 수정/삭제 : 2024.3 -->
										<div class="col-12 d-flex justify-content-end">
										
											<a href="#" id="reply_update_btn_${review.memberReviewNum}" class="btn btn-sm btn-outline-info me-2 mb-1" 
											   onClick="updateReply(${originalCampId}, ${review.memberReviewNum}, '${userNickname}')">수정</a>
								
											<a href="#" id="reply_delete_btn_${review.memberReviewNum}" class="btn btn-sm btn-outline-info mb-1 me-3"
											   onClick="deleteReply(${originalCampId}, ${review.memberReviewNum}, '${userNickname}')">삭제</a>
							
										</div>
										<!--// 메뉴 추가(이동) : 리뷰 수정/삭제 : 2024.3 -->
										
									</div>
									<!--// 리뷰 작성일 -->
									
									<!-- ///////////////////////////////////////////////////////////////// -->
									
								</div>`;
											
					//////////////////////////////////////////////////////////////////////////////////////////////////
					
					
					replyListPnl.innerHTML += reviewData;	
					
				} // for
				
			 })	 				 
			 .catch(function(err) {
				console.error("리뷰 작성 중 서버 에러가 발견되었습니다.");
				
				// 에러 처리
				console.log(err.response.data);
				console.log(err.response.status);
				
				if (err.response.status == 401) {
					alert("게시글 패쓰워드가 일치하지 않습니다.");								
				}
			
			 }); // axios			  
		
			//} // if (boardUpdatePass.value.trim() == '') { ....
			
		} // if (replyUpdateForm.value.trim() == "") { ....
		
		
		// 리뷰 전송 => 리뷰 작성자 아이디/패쓰워드 비교
	
	} //
	
	// 리뷰 수정 (전송)취소 버튼을 클릭시
	let replyResetBtn = document.getElementById("reply_reset_btn_" + memberReviewNum);
	
	// 리뷰 수정 취소 버튼을 클릭시 
	replyResetBtn.onclick = () => {
		
		alert("리뷰 수정 취소");
						
		// 원상 복구 : 원래의 일반 리뷰 등록 모드로 변경				
		replyPnl.innerHTML = oldReplyPnl;
		
		// 리뷰 작성란도 원상 복구
		document.getElementById("reply_write_form").innerHTML = "";
		// document.getElementById(`board_pass_${campId}`).value = "";
	} //
			
}


/* **************************************************************************************************** */

// 리뷰 삭제 : 2024.3 : 리뷰 삭제 버튼의 리뷰 영역 내로 이관된 조치에 따른 후속 조치(수정)
function deleteReply(originalCampId, memberReviewNum, userNickname) {

	console.log("원글 아이디, 리뷰 아이디, 글 작성자 : " + originalCampId + "," + memberReviewNum +"," + userNickname);
	
	// 리뷰 삭제를 위해 게시글 목록 해당 게시글 패널 하단에 입력 패널 생성 및 내용 삽입			
	let replyPnl = document.getElementById("reply_" + memberReviewNum);
	
	// 이전 상태 복원 대비 위해 이전 등록 모드 상태 보전 : "취소" 버튼 클릭시 이전 상태 복원
	let oldReplyPnl = replyPnl.innerHTML;
	
	// 리뷰 삭제 및 전송 버튼 생성 : 삭제용 패쓰워드 입력란 포함
	let replySubmitBtns = `<div id="reply_submit_btns_${memberReviewNum}" class="d-flex justify-content-end my-2">
	
							 <button type="button" 
						 	 	 id="reply_submit_btn_${memberReviewNum}"
						  		 class="btn btn-sm btn-info me-2">삭제 전송</button>
							
							 <button type="button" 
						 	 	 id="reply_reset_btn_${memberReviewNum}"
						  		 class="btn btn-sm btn-info me-2">취소</button>
						
						  </div>`;
				
	// 기존의 패널이 없을 경우에만 추가
	if (document.getElementById(`reply_write_update_pnl_${memberReviewNum}`) == null) {		
		replyPnl.innerHTML += replySubmitBtns;
	} //			
	
	// 실제 리뷰 작성자 : 아래의 작성자 패널의 예시에서 처럼 실제 작성자가 
	// selector(선택자)를 활용하여 해당 요소를 검색할 경우
	// "#reply_actual_writer_109 button"와 같이 id값으로  
	// 추출하면 실제 작성자의 값을 조회할 수 있습니다.
	
	// 주의) 실제 리뷰 작성자와 현재 리뷰 작성을 시도하는 회원의 아이디를 
	// 비교하여 다르면 작성하지 못하도록 차단해야 됩니다.
	//        
	/****** ex)
	
	<div id="reply_actual_writer_109" class="d-flex align-items-center ms-1 mt-1">
							
		<button id="reply_writer_springspring1234" type="button" class="btn btn-outline-secondary position-relative">
			
			springspring1234
			
		  	<span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-info">
		    	작성자
		    	<span class="visually-hidden">unread messages</span>
		  	</span>
		  	
		</button>
												
	</div>
	
	*******/
	
	let replyActualWriter = document.querySelector(`#reply_actual_writer_${memberReviewNum} button`).id;
	
	replyActualWriter = replyActualWriter.substring("reply_writer_".length); // 실제 작성자 아이디 추출			
	
	//////////////////////////////////////////////////////////////////////////////////////
	
	// 리뷰 삭제 (전송)등록 버튼을 클릭시
	let replySubmitBtn = document.getElementById("reply_submit_btn_" + memberReviewNum);
				
		console.log("원 게시글 작성자 : "+ replyActualWriter);
		console.log("현재 게시글 작성 요청자 : "+ userNickname);
	
	replySubmitBtn.onclick = () => {				
	
		// 리뷰 폼점검 : 비어 있는지 여부 점검
		// 유의) 여기서는 "리뷰 삭제" 버튼을 직접 이용하기 때문에 원글(campId) 버튼을 활용 
		// let boardDeletePass = document.getElementById(`board_pass_${memberReviewNum}`);
		
		alert("원 게시글 작성자 : "+ replyActualWriter);
		alert("현재 게시글 작성 요청자 : "+ userNickname);
		
		if (replyActualWriter != userNickname) { // 회원이 실제 리뷰 작성자가 아니라면 차단 !
			
		 	console.log("작성자 아이디 : ", userNickname);
			console.log("실제 작성자 아이디 : ", replyActualWriter);
			
			alert("실제 리뷰 작성자만 리뷰을 삭제할 수 있습니다.");
			
			// 2024.3 : 추가) 원상 복구
			replyPnl.innerHTML = oldReplyPnl;
			
		} else { // 회원이 실제 리뷰 작성자라면...
		
				
			alert("삭제 전송");
		
			console.log("-- memberReviewNum : ", memberReviewNum);			
			console.log("-- originalCampId : ", originalCampId);
			console.log("**-- userNickname : ", userNickname);
			console.log("**-- replyActualWriter : ", replyActualWriter);
			//console.log("-- boardDeletePass.value : ", boardDeletePass.value);
			
			// 삭제를 위한 AJAX 전송
			// 삭제할 리뷰 아이디와 리뷰 부모글(원글) 아이디 : 원글은 삭제 후 리뷰 목록의 현황 갱신을 위한 전송 
			axios.post(`/Camp/memberReviewDelete.do`,
			{
				memberReviewNum : memberReviewNum, // 삭제할 리뷰 아이디
				originalCampId : originalCampId, // 리뷰 목록 갱신을 위한 원글 아이디
			}) 
			 .then(function(response) {
				let resData = response.data;
				console.log("response.data : ", resData);
	
				// 전체 리뷰 현황 리턴 확인
				console.log("전체 리뷰 수 : ", resData.length);
				
				let replyListPnl = document.getElementById("reply_list_pnl");
				
				// 기존 패널 비우기(초기화)
				replyListPnl.innerHTML = "";
				
				let reviewData = "";
				
				for (let review of resData) {
	
					// 날짜 포매팅(형식화)
					//let replyFormattedBoardDate = reply.boardDate;
					
					
					
					// 개별 게시글 동적 패널
					reviewData = `<div id="reply_${review.memberReviewNum}" class="border-bottom border-dark-1 bg-light w-100 ps-4">
									
									<div class="d-flex flex-row py-2">
									
										<!-- 사람 아이콘 -->
										<div class="me-2">
										
											<i class="bi bi-person-circle" style="font-size:3em; color:#ccc"></i>
											
										</div>
										<!--// 사람 아이콘 -->
										
										<!-- 작성자 : bootstrap badge(뱃지) 적용 -->
										<!-- 참고 : https://getbootstrap.com/docs/5.3/components/badge/#positioned -->
										
										<!-- 실제 리뷰 작성자 파악을 위해 id 등록 -->
										<div id="reply_actual_writer_${review.memberReviewNum}" class="d-flex align-items-center ms-1 mt-1">
										
											<!-- 실제 리뷰 작성자 파악을 위해 id 등록 -->	
											<button id="reply_writer_${review.userNickname}" type="button" class="btn btn-secondary position-relative">
												
												${review.userNickname}
								
											  	
											</button>
																					
										</div>
										<!--// 작성자 -->
										
									</div>
								
									<!-- 리뷰 내용 -->	
									<div class="my-1 d-flex flow">
									
										<div style="width:25px;">
											&nbsp;
										</div>
										
										<!-- 리뷰 제어의 원활하게 하기 위해 div에 ID 할당 -->
										<div id="boardContent_${review.memberReviewNum}">
											${review.memberReviewContent}
										</div>
										
									</div>
									<!--// 리뷰 내용 -->
									
									<!-- ///////////////////////////////////////////////////////////////// -->
							
									<!-- 리뷰 작성일,  리뷰 수정/삭제 메뉴 : 2024.3(이관) -->
									<div class="my-1 d-flex flow row">
									
										<!-- 메뉴 추가(이동) : 리뷰 수정/삭제 : 2024.3 -->
										<div class="col-12 d-flex justify-content-end">
										
											<a href="#" id="reply_update_btn_${review.memberReviewNum}" class="btn btn-sm btn-outline-info me-2 mb-1" 
											   onClick="updateReply(${originalCampId}, ${review.memberReviewNum}, '${userNickname}')">수정</a>
								
											<a href="#" id="reply_delete_btn_${review.memberReviewNum}" class="btn btn-sm btn-outline-info mb-1 me-3"
											   onClick="deleteReply(${originalCampId}, ${review.memberReviewNum}, '${userNickname}')">삭제</a>
							
										</div>
										<!--// 메뉴 추가(이동) : 리뷰 수정/삭제 : 2024.3 -->
										
									</div>
									<!--// 리뷰 작성일 -->
									
									<!-- ///////////////////////////////////////////////////////////////// -->
									
								</div>`;
								
					
					replyListPnl.innerHTML += reviewData;	
					
				} // for
			
			 })	 				 
			 .catch(function(err) {
				console.error("리뷰 삭제 중 서버 에러가 발견되었습니다.");
				
				// 에러 처리
				console.log(err.response.data);
				console.log(err.response.status);
				
				if (err.response.status == 401) {
					alert("게시글 패쓰워드가 일치하지 않습니다.");								
				}
			 }); // axios			  
				
			
			// } // if (boardUpdatePass.value.trim() == '') { 	 
				
		} //
	
	} // 회원이 실제 리뷰 작성자라면...	
	
	// 리뷰 삭제 (전송)취소 버튼을 클릭시
	let replyResetBtn = document.getElementById("reply_reset_btn_" + memberReviewNum);
	
	// 리뷰 수정 취소 버튼을 클릭시 
	replyResetBtn.onclick = () => {
		
		alert("리뷰 삭제 취소");
						
		// 원상 복구 : 원래의 일반 리뷰 등록 모드로 변경				
		replyPnl.innerHTML = oldReplyPnl;
		
		// 리뷰 작성란도 원상 복구
		document.getElementById("reply_write_form").innerHTML = "";
		// document.getElementById(`board_pass_${campId}`).value = "";
	} //			
	
} //

/* **************************************************************************************************** */


/*
// (원)글 삭제
// 0424 : 삭제부분 취소누르면 삭제안되게 수정(유재승)
function deleteBoard(boardNum) {
	
	// console.log("boardNum : ", boardNum, ", userNickname : ", userNickname);
	// console.log("삭제할 게시글(원글) 아이디 : ",boardNum);
	
	// 게시글 삭제 버튼
	let boardDeleteBtn = document.getElementById(`board_delete_btn_${boardNum}`);
	
	// 삭제할 게시글 패쓰워드
	// let boardDeletePass = document.getElementById(`board_pass_${boardNum}`);
	
	boardDeleteBtn.onclick = function() {
		
		console.log("삭제할 게시글 아이디 : ", boardNum);
		
		// 게시글 삭제 의사 재점검
		if (confirm("정말 삭제하시겠습니까?") == true) {
		
			// 전송
			// 삭제를 위한 AJAX 전송
			// 삭제할 게시글 아이디
			
			//////////////////////////////////////////////////////////////////////////
			// 
			// 주의사항) 특수문자(#)이 포함된 boardPass 인자 전송할 경우 => encodeURIComponent 활용 !
			//  
			
			let str = `/campDetailPage/deleteProc.do/${boardNum}`;
			
			console.log('str : ', str);
			
			location.href = str;
				
			//} // if
		
		} else {
			
			alert("게시글 삭제를 취소하였습니다.");
			
		} // if (confirm("정말 삭제하시겠습니까?") == true	
		
	} // boardDeleteBtn.onclick

} //*/