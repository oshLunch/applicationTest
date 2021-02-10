package com.cos.test.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Book {
	@Id //pk를 해당 변수로 하겠다는 뜻
	@GeneratedValue(strategy = GenerationType.IDENTITY)//해당 데이터베이스 번호증가 전략을 따라가겠다.
	
	private Integer id;//null 값이 들어가도록 타입을 객체로 변환
	
	private String title;
	private double rating;	
	private double price;

}
