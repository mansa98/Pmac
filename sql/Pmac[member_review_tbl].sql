-- 멤버 리뷰 테이블 생성
CREATE TABLE member_review_tbl(
member_review_num NUMBER(5),
camp_id NUMBER(10),
-- 0523 바꿈 member_nickname VARCHAR2(20 CHAR),
user_nickname VARCHAR2(20 CHAR),
member_review_content VARCHAR2(500 CHAR),
member_review_image VARCHAR2(500 CHAR),
member_review_og_image VARCHAR2(500 CHAR)
);

-- 멤버 리뷰 테이블 제약조건 추가
ALTER TABLE member_review_tbl
ADD CONSTRAINT mrt_member_review_num_pk PRIMARY KEY(member_review_num);

--ALTER TABLE member_review_tbl
--ADD CONSTRAINT mrt_camp_id_fk FOREIGN KEY(camp_id) 
--REFERENCES camp_info_tbl(camp_id) ON DELETE CASCADE;

ALTER TABLE member_review_tbl
MODIFY (user_nickname CONSTRAINT mrt_user_nickname_nn NOT NULL);

ALTER TABLE member_review_tbl
MODIFY (member_review_content CONSTRAINT mrt_member_review_content_nn NOT NULL);

-- 멤버 리뷰 테이블 코멘트 생성
COMMENT ON COLUMN member_review_tbl.member_review_num IS '회원 리뷰 댓글 번호(시퀀스)';

COMMENT ON COLUMN member_review_tbl.camp_id IS '캠핑장 아이디(외래키)';

COMMENT ON COLUMN member_review_tbl.user_nickname IS '회원 닉네임';

COMMENT ON COLUMN member_review_tbl.member_review_content IS '회원 리뷰 본문 내용';

COMMENT ON COLUMN member_review_tbl.member_review_image IS '회원 리뷰 이미지(암호화)';

COMMENT ON COLUMN member_review_tbl.member_review_og_image IS '회원 리뷰 이미지(원본)';

-- member_review_tbl 시퀀스 생성
CREATE SEQUENCE member_review_tbl_seq
    INCREMENT BY 1
    START WITH 1
    MINVALUE 1
    MAXVALUE 99999
    NOCYCLE;
	
	
ALTER TABLE member_review_tbl 
ADD CONSTRAINT mrt_user_nickname_fk 
FOREIGN KEY(user_nickname) REFERENCES user_tbl(nickname);

ALTER TABLE user_tbl 
ADD CONSTRAINT ut_nickname_uq UNIQUE(nickname);