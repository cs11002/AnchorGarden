package jaist.css.covis;

import jaist.css.covis.util.FileReadWriter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.StringTokenizer;

/**
 * 設定項目（プロパティ）オブジェクト．コンストラクタで設定ファイル(*.ssf)を読み込み，自分自身の変数のデフォルト値をリフレクションによって変更する．
 * @author miuramo
 *
 */
public class CoVisProperty {
	/**
	 * ATNWindow表示の幅
	 */
	public int viewsizex = 570;
	/**
	 * ATNWindow表示の高さ
	 */
	public int viewsizey = 560;
	/**
	 * ATNWindow表示の位置（x座標）
	 */
	public int viewlocx = 420;
	/**
	 * ATNWindow表示の位置（y座標）
	 */
	public int viewlocy = 5;

	/**
	 * ホイール回転ズーム方向の設定(-1 または 1)
	 */
	public int wheelrotationdirection = -1;

	/**
	 * 設定ファイルを読み込んだ，プロパティオブジェクトを返します
	 * @param fname 設定ファイル
	 */
	public CoVisProperty(String fname) {
		if (fname != null){
			String[] lines = FileReadWriter.getLines(fname);
			String line;
			StringTokenizer st2;
			for (int i = 0; i < lines.length; i++) {
				// System.out.println(lines[i]);
				int index = lines[i].indexOf(";");
				if (index > -1) {
					line = lines[i].substring(0, index);
				} else {
					line = lines[i];
				}
				// System.out.println(line);
				st2 = new StringTokenizer(line, "=");
				if (st2.countTokens() == 2) {
					String finame = st2.nextToken().trim();
					String val = st2.nextToken().trim();
					Object o = gen_primitive(this, finame, val);
					setter(this, finame, o);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static final Class getWrapperClass(Class primitiveClass) {
		Class wrapper = null;
		if (primitiveClass == Boolean.TYPE)
			wrapper = java.lang.Boolean.class;
		else if (primitiveClass == Character.TYPE)
			wrapper = java.lang.Character.class;
		else if (primitiveClass == Byte.TYPE)
			wrapper = java.lang.Byte.class;
		else if (primitiveClass == Short.TYPE)
			wrapper = java.lang.Short.class;
		else if (primitiveClass == Integer.TYPE)
			wrapper = java.lang.Integer.class;
		else if (primitiveClass == Long.TYPE)
			wrapper = java.lang.Long.class;
		else if (primitiveClass == Float.TYPE)
			wrapper = java.lang.Float.class;
		else if (primitiveClass == Double.TYPE)
			wrapper = java.lang.Double.class;
		else if (primitiveClass == java.lang.String.class)
			wrapper = java.lang.String.class;
		return wrapper;
	}

	@SuppressWarnings("unchecked")
	public static Object gen_primitive(Object obj, String fieldname, String value) {
		try {
			// (1) determine target wrapper class of primitive
			Class cthis = obj.getClass();
			Field f = cthis.getField(fieldname);
			// System.out.println(f.getType().toString());
			Class pclass = getWrapperClass(f.getType());
			// System.out.println(pclass.toString());
			Class[] pconstcarg = new Class[1];
			pconstcarg[0] = Class.forName("java.lang.String");
			Constructor pconst = pclass.getConstructor(pconstcarg);
			Object[] parg = new Object[1];
			parg[0] = value;
			return pconst.newInstance(parg);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		}
		return null;
	}

	// obj: 対象オブジェクト，fieldname 対象フィールド, o 変更後の値
	@SuppressWarnings("unchecked")
	public static void setter(Object obj, String fieldname, Object o) {
		try {
			Class c = obj.getClass();
			Field f = c.getField(fieldname);
			StringBuffer sb = new StringBuffer();
			sb.append("  | " + fieldname + " : ");
			sb.append(f.get(obj).toString() + " -> ");
			f.set(obj, o);
			sb.append(f.get(obj).toString());
			System.out.println(sb.toString());
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		}
	}
}
