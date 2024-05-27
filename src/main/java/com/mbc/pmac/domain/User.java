package com.mbc.pmac.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//네아로
@Entity
@Table(name="user_tbl")
@Getter
@Setter // 추가
@ToString
@SequenceGenerator(
		name = "user_tbl_seq_generator",
		sequenceName = "user_tbl_seq",
		initialValue = 1,
		allocationSize = 1)
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseTimeEntity {
	
	@Id
	@Column(nullable=false, precision=10, scale=0)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,
					generator="user_tbl_seq_generator")
	private BigDecimal id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="email")
	private String email;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	private Role role;
	
	@Column(name="created_date")
	private LocalDateTime createdDate;
	
	@Column(name="modified_date")
	private LocalDateTime modifiedDate;
	
	@Column(name="provider")
	private String provider;
	
	// 닉네임 추가
	@Column(name="nickname")
	private String nickname;
	
	// 업데이트 로직 추가
	@Column(name="is_user")
	private int isUser;
	
	
	
	private static LocalDateTime getCurrentTime() {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		
		
		LocalDateTime currentDateTime = LocalDateTime.parse(currentTime, formatter);
		
		
		return currentDateTime;
	}// getCurrentTime()
	
	@Builder
    public User(String name, String email, Role role, String provider, String nickname, int isUser) {
        this.name = name;
        this.email = email;
        this.role = role;
//      this.createdDate = LocalDateTime.now();
        this.createdDate = User.getCurrentTime();
        this.provider = provider;
        this.nickname = nickname; // 닉네임 추가
        this.isUser = 1;
    }
	
	// 유저 정보 업데이트
	public User update(String name) {
        this.name = name;
        //구글 추가
        // this.provider = provider;
        this.modifiedDate = User.getCurrentTime();
        
        return this;
    }
	
	public String getRoleKey() {
        return this.role.getKey();
    }
	
}
