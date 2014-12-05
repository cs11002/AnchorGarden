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
	 * Long�Ŏ����ꂽ�������C�t�H�[�}�b�g�����������Ԃ�
	 * @param time Long�Ŏ����ꂽ����
	 * @return �t�H�[�}�b�g����������
	 */
	public static String convert(long time) {
		Date d = new Date(time);
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return df.format(d);
	}
	/**
	 * ���t������킷��������CDate�I�u�W�F�N�g����
	 * @param y
	 * @param m ��(1-12)
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
