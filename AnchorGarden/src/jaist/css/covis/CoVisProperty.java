package jaist.css.covis;

import jaist.css.covis.util.FileReadWriter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.StringTokenizer;

/**
 * �ݒ荀�ځi�v���p�e�B�j�I�u�W�F�N�g�D�R���X�g���N�^�Őݒ�t�@�C��(*.ssf)��ǂݍ��݁C�������g�̕ϐ��̃f�t�H���g�l�����t���N�V�����ɂ���ĕύX����D
 * @author miuramo
 *
 */
public class CoVisProperty {
	/**
	 * ATNWindow�\���̕�
	 */
	public int viewsizex = 750;
	//public int viewsizex = 570;�����l
	/**
	 * ATNWindow�\���̍���
	 */
	public int viewsizey = 750;
	//public int viewsizey = 560;�����l
	/**
	 * ATNWindow�\���̈ʒu�ix���W�j
	 */
	public int viewlocx = 420;
	//public int viewlocx = 420;�����l
	/**
	 * ATNWindow�\���̈ʒu�iy���W�j
	 */
	public int viewlocy = 5;
	//public int viewlocy = 5;�����l

	/**
	 * �z�C�[����]�Y�[�������̐ݒ�(-1 �܂��� 1)
	 */
	public int wheelrotationdirection = -1;

	/**
	 * �ݒ�t�@�C����ǂݍ��񂾁C�v���p�e�B�I�u�W�F�N�g��Ԃ��܂�
	 * @param fname �ݒ�t�@�C��
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

	// obj: �ΏۃI�u�W�F�N�g�Cfieldname �Ώۃt�B�[���h, o �ύX��̒l
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