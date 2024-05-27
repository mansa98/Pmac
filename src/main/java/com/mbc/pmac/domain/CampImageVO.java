package com.mbc.pmac.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="camp_image_tbl")
@SequenceGenerator(
	    name = "CAMP_IMAGE_TBL_SEQ_GENERATOR",
	    sequenceName = "CAMP_IMAGE_TBL_SEQ",
	    initialValue = 1,
	    allocationSize = 1)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CampImageVO {
	
	@Id
	@Column(name="camp_image_num")
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
					generator = "CAMP_IMAGE_TBL_SEQ_GENERATOR")
	private int campImageNum;
	
	@Column(name="camp_name")
	private String campName;
	
	@Column(name="camp_image_main")
	private String campImageMain;
	
	@Column(name="camp_image_sub")
	private String campImageSub;
}
