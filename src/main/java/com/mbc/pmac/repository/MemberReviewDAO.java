package com.mbc.pmac.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.mbc.pmac.domain.MemberReviewVO;

public interface MemberReviewDAO extends PagingAndSortingRepository<MemberReviewVO, Integer> {
	
	/** 리뷰 저장 */
	MemberReviewVO save(MemberReviewVO memberReviewVO);
	
	/** 리뷰 번호를 이용해서 리뷰 가져오기 */
	MemberReviewVO findByMemberReviewNum(int memberReviewNum);
	
	/** 캠핑장 아이디로 리뷰 찾기 */
	List<MemberReviewVO> findByCampId(int campId);
	
	/** 멤버 리뷰 갯수 */
	long countByMemberReviewNum(int memberReviewNum);
	
	/** 리뷰 삭제 */
	void deleteByMemberReviewNum(int memberReviewNum);
	
}
