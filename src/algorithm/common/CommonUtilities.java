package algorithm.common;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
		
		/*HashMap<Integer, HashMap<Integer, List>> reqResTimeLogTable = new HashMap<Integer, HashMap<Integer, List>>();
		String requestTimeStamp=new SimpleDateFormat("HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
		HashMap<Integer,List> reqTypeTimeStampHM=new HashMap<Integer,List>();
		List locationAndRequestTimeStampAL=new ArrayList();
		
		locationAndRequestTimeStampAL.add(0,123); //location
//		locationAndRequestTimeStampAL.add(1,requestTimeStamp); //request timestamp
//		locationAndRequestTimeStampAL.add(2,null); //response time set to null by default
		reqTypeTimeStampHM.put(7, locationAndRequestTimeStampAL); //requesttype[key]-location n time [values-List]
		
		
		locationAndRequestTimeStampAL.add(0,123); //location
		locationAndRequestTimeStampAL.add(1,requestTimeStamp); //request timestamp
		reqTypeTimeStampHM.put(9, locationAndRequestTimeStampAL); //requesttype[key]-location n time [values-List]
		
		reqResTimeLogTable.put(1,reqTypeTimeStampHM);
		System.out.println(reqResTimeLogTable);
		System.out.println(reqResTimeLogTable.get(1).get(7));
		
//		System.out.println(requestTimeHM.get(1).get(0));
		
		reqResTimeLogTable.get(1).get(7).add(1, null);
		reqResTimeLogTable.get(1).get(7).set(1, 555);
		reqResTimeLogTable.get(1).get(7).set(1, 333);
		System.out.println(reqResTimeLogTable.get(1).get(7));*/
		/*HashMap<Integer, HashMap<Integer,Double>> locationResponseTimeLogTable = new HashMap<Integer, HashMap<Integer,Double>>();
		HashMap<Integer,Double> minResponseTimeTempHM=new HashMap<Integer,Double>();
	    minResponseTimeTempHM.put(10, 11.0);
		Object value = locationResponseTimeLogTable.get(1);
	    if (value == null) {
	        locationResponseTimeLogTable.put(1, minResponseTimeTempHM);
	    }
		System.out.println(locationResponseTimeLogTable);
		minResponseTimeTempHM.put(10, 111.0);
		if(locationResponseTimeLogTable.get(1).get(10)>0){
			 locationResponseTimeLogTable.put(1, minResponseTimeTempHM);
		}
		System.out.println(locationResponseTimeLogTable);*/
		
		//Minimum response time till now ********IMP********
		/*HashMap<Integer, HashMap<Integer,Double>> locationResponseTimeLogTable = new HashMap<Integer, HashMap<Integer,Double>>();
		HashMap<Integer,Double> minResponseTimeTempHM=new HashMap<Integer,Double>();
	    minResponseTimeTempHM.put(requestType, diffSeconds);
	    Object value = locationResponseTimeLogTable.get(location);
	    if (value == null) {
	        locationResponseTimeLogTable.put(location, minResponseTimeTempHM);
	    }
	    else{
	    	Object tempValue = locationResponseTimeLogTable.get(location).get(requestType);
	    	if(tempValue == null){
	    		locationResponseTimeLogTable.put(location, minResponseTimeTempHM);
	    	}
	    	else{
	    		if(diffSeconds<locationResponseTimeLogTable.get(location).get(requestType)){
	    			locationResponseTimeLogTable.put(location, minResponseTimeTempHM);
	    		}
	    	}
	    }*/
		/*HashMap<Integer, HashMap<Integer,List>> locationResponseTimeLogTable = new HashMap<Integer, HashMap<Integer,List>>();
		HashMap<Integer,List> avgResponseTimeTempHM=new HashMap<Integer,List>();
	    List avgResponseTimeAL=new ArrayList();
	    avgResponseTimeAL.add(0,12);
	    avgResponseTimeAL.add(1,8);
	    avgResponseTimeTempHM.put(4, avgResponseTimeAL);
	    Object value = locationResponseTimeLogTable.get(456);
	    if (value == null) {
	        locationResponseTimeLogTable.put(456, avgResponseTimeTempHM);
	    }
	    Object tempValue = locationResponseTimeLogTable.get(456).get(4);
	    if(tempValue == null){
	    	locationResponseTimeLogTable.put(456, avgResponseTimeTempHM);
	    }
	    else{
	    	Double newAvgResponseTime=(Double.parseDouble(locationResponseTimeLogTable.get(456).get(4).get(0).toString())+4)/(Integer.parseInt(locationResponseTimeLogTable.get(456).get(4).get(1).toString())+1);
	    	avgResponseTimeAL=new ArrayList();
	    	avgResponseTimeAL.add(0,newAvgResponseTime);
		    avgResponseTimeAL.add(1,Integer.parseInt(locationResponseTimeLogTable.get(456).get(4).get(1).toString())+1);
		    avgResponseTimeTempHM.put(4, avgResponseTimeAL);
		    locationResponseTimeLogTable.put(456,avgResponseTimeTempHM);
	    }
	    
	    
	    avgResponseTimeAL=new ArrayList();
	    avgResponseTimeTempHM=new HashMap<>();
	    avgResponseTimeAL.add(0,17);
	    avgResponseTimeAL.add(1,6);
	    avgResponseTimeTempHM.put(4, avgResponseTimeAL);
	    value=null;tempValue=null;
	    value = locationResponseTimeLogTable.get(123);
	    if (value == null) {
	        locationResponseTimeLogTable.put(123, avgResponseTimeTempHM);
	    }
	    tempValue = locationResponseTimeLogTable.get(123).get(4);
	    if(tempValue == null){
	    	locationResponseTimeLogTable.put(123, avgResponseTimeTempHM);
	    }
	    else{
	    	Double newAvgResponseTime=(Double.parseDouble(locationResponseTimeLogTable.get(123).get(4).get(0).toString())+4)/(Integer.parseInt(locationResponseTimeLogTable.get(123).get(4).get(1).toString())+1);
	    	avgResponseTimeAL=new ArrayList();
	    	avgResponseTimeAL.add(0,newAvgResponseTime);
		    avgResponseTimeAL.add(1,Integer.parseInt(locationResponseTimeLogTable.get(123).get(4).get(1).toString())+1);
		    avgResponseTimeTempHM.put(4, avgResponseTimeAL);
		    locationResponseTimeLogTable.put(123,avgResponseTimeTempHM);
	    }
	    System.out.println(locationResponseTimeLogTable);
	    int temp=0,templocation=1;
	    double tempavgval=0;
	    Iterator<Entry<Integer, HashMap<Integer, List>>> it = locationResponseTimeLogTable.entrySet().iterator();
	    HashMap<Integer, List> hm=new HashMap<Integer, List>();
	    while (it.hasNext()) {
	    	Map.Entry<Integer, HashMap<Integer, List>> pair = (Map.Entry<Integer, HashMap<Integer, List>>)it.next();
			System.out.println(pair.getValue());
			hm=pair.getValue();
//			System.out.println(hm.get(4).get(0));
			if(temp==0){
				tempavgval=Double.parseDouble(hm.get(4).get(0).toString());
				templocation=pair.getKey();
				temp++;
			}
			else{
				if(tempavgval>Double.parseDouble(hm.get(4).get(0).toString())){
					templocation=pair.getKey();
				}
			}
		}
	    System.out.println(templocation);*/
		
		/*ArrayList<Integer> insufficientResourceLocation=new ArrayList<Integer>();
//		insufficientResourceLocation.add(1);
//		insufficientResourceLocation.add(2);
		int i=2;
		if(insufficientResourceLocation.contains(3)){
			System.out.println("yes");
		}*/
		/*HashMap<Integer, HashMap<Integer,List>> locationResponseTimeLogTable = new HashMap<Integer, HashMap<Integer,List>>();
		ArrayList lst=new ArrayList<>();
		HashMap<Integer,List> hmap=new HashMap<Integer,List>();
		for(int tempVar=0;tempVar<5;tempVar++){
			if(tempVar<2)
				lst.add(0);
			
			hmap.put(tempVar, lst);
		}
		
		for(int tempVar=0;tempVar<5;tempVar++){
			locationResponseTimeLogTable.put(tempVar, hmap);
		}
		if(locationResponseTimeLogTable.get(0)!=null){
			System.out.println(locationResponseTimeLogTable);
		}*/
		
		/*ArrayList lst=new ArrayList<>();
		lst.add(0,"rohan");
		System.out.println(lst);
		lst.set(0,"abc");
		System.out.println(lst);*/
		
		/*HashMap<Integer, HashMap<Integer,ArrayList>> locationResponseTimeLogTable = new HashMap<Integer, HashMap<Integer,ArrayList>>();
		ArrayList lst=new ArrayList<>();
		HashMap<Integer,ArrayList> hmap=new HashMap<Integer,ArrayList>();
		for(int tempVar=0;tempVar<3;tempVar++){
			if(tempVar<2)
				lst.add(0);
			
			hmap.put(tempVar, lst);
		}
		
		for(int tempVar=1;tempVar<3;tempVar++){
			locationResponseTimeLogTable.put(tempVar, hmap);
		}
		System.out.println("before: "+locationResponseTimeLogTable);
					
//		System.out.println(lst);
		lst.add(0, 5.5);
		lst.add(1, 1);
		hmap.put(2, lst);
		
		lst.add(0, 33);
		lst.add(1, 2);
		System.out.println("hmap before: "+hmap);
		
            	System.out.println("in 2");
            	hmap.get(0).set(0, 33);
                hmap.get(1).set(1, 1);
           
		System.out.println("hmap: "+hmap);
		System.out.println(locationResponseTimeLogTable);
		locationResponseTimeLogTable.put(1, hmap);
//		locationResponseTimeLogTable.get(1).get(2).set(0,3);
		System.out.println(locationResponseTimeLogTable);*/
		/*JSONObject jo = new JSONObject();
		JSONArray jArr = new JSONArray();
		List lst = new ArrayList<>();
		for(int j=0;j<10;j++){
			lst = new ArrayList<>();
			for(int i=0;i<3;i++){
				lst.add(i);
			}
			jArr.add(lst);
		}
		
		jo.put("HBAlgoGraph", jArr);
		System.out.println(jo);
		System.out.println(jo.get("HBAlgoGraph"));
		ArrayList ar=(ArrayList)jo.get("HBAlgoGraph");
		System.out.println(ar.get(0));*/
		List lst = new ArrayList<>();
//		lst.add("");
		if(lst.get(0).equals("")){
			System.out.println("Blank");
		}
		else{
			System.out.println("NOT Blank");
		}
		
	}
}
