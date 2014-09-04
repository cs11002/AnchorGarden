package jp.ac.jaist.css.common.util;

import java.util.Collection;
import java.util.Hashtable;

/**
 * マス目に文字列などのデータを結びつけるクラス
 * 
 * ここでは，特に(int,int) -> String を意識して，putメソッドを作成している
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
