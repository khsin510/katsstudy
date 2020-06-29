package com.javaMavenTest.scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SchedulerController {

	@Autowired
	private SchedulerDao schedulerDao;
	
//	 @Scheduled(cron = "0/3 * * * * *") // 서버 시작 후 3초마다 돈다.
//	    public void cronTest1(){
//	        try {
//	        	
//	            String value = "1";
//	            value = schedulerDao.test();
//	            System.out.println("value:"+value);
//	        	System.out.println("스케줄러 테스트");
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	        }
//	    }

	

}

