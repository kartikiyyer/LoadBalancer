package algorithm.common;

import java.text.DecimalFormat;
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
			Thread.sleep(1);
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
		double diffSeconds = diff / 1000.0;  
		double diffMinutes = diff / (60 * 1000.0);     
		double diffHours = diff / (60 * 60 * 1000.0);  
		DecimalFormat df = new DecimalFormat("#.#####");
		System.out.println("Time in milliseconds: " + diffMilliSeconds + " milliseconds.");   
		System.out.println("Time in seconds: " + diffSeconds + " seconds.");         
		System.out.println("Time in minutes: " + df.format(diffMinutes) + " minutes.");         
		System.out.println("Time in hours: " + df.format(diffHours) + " hours."); */
		
		/*String requestTimeStamp=new SimpleDateFormat("HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
		HashMap<Integer, List<String>> requestTimeHM=new HashMap<Integer, List<String>>();
		List<String> reqTimeStampLS=new ArrayList<String>();
		reqTimeStampLS.add(0,requestTimeStamp );
		requestTimeHM.put(1,reqTimeStampLS);
		System.out.println(requestTimeHM);
		reqTimeStampLS.add(2,"rohan" );
		requestTimeHM.put(1,reqTimeStampLS);
		System.out.println(requestTimeHM.get(1));
		System.out.println(requestTimeHM.get(1).get(0));*/
		
		/*HashMap<Integer,String> req=new HashMap<Integer,String>();
		req.put(1, "rohan");
		System.out.println(req);
		req.put(1, "abc");
		System.out.println(req);*/
		
		HashMap<Integer, HashMap<Integer, List>> reqResTimeLogTable = new HashMap<Integer, HashMap<Integer, List>>();
		String requestTimeStamp=new SimpleDateFormat("HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
		HashMap<Integer,List> reqTypeTimeStampHM=new HashMap<Integer,List>();
		List locationAndRequestTimeStampAL=new ArrayList();
		
		locationAndRequestTimeStampAL.add(0,123); //location
//		locationAndRequestTimeStampAL.add(1,requestTimeStamp); //request timestamp
//		locationAndRequestTimeStampAL.add(2,null); //response time set to null by default
		reqTypeTimeStampHM.put(7, locationAndRequestTimeStampAL); //requesttype[key]-location n time [values-List]
		
		
		/*locationAndRequestTimeStampAL.add(0,123); //location
		locationAndRequestTimeStampAL.add(1,requestTimeStamp); //request timestamp
		reqTypeTimeStampHM.put(9, locationAndRequestTimeStampAL); //requesttype[key]-location n time [values-List]
		*/
		reqResTimeLogTable.put(1,reqTypeTimeStampHM);
		System.out.println(reqResTimeLogTable);
		System.out.println(reqResTimeLogTable.get(1).get(7));
		
//		System.out.println(requestTimeHM.get(1).get(0));
		
		reqResTimeLogTable.get(1).get(7).add(1, null);
		reqResTimeLogTable.get(1).get(7).set(1, 555);
		reqResTimeLogTable.get(1).get(7).set(1, 333);
		System.out.println(reqResTimeLogTable.get(1).get(7));
		
	}
}
