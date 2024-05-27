-- 멤버 테이블 생성
CREATE TABLE member_tbl(
member_num NUMBER(5),
member_name VARCHAR2(20 CHAR),
member_email VARCHAR2(20 CHAR),
member_nickname VARCHAR2(20 CHAR),
member_provider VARCHAR2(10 CHAR)
)

-- 멤버 테이블 제약조건 추가
ALTER TABLE member_tbl
ADD CONSTRAINT mt_member_num_pk PRIMARY KEY(member_num);

ALTER TABLE member_tbl
ADD CONSTRAINT mt_member_nickname_uq UNIQUE(member_nickname);

ALTER TABLE member_tbl
ADD CONSTRAINT mt_member_email_uq UNIQUE(member_email);

ALTER TABLE member_tbl
MODIFY (member_nickname CONSTRAINT mt_member_nickname_nn NOT NULL);

-- 멤버 테이블 코멘트 추가
COMMENT ON COLUMN member_tbl.member_num IS '회원 번호(시퀀스)';

COMMENT ON COLUMN member_tbl.member_name IS '회원 실명';

COMMENT ON COLUMN member_tbl.member_email IS '회원 이메일(OAuth ID)';

COMMENT ON COLUMN member_tbl.member_nickname IS '회원 닉네임(우리 사이트 고유)';

COMMENT ON COLUMN member_tbl.member_provider IS 'OAuth 서비스 제공업체(구글, 네이버)';

-- member_tbl 시퀀스 생성
CREATE SEQUENCE member_tbl_seq
    INCREMENT BY 1
    START WITH 1
    MINVALUE 1
    MAXVALUE 99999
    NOCYCLE;
