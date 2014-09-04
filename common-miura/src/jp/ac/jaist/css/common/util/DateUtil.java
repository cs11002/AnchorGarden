package jp.ac.jaist.css.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Date Utility Class
 * 
 * @author miuramo
 *
 */
public class DateUtil {
	/**
	 * Longで示された時刻を，フォーマットした文字列を返す
	 * @param time Longで示された時刻
	 * @return フォーマットした文字列
	 */
	public static String convert(long time) {
		Date d = new Date(time);
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return df.format(d);
	}
	/**
	 * 日付をあらわす数字から，Dateオブジェクト生成
	 * @param y
	 * @param m 月(1-12)
	 * @param d
	 * @param h
	 * @param min
	 * @param s
	 * @return
	 */
	public static Date getDateFromyMdHms(int y, int m, int d, int h, int min, int s){
		GregorianCalendar gc = new GregorianCalendar(y,m-1,d,h,min,s);
		return new Date(gc.getTimeInMillis());
	}
}
