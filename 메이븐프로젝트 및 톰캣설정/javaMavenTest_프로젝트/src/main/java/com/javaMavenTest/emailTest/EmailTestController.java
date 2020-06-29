package com.javaMavenTest.emailTest;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class EmailTestController {
	
	@RequestMapping(value = "/mailSender") public void mailSender(HttpServletRequest request, ModelMap mo) throws AddressException, MessagingException {

		// gmail로 보내기전에 계정 설정 필수 
		// 1. 해당 gmail계정 smtp 사용 허용 설정
		// 2. 해당 계정 접근권한 보안 수준이 낮은 앱의 액세스 (허용) 필요
		String host = "smtp.gmail.com"; // 구글의 smtp 서버 이용
		final String username = "k";  // gmail 계정
		final String password = "k";  // 페스워드
		int port =465;
		//int port =587;
		
		String recipient = "mm"; //받는 사람의 메일주소를 입력해주세요. 
		String subject = "메일테스트"; //메일 제목 입력해주세요. 
		String body = username+"님으로 부터 메일을 받았습니다."; //메일 내용 입력해주세요. 
		Properties props = System.getProperties(); // 정보를 담기 위한 객체 생성 

		// SMTP 서버 정보 설정 
		props.put("mail.smtp.host", host); 
		props.put("mail.smtp.port", port); 
		props.put("mail.smtp.auth", "true"); 
		//props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.enable", "true"); 
	    props.put("mail.smtp.ssl.trust", host); 

		//Session 생성 
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			String un=username; String pw=password; 
			protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
				return new javax.mail.PasswordAuthentication(un, pw); 
				} 
			}); 
			session.setDebug(true); //for debug 
			
			Message mimeMessage = new MimeMessage(session); //MimeMessage 생성 
			mimeMessage.setFrom(new InternetAddress(username)); //발신자 셋팅 
			mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient)); //수신자셋팅 
			
			//.TO 외에 .CC(참조) .BCC(숨은참조) 도 있음 
			mimeMessage.setSubject(subject); //제목셋팅 
			mimeMessage.setText(body); //내용셋팅 
			Transport.send(mimeMessage); //javax.mail.Transport.send() 이용
	}

	
}