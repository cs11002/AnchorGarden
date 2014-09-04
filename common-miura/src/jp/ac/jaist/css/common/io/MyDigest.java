package jp.ac.jaist.css.common.io;

import java.io.FileInputStream;
import java.security.MessageDigest;

public class MyDigest {
	public static String getBytesDigest(byte[] ba) throws Exception {//�����񂩂�_�C�W�F�X�g�𐶐�����
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(ba);//dat�z�񂩂�_�C�W�F�X�g���v�Z����
		return printDigest(md.digest());
	}
	public static String printDigest(byte[] digest) {//�_�C�W�F�X�g��16�i���ŕ\������
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < digest.length; i++) {
			int d = digest[i];
			if (d < 0) {//byte�^�ł�128�`255�����l�ɂȂ��Ă���̂ŕ␳
				d += 256;
			}
			if (d < 16) {//0�`15��16�i����1�����ɂȂ�̂ŁA2�����ɂȂ�悤����0��ǉ�
				sb.append("0");
			}
			sb.append(Integer.toString(d, 16));//�_�C�W�F�X�g�l��1�o�C�g��16�i��2�����ŕ\��
		}
		return sb.toString();
	}
	public static byte[] getFileDigest(String filename) throws Exception {//�t�@�C���̒��g����_�C�W�F�X�g�𐶐�����
		MessageDigest md = MessageDigest.getInstance("MD5");
		FileInputStream in = new FileInputStream(filename);
		byte[] dat = new byte[256];
		int len;
		while ((len = in.read(dat)) >=0) {
			md.update(dat, 0, len);//dat�z��̐擪����len�܂ł̃_�C�W�F�X�g���v�Z����
		}
		in.close();
		return md.digest();
	}
	public static byte[] getStringDigest(String data) throws Exception {//�����񂩂�_�C�W�F�X�g�𐶐�����
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] dat = data.getBytes();
		md.update(dat);//dat�z�񂩂�_�C�W�F�X�g���v�Z����
		return md.digest();
	}
	
}