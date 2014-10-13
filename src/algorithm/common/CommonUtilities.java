package algorithm.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CommonUtilities {
	public static void main(String[] args){
		
		/*String dt1=new SimpleDateFormat("HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
		try{
			Thread.sleep(100);
		}		
		catch(Exception e){
			
		}
		String dt2=new SimpleDateFormat("HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
		
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SSS"); 
		Date d1 = null;
		Date d2 = null;
		try {
		    d1 = format.parse(dt1);
		    d2 = format.parse(dt2);
		} catch (ParseException e) {
		    e.printStackTrace();
		}
		
		long diff = d2.getTime() - d1.getTime();
		long diffMilliSeconds = diff; 
		long diffSeconds = diff / 1000 % 60;  
		long diffMinutes = diff / (60 * 1000) % 60;      
		long diffHours = diff / (60 * 60 * 1000);   
		System.out.println("Time in milliseconds: " + diffMilliSeconds + " milliseconds.");   
		System.out.println("Time in seconds: " + diffSeconds + " seconds.");         
		System.out.println("Time in minutes: " + diffMinutes + " minutes.");         
		System.out.println("Time in hours: " + diffHours + " hours."); */
		
		String requestTimeStamp=new SimpleDateFormat("HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
		HashMap<Integer, List<String>> requestTimeHM=new HashMap<Integer, List<String>>();
		List<String> reqTimeStampLS=new ArrayList<String>();
		reqTimeStampLS.add(0,requestTimeStamp );
		requestTimeHM.put(1,reqTimeStampLS);
		System.out.println(requestTimeHM);
		reqTimeStampLS.add(1,"rohan" );
		requestTimeHM.put(1,reqTimeStampLS);
		System.out.println(requestTimeHM.get(1).get(0));
	}
}
