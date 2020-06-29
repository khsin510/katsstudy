package com.javaMavenTest.hello;

import org.springframework.stereotype.Repository;



@Repository
public class HelloDao {
	
	public String helloGet(){
		
		return "helloDao";
	}
	
}