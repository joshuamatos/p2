package edu.uprm.cse.datastructures.cardealer.util;


public class MapEntry<K,V> implements Entry<K,V> {

	private K k;
	private V v;
	
	public MapEntry(K key, V value) {
		k = key;
		v = value;
	}
	
	public K getKey() {
		return k;
	}
	
	public V getValue() {
		return v;
	}
	
	public void setKey(K key) {
		k = key;
	}
	public V setValue(V value) {
		V old = v;
		v = value;
		return old;
	}
	
	public String toString() {
		return "Key: " + k + ",Value: " + v;
	}
	
}
