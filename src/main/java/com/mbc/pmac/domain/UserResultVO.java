package com.mbc.pmac.domain;
// 0525 재욱
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
// 캠핑장추천
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResultVO {

	/** 결과셋 아이디 */
	@Id
	@Column
	private int id;

	/** 사용자 아이디 */
	@Column(name="nickname")
	private String nickname;

	/** 행선예정지 ex) 강릉 */
	@Column(name="destination")
	private String destination;

	/** 캠핑장 아이디  */
	@Column(name="camp_id")
	private int campId;

	/** 캠핑장명  */
	@Column(name="camp_name")
	private String campName;

	/** 행선 시기 ex) 2024년 5월 10일 운영 가능 */
	private String enableOps;

	/** 전기 사용가 여부 */
	@Column(name="is_electricity")
	private int isElectricity;

	/** WIFI 인터넷 사용가 여부 */
	@Column(name="is_wifi")
	private int isWifi;

	/** 캠프파이어 가능 여부 */
	@Column(name="is_campfire")
	private int isCampfire;

	/** 온수 가능 여부 */
	@Column(name="is_heater")
	private int isHeater;

	/** 풀장 가능 여부 */
	@Column(name="is_pool")
	private int isPool;

	/** 놀이터 가능 여부 */
	@Column(name="is_playground")
	private int isPlayground;

	/** 취사 시설 구비 여부 */
	@Column(name="is_sink")
	private int isSink;

	/** 산책로 사용 가능 여부 */
	@Column(name="is_trail")
	private int isTrail;

	/** 수상 레저 가능 여부 */
	@Column(name="is_maritime_leisure")
	private int isMaritimeLeisure;

	/** 낚시 가능 여부 */
	@Column(name="is_fishing")
	private int isFishing;

	/** 전체 요구사항 반영율 */
	@Column(name="satisfaction")
	private int satisfaction;

	/** 캠프장 도로명 주소 */
//	@Column(name="road_address")
//	private String roadAddress;

	/** 캠프장 지번 주소 */
//	@Column(name="jibun_address")
//	private String jibunAddress;

	/** 네이버 별점 */
	@Column(name="avg_rating")
	private String avgRating;

	/** 네이버 리뷰(긍정지수) */
	@Column(name="review_positive")
	private String reviewPositive;

	/** 네이버 리뷰(부정지수) */
	@Column(name="review_negative")
	private String reviewNegative;
	
	// 사진 추가
	/** 캠프장 이미지 */
	@Column(name="camp_image_main")
	private String campImageMain;

}