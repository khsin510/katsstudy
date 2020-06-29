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

		// gmail�� ���������� ���� ���� �ʼ� 
		// 1. �ش� gmail���� smtp ��� ��� ����
		// 2. �ش� ���� ���ٱ��� ���� ������ ���� ���� �׼��� (���) �ʿ�
		String host = "smtp.gmail.com"; // ������ smtp ���� �̿�
		final String username = "k";  // gmail ����
		final String password = "k";  // �佺����
		int port =465;
		//int port =587;
		
		String recipient = "mm"; //�޴� ����� �����ּҸ� �Է����ּ���. 
		String subject = "�����׽�Ʈ"; //���� ���� �Է����ּ���. 
		String body = username+"������ ���� ������ �޾ҽ��ϴ�."; //���� ���� �Է����ּ���. 
		Properties props = System.getProperties(); // ������ ��� ���� ��ü ���� 

		// SMTP ���� ���� ���� 
		props.put("mail.smtp.host", host); 
		props.put("mail.smtp.port", port); 
		props.put("mail.smtp.auth", "true"); 
		//props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.enable", "true"); 
	    props.put("mail.smtp.ssl.trust", host); 

		//Session ���� 
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			String un=username; String pw=password; 
			protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
				return new javax.mail.PasswordAuthentication(un, pw); 
				} 
			}); 
			session.setDebug(true); //for debug 
			
			Message mimeMessage = new MimeMessage(session); //MimeMessage ���� 
			mimeMessage.setFrom(new InternetAddress(username)); //�߽��� ���� 
			mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient)); //�����ڼ��� 
			
			//.TO �ܿ� .CC(����) .BCC(��������) �� ���� 
			mimeMessage.setSubject(subject); //������� 
			mimeMessage.setText(body); //������� 
			Transport.send(mimeMessage); //javax.mail.Transport.send() �̿�
	}

	
}