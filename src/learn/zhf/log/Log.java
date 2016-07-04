package learn.zhf.log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
	
	private static SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
	private static Date mDate = new Date();
	private static String mTime = "0000-00-00 00:00:00:000";
	private static String LEVEL_DEBUG = " DEBUG ";
	private static void setTime(){
		mDate.setTime(System.currentTimeMillis());
		mTime = mFormat.format(mDate);
	}
	
	public static void d(String tag,String content){
		setTime();
		System.out.println(mTime + LEVEL_DEBUG + "["+ tag + "]:" + content);
	}

}
