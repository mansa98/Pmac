package com.mbc.pmac.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mbc.pmac.domain.MemberReviewVO;
import com.mbc.pmac.repository.MemberReviewDAO;


import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MemberReviewService {

	@Autowired
	MemberReviewDAO memberReviewDAO;
		
		/** 캠프 아이디 가져오기 */
		@Transactional(rollbackFor = Exception.class)
		public List<MemberReviewVO> selectReplyByCampId(int CampId){
			
			return memberReviewDAO.findByCampId(CampId);
		}
		
		/** 리뷰 작성 */ 
		@Transactional(rollbackFor = Exception.class)
		public MemberReviewVO insertReview(MemberReviewVO memberReviewVO) {
			
			return memberReviewDAO.save(memberReviewVO);
		}
		
		/** 리뷰 삭제 */ 
		@Transactional(rollbackFor = Exception.class)
		public boolean deleteByMemberReviewNum(int memberReviewNum) {
			
			boolean result = false;
			
			try {
				memberReviewDAO.deleteByMemberReviewNum(memberReviewNum);
				result = true;
			} catch (Exception e) {
				log.error("deleteByMemberReviewNum error : {}", e);
				result = false;
			}
			
			return result;
		}
		
		/** 리뷰 갯수 */ 
		@Transactional(readOnly = true)
		public int selectMembersCountWithReviews(int memberReviewNum) {
			
			return (int)memberReviewDAO.countByMemberReviewNum(memberReviewNum); // 댓글의 갯수 추출 : board_re_ref = boardNum
		} //
		
		/** 리뷰 현황 가져오기 */
		@Transactional(readOnly=true)
		public MemberReviewVO selectBymemberReviewNum(int memberReviewNum) {
			
			return memberReviewDAO.findByMemberReviewNum(memberReviewNum);
		}
		
//		@Transactional(readOnly=true)
//		public MemberReviewVO selectBycampId(int campId) {
//			
//			return memberReviewDAO.findByCampId2(campId);
//		}
		
		/** 댓글 수정 */ 
		@Transactional(rollbackFor = Exception.class)
		public MemberReviewVO updateReview(MemberReviewVO memberReviewVO) {
			
			return memberReviewDAO.save(memberReviewVO);
		}
		
}
