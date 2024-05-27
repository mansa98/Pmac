package com.mbc.pmac.domain;

import java.io.Serializable;
import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.extern.slf4j.Slf4j;

@Entity
@Table(name="member_review_tbl")
@Slf4j
public class MemberReviewVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/** 회원 리뷰 번호 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator = "MEMBER_REVIEW_TBL_SEQ_GENERATOR")
			@SequenceGenerator(
			name = "MEMBER_REVIEW_TBL_SEQ_GENERATOR",
			sequenceName= "member_review_tbl_seq",
			initialValue = 1,
			allocationSize = 1)
	@Column(name = "member_review_num")
	private int memberReviewNum;
	
	/** 캠핑장 번호 */
	@Column(name = "camp_id")
	private int campId;
	
	/** 회원 리뷰 내용 */
	@Column(name = "member_review_content")
	private String memberReviewContent;
	
	/** 회원 닉네임 */
	@Column(name = "user_nickname")
	private String userNickname;
	
	/** 회원 리뷰 이미지 */
	@Column(name = "member_review_image")
	private String memberReviewImage;
	
	/** 회원 리뷰 이미지 (원본) */
	@Column(name = "member_review_og_image")
	private String memberReviewOgImage;

	public int getMemberReviewNum() {
		return memberReviewNum;
	}

	public void setMemberReviewNum(int memberReviewNum) {
		this.memberReviewNum = memberReviewNum;
	}

	public int getCampId() {
		return campId;
	}

	public void setCampId(int campId) {
		this.campId = campId;
	}

	public String getUserNickname() {
		return userNickname;
	}

	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}

	public String getMemberReviewContent() {
		return memberReviewContent;
	}

	public void setMemberReviewContent(String memberReviewContent) {
		this.memberReviewContent = memberReviewContent;
	}

	public String getMemberReviewImage() {
		return memberReviewImage;
	}

	public void setMemberReviewImage(String memberReviewImage) {
		this.memberReviewImage = memberReviewImage;
	}

	public String getMemberReviewOgImage() {
		return memberReviewOgImage;
	}

	public void setMemberReviewOgImage(String memberReviewOgImage) {
		this.memberReviewOgImage = memberReviewOgImage;
	}
	
	/** 맵형식 */
	public MemberReviewVO(Map<String, Object> map) {
		log.info("ReplyVO 오버로딩 생성자 : Map to VO");
		
		this.memberReviewNum = Integer.parseInt((String) map.get("memberReviewNum").toString());
		this.campId = Integer.parseInt((String)map.get("campId").toString());
		this.userNickname = (String)map.get("userNickname");
		this.memberReviewContent = (String)map.get("memberReviewContent");
		this.memberReviewImage = (String)map.get("memberReviewImage");
		this.memberReviewOgImage = (String)map.get("memberReviewOgImage");
	}
	
	public MemberReviewVO() {
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MemberReviewVO [memberReviewNum=").append(memberReviewNum)
		.append(", campId=").append(campId)
		.append(", userNickname=").append(userNickname)
		.append(", memberReviewContent=").append(memberReviewContent)
		.append(", memberReviewImage=").append(memberReviewImage)
		.append(", memberReviewOgImage=").append(memberReviewOgImage)
		.append("]");
		return builder.toString();
	}
	
}
