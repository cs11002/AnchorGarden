package jp.ac.jaist.css.common.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import jp.ac.jaist.css.common.io.FileReadWriter;

/**
 * <p>ちょっとした設定など，オブジェクトを保存・読み出すのに便利なクラス</p>
 * <p>保存するときの使い方：保存したいデータをArrayListに入れて，writeObjectToFileを呼ぶ．</p>
 * <p>読み出すときの使い方：(1) readObjectFromFileを呼び，ArrayListを得る．(2)あとは各自，キャストして利用する</p>
 * @author miuramo
 */
public class ObjectArraySerializer {

	/**
	 * ArrayListに格納したオブジェクトをシリアライズしてGZIPして保存する
	 * @param path ファイル
	 * @param _objlist オブジェクトのリスト
	 */
	public static void writeObjectToFile(String path, ArrayList<Object> _objlist){
		byte[] ba = byteSerializeExport(_objlist);
		FileReadWriter.writeBytesToFile(path, ba);
	}
	/**
	 * オブジェクトをシリアライズしてGZIPして保存したファイルからArrayListに取り出す
	 * @param path ファイル
	 * @return オブジェクトのリスト
	 */
	public static ArrayList<Object> readObjectFromFile(String path){
		File file = new File(path);
		if (!file.exists()) return null;
		byte[] ba = FileReadWriter.readBytesFromFile(path);
		return byteSerializeImport(ba);
	}

	/**
	 * 引数に渡したArrayListに格納されたオブジェクトのバイト表現を返す
	 */
	public static byte[] byteSerializeExport(ArrayList<Object> _objlist) {
		// serialize to byte array
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStream gzipos = null;
		ObjectOutputStream pobjos = null; // PNodeを保存するときはPObjectOutputStream

		try {
			gzipos = new GZIPOutputStream(baos);
			pobjos = new ObjectOutputStream(gzipos);
			pobjos.writeInt(_objlist.size()); // 数を書き込む
			for (Object o : _objlist) {
				pobjos.writeObject(o);
			}
			pobjos.close();
			gzipos.close();
		} catch (IOException iex) {
			iex.printStackTrace(System.out);
		}
//		System.out.println("save size: " + baos.size() + " bytes.");
		return baos.toByteArray();
	}

	/**
	 * SerializeObjectSupportの実装：バイト表現を読込み，オブジェクトリストを返す
	 */
	public static ArrayList<Object> byteSerializeImport(byte[] ba) {
		ByteArrayInputStream bais = new ByteArrayInputStream(ba);
		InputStream gzipis = null;
		ObjectInputStream ois = null;
		ArrayList<Object> objlist = new ArrayList<Object>();
		try {
			gzipis = new GZIPInputStream(bais);
			ois = new ObjectInputStream(gzipis);
			int num = ois.readInt();
			for (int i = 0; i < num; i++) {
				objlist.add(ois.readObject());
			}
			ois.close();
			gzipis.close();
			bais.close();
		} catch (IOException excep) {
			excep.printStackTrace(System.err);
		} catch (ClassNotFoundException excnf) {
			System.err.println("ClassNotFound Error");
		} finally {
		}
		return objlist;
	}
}
