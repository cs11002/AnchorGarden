package jp.ac.jaist.css.common.io;

import java.io.FileInputStream;
import java.security.MessageDigest;

public class MyDigest {
	public static String getBytesDigest(byte[] ba) throws Exception {//文字列からダイジェストを生成する
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(ba);//dat配列からダイジェストを計算する
		return printDigest(md.digest());
	}
	public static String printDigest(byte[] digest) {//ダイジェストを16進数で表示する
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < digest.length; i++) {
			int d = digest[i];
			if (d < 0) {//byte型では128～255が負値になっているので補正
				d += 256;
			}
			if (d < 16) {//0～15は16進数で1けたになるので、2けたになるよう頭に0を追加
				sb.append("0");
			}
			sb.append(Integer.toString(d, 16));//ダイジェスト値の1バイトを16進数2けたで表示
		}
		return sb.toString();
	}
	public static byte[] getFileDigest(String filename) throws Exception {//ファイルの中身からダイジェストを生成する
		MessageDigest md = MessageDigest.getInstance("MD5");
		FileInputStream in = new FileInputStream(filename);
		byte[] dat = new byte[256];
		int len;
		while ((len = in.read(dat)) >=0) {
			md.update(dat, 0, len);//dat配列の先頭からlenまでのダイジェストを計算する
		}
		in.close();
		return md.digest();
	}
	public static byte[] getStringDigest(String data) throws Exception {//文字列からダイジェストを生成する
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] dat = data.getBytes();
		md.update(dat);//dat配列からダイジェストを計算する
		return md.digest();
	}
	
}