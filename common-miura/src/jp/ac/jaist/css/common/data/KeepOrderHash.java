package jp.ac.jaist.css.common.data;

import java.util.ArrayList;
import java.util.Hashtable;

public class KeepOrderHash<V,E> {
	public ArrayList<V> orderarray;
	public Hashtable<V,E> hash;
	public KeepOrderHash(){
		orderarray = new ArrayList<V>();
		hash = new Hashtable<V,E>();
	}
	public void put(V v, E e){
		orderarray.add(v);
		hash.put(v,e);
	}
	public ArrayList<V> getIterator(){
		return orderarray;
	}
	public E get(V v){
		return hash.get(v);
	}
}
