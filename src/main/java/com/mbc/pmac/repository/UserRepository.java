package com.mbc.pmac.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mbc.pmac.domain.User;

// 네아로
public interface UserRepository extends JpaRepository<User, Long> {

	
	Optional<User> findByEmail(String Email);
	
	// 기본 findByEamil이 return이 Optional이라 사용하기 좋지 않아서 하나 더 만듦
	@Query(value="SELECT * FROM user_tbl ut WHERE ut.email=:email", nativeQuery=true)
	User findByUserEmail(@Param("email") String email);
	
	// 로직 변경으로 인해 추가
	@Query(value="SELECT * FROM user_tbl ut WHERE ut.email=:email AND ut.provider=:provider", nativeQuery=true)
	User findByUserEmailAndProvider(@Param("email") String email, @Param("provider") String provider);
	
	
	@Modifying // update 쿼리문에는 필수인 어노테이션 + 업데이트 로직 변경으로 provider 추가
	@Query(value="UPDATE user_tbl ut SET ut.nickname=:nickname WHERE ut.email=:email AND ut.provider=:provider", nativeQuery=true)
	void addNickname(@Param("nickname") String nickname, @Param("email") String email, @Param("provider") String provider);
	
	@Query(value="SELECT * FROM user_tbl ut WHERE ut.email=:email AND ut.provider=:provider AND ut.is_user=1", nativeQuery=true)
	Optional<User> findByEmailAndProviderAndIsUser(@Param("email") String email, @Param("provider") String provider);
	
}
