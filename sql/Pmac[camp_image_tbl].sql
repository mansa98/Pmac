-- 캠프 이미지 테이블 생성
CREATE TABLE camp_image_tbl(
camp_image_num NUMBER(5),
camp_name VARCHAR2(100 CHAR),
camp_image_main VARCHAR2(300 CHAR),
camp_image_sub VARCHAR2(1000 CHAR)
)

-- 캠프 이미지 테이블 제약조건 추가
ALTER TABLE camp_image_tbl
ADD CONSTRAINT cit_camp_image_num_pk PRIMARY KEY(camp_image_num);

--ALTER TABLE camp_image_tbl
--ADD CONSTRAINT cit_camp_name_fk FOREIGN KEY(camp_name)
--REFERENCES camp_info_tbl(camp_name) ON DELETE CASCADE;

-- 캠프 이미지 테이블 코멘트 생성
COMMENT ON COLUMN camp_image_tbl.camp_image_num IS '캠핑장 이미지 번호(시퀀스)';

COMMENT ON COLUMN camp_image_tbl.camp_name IS '캠핑장 이름(외래키)';

COMMENT ON COLUMN camp_image_tbl.camp_image_main IS '캠핑장 이미지 메인';

COMMENT ON COLUMN camp_image_tbl.camp_image_sub IS '캠핑장 이미지 서브';

-- 캠프 이미지 테이블 시퀀스 생성
CREATE SEQUENCE camp_image_tbl_seq
    INCREMENT BY 1
    START WITH 1
    MINVALUE 1
    MAXVALUE 9999
    NOCYCLE;