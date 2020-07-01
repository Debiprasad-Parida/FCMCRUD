package com.sdrc.fcmdemo.model;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class Person {
	
	private String name;
	private String address;
	private Integer age;

	public Person(String name, String address, Integer age) {
		this.name=name;
		this.age=age;
		this.address=address;
	}

}
