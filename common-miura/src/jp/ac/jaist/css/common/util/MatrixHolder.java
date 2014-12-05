package jp.ac.jaist.css.common.util;

import java.util.Collection;
import java.util.Hashtable;

/**
 * �}�X�ڂɕ�����Ȃǂ̃f�[�^�����т���N���X
 * 
 * �����ł́C����(int,int) -> String ���ӎ����āCput���\�b�h���쐬���Ă���
 * 
 */
public class MatrixHolder<V> {
	Hashtable<String, V> datahash;

	public MatrixHolder() {
		datahash = new Hashtable<String, V>();
	}

	public void put(int x, int y, V val) {
		String key = String.valueOf(x) + ":" + String.valueOf(y);
		datahash.put(key, val);
	}

	public V get(int x, int y) {
		String key = String.valueOf(x) + ":" + String.valueOf(y);
		return datahash.get(key);
	}

	public void removeAll() {
		datahash.clear();
	}

	public void remove(int x, int y) {
		String key = String.valueOf(x) + ":" + String.valueOf(y);
		// datahash.put(key,null);
		datahash.remove(key);
	}
	public Collection<V> values(){
		return datahash.values();
	}
}
