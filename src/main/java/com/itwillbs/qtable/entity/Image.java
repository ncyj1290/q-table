package com.itwillbs.qtable.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name= "image")
@Getter
@Setter
public class Image {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name= "image_idx")
	private Integer imageIdx;
	
	@Column(name= "target_type", nullable = false)
	private String targetType;
	
	@Column(name= "target_idx", nullable = false)
	private Integer targetIdx;
	
	@Column(name= "image_url", nullable = false)
	private String imageUrl;
	
	@Column(name= "is_main_image", nullable = false)
	private Boolean isMainImage = false;
		
}
