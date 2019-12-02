package edu.uprm.cse.datastructures.cardealer.util;

import java.util.Iterator;

public abstract class AbstractMap<K,V> implements Map<K,V> {

	public boolean isEmpty() {
		return size() == 0;
	}
	
	//Support for public keySet method
	private class KeyIterator implements Iterator<K>{
		private Iterator<Entry<K,V>> entries = entrySet().iterator();
		public boolean hasNext() {
			return entries.hasNext();
		}
		public K next() {
			return entries.next().getKey();
		}
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	
	private class KeyIterable implements Iterable<K>{
		public Iterator<K> iterator(){
			return new KeyIterator();
		}
	}
	
	public Iterable<K> keySet(){
		return new KeyIterable();
	}
	
	//Support for public values method
	private class ValueIterator implements Iterator<V>{
		private Iterator<Entry<K,V>> entries = entrySet().iterator();
		public boolean hasNext() {
			return entries.hasNext();
		}
		public V next() {
			return entries.next().getValue();
		}
		public void remove() {
			throw new UnsupportedOperationException();
		}		
	}
	
	private class ValueIterable implements Iterable<V>{
		public Iterator<V> iterator(){
			return new ValueIterator();
		}
	}
	
	public Iterable<V> values(){
		return new ValueIterable();
	}
	
	public abstract Iterable<Entry<K,V>> entrySet();
}
