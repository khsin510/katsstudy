package com.javaMavenTest.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.javaMavenTest.common.TestController;

@Controller
public class HelloController {
	
	
	
	@Autowired
	HelloDao dao;
	@Autowired
	TestController testCon;
	
	@RequestMapping(value = "/hello")
	public String hello() {
		//return "/WEB-INF/views/hello.jsp";
		String daoValue = dao.helloGet(); 
		System.out.println("helloDao value : " + daoValue);
		System.out.println("TestCon : " + testCon.test());
		return "views/hello";
	}
	
	
	
}