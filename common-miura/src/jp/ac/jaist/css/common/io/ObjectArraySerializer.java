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
 * <p>������Ƃ����ݒ�ȂǁC�I�u�W�F�N�g��ۑ��E�ǂݏo���̂ɕ֗��ȃN���X</p>
 * <p>�ۑ�����Ƃ��̎g�����F�ۑ��������f�[�^��ArrayList�ɓ���āCwriteObjectToFile���ĂԁD</p>
 * <p>�ǂݏo���Ƃ��̎g�����F(1) readObjectFromFile���ĂсCArrayList�𓾂�D(2)���Ƃ͊e���C�L���X�g���ė��p����</p>
 * @author miuramo
 */
public class ObjectArraySerializer {

	/**
	 * ArrayList�Ɋi�[�����I�u�W�F�N�g���V���A���C�Y����GZIP���ĕۑ�����
	 * @param path �t�@�C��
	 * @param _objlist �I�u�W�F�N�g�̃��X�g
	 */
	public static void writeObjectToFile(String path, ArrayList<Object> _objlist){
		byte[] ba = byteSerializeExport(_objlist);
		FileReadWriter.writeBytesToFile(path, ba);
	}
	/**
	 * �I�u�W�F�N�g���V���A���C�Y����GZIP���ĕۑ������t�@�C������ArrayList�Ɏ��o��
	 * @param path �t�@�C��
	 * @return �I�u�W�F�N�g�̃��X�g
	 */
	public static ArrayList<Object> readObjectFromFile(String path){
		File file = new File(path);
		if (!file.exists()) return null;
		byte[] ba = FileReadWriter.readBytesFromFile(path);
		return byteSerializeImport(ba);
	}

	/**
	 * �����ɓn����ArrayList�Ɋi�[���ꂽ�I�u�W�F�N�g�̃o�C�g�\����Ԃ�
	 */
	public static byte[] byteSerializeExport(ArrayList<Object> _objlist) {
		// serialize to byte array
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStream gzipos = null;
		ObjectOutputStream pobjos = null; // PNode��ۑ�����Ƃ���PObjectOutputStream

		try {
			gzipos = new GZIPOutputStream(baos);
			pobjos = new ObjectOutputStream(gzipos);
			pobjos.writeInt(_objlist.size()); // ������������
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
	 * SerializeObjectSupport�̎����F�o�C�g�\����Ǎ��݁C�I�u�W�F�N�g���X�g��Ԃ�
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
