package date;

import java.util.Calendar;
import java.util.Date;

/**
 * Class to deal with date operations
 * @author tomas
 * 
 */
public class Data {
	
	
	 /**
	  * Method to add seconds = [param seconds] to a date = [param date]
	  * @param date - date to add the seconds
	  * @param seconds - seconds to add to the date
	  * @return new date after performed the last operation
	  */
	
	public static Date addSeconds(Date date, Integer seconds) {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    cal.add(Calendar.SECOND, seconds);
	    return cal.getTime();
	  }
	  
	  
}
