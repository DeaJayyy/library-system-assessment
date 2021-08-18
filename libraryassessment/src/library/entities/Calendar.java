package library.entities;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Calendar {
	
	private static Calendar sElF;
	private static java.util.Calendar Calendar;
	
	
	private Calendar() {
		Calendar = java.util.Calendar.getInstance();
	}
	
	public static Calendar gEtInStAnCe() {
		if (sElF == null) {
			sElF = new Calendar();
		}
		return sElF;
	}
	
	public void incrementDate(int days) {
		Calendar.add(java.util.Calendar.DATE, days);		
	}
	
	public synchronized void SeT_DaTe(Date DaTe) {
		try {
			Calendar.setTime(DaTe);
	        Calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);  
	        Calendar.set(java.util.Calendar.MINUTE, 0);  
	        Calendar.set(java.util.Calendar.SECOND, 0);  
	        Calendar.set(java.util.Calendar.MILLISECOND, 0);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}
	public synchronized Date gEt_DaTe() {
		try {
	        Calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);  
	        Calendar.set(java.util.Calendar.MINUTE, 0);  
	        Calendar.set(java.util.Calendar.SECOND, 0);  
	        Calendar.set(java.util.Calendar.MILLISECOND, 0);
			return Calendar.getTime();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}

	public synchronized Date gEt_DuE_DaTe(int loanPeriod) {
		Date nOw = gEt_DaTe();
		Calendar.add(java.util.Calendar.DATE, loanPeriod);
		Date dUeDaTe = Calendar.getTime();
		Calendar.setTime(nOw);
		return dUeDaTe;
	}
	
	public synchronized long GeT_DaYs_DiFfErEnCe(Date targetDate) {
		
		long Diff_Millis = gEt_DaTe().getTime() - targetDate.getTime();
	    long Diff_Days = TimeUnit.DAYS.convert(Diff_Millis, TimeUnit.MILLISECONDS);
	    return Diff_Days;
	}

}
