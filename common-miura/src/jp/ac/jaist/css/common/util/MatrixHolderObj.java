package jp.ac.jaist.css.common.util;

import java.util.Collection;
import java.util.Hashtable;

/**
 * �}�X�ڂɕ�����Ȃǂ̃f�[�^�����т���N���X
 * 
 * �����ł́C����(int,int) -> String ���ӎ����āCput���\�b�h���쐬���Ă���
 * 
 */
public class MatrixHolderObj<K,V> {
	Hashtable<K, Hashtable<K,V>> datahash;

	public MatrixHolderObj() {
		datahash = new Hashtable<K, Hashtable<K,V>>();
	}

	public void put(K x, K y, V val) {
		Hashtable<K, V> tmp = datahash.get(x);
		if (tmp == null){
			tmp = new Hashtable<K, V>();
		}
		tmp.put(y, val);
		datahash.put(x, tmp);
	}

	public V get(K x, K y) {
		Hashtable<K, V> tmp = datahash.get(x);
		if (tmp == null){
			return null;
		} else {
			return tmp.get(y);
		}
	}

	public void removeAll() {
		datahash.clear();
	}
	
	public void removeAll(K x) {
		datahash.remove(x);
	}

	public void remove(K x, K y) {
		Hashtable<K, V> tmp = datahash.get(x);
		if (tmp == null){
			return;
		} else {
			tmp.remove(y);
		}
	}
	public Collection<V> values(K x){
		return datahash.get(x).values();
	}
}
