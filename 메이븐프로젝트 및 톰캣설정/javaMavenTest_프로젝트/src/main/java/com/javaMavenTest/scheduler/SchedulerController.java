package com.javaMavenTest.scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SchedulerController {

	@Autowired
	private SchedulerDao schedulerDao;
	
//	 @Scheduled(cron = "0/3 * * * * *") // ���� ���� �� 3�ʸ��� ����.
//	    public void cronTest1(){
//	        try {
//	        	
//	            String value = "1";
//	            value = schedulerDao.test();
//	            System.out.println("value:"+value);
//	        	System.out.println("�����ٷ� �׽�Ʈ");
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	        }
//	    }

	

}

