package edu.uprm.cse.datastructures.cardealer.util;

import java.util.ArrayList;
import java.util.Comparator;

public class ProbeHashMap<K,V> extends AbstractHashMap<K,V> {

	private MapEntry<K,V>[] table; //fixed array of entries (all initially full)
	private MapEntry<K,V> DEFUNCT = new MapEntry<>(null,null); //sentinel
	private int secondHashPrime = 13;
	private Comparator<K> keyComparator = null;
	private Comparator<V> valueComparator = null;
	public ProbeHashMap() {
		super();
	}
	public ProbeHashMap(int cap) {
		super(cap);
	}
	public ProbeHashMap(int cap, int p) {
		super(cap,p);
	}
	//Creates an empty table having length equal to current capacity
	protected void createTable() {
		table = (MapEntry<K,V>[]) new MapEntry[capacity];
	}
	
	//returns true if location is either empty or the defunct sentinel
	private boolean isAvailable(int i) {
		return (table[i] == null || table[i] == DEFUNCT);
	}
	
	public void setValueComparator(Comparator<V> comp) {
		valueComparator = comp;
	}
	public void setKeyComparator(Comparator<K> comp) {
		keyComparator = comp;
	}
	
	//Returns index with key, or -(a + 1) such that k could be added at index a
	private int findSlot(int h, K k) {
		int avail = -1; //no slot available so far
		int j = h; //index while scanning table
//-----------------------------------------------------------------------------------------------------------------------
//		Si empieza a funcionar raro  borra esto que fue lo que le añadi del double hash,
//		porque antes de añadir esto estaba funcinando bien.
//		System.out.println("Original --> " + h);
		int secHash = findSecondHash(h, k);
		for(int i = 0; i < table.length; i++) {
			System.out.println(table[i]);
			System.out.println(i);
		}
		
		System.out.println("SecondHash pos --> " + secHash);
		
		if(secHash > -1)
			j = secHash;
		
//-----------------------------------------------------------------------------------------------------------------------		
		do {
			if(isAvailable(j)) { //may be either empty or defunct
				if(avail == -1) //this is the first available slot
					avail = j; 
				if(table[j] == null) {//if empty, search fails immediately
//					System.out.println("Position used is --> " + j);
					break;
				}
			}
			else if(table[j].getKey().equals(k))
				return j; //successful match
			j = (j + 1) % capacity; //keep looking (cyclically)
		}
		while(j != h); //stop if we return to start
		return -(avail + 1); //search has failed 
	}
	
	//return value associated with key k in bucket with hash value h, or else null
	protected V bucketGet(int h, K k) {
		int j = findSlot(h, k);
		if( j < 0) //no match found
			return null;
		return table[j].getValue();
	}
	
	//Associates key k with value v in bucket with hash value h, returns old value
	protected V bucketPut(int h, K k, V v) {
		int j = findSlot(h, k);
		if(j >= 0) //this key is an existing entry
			return table[j].setValue(v);
		table[-(j+1)] = new MapEntry<>(k,v); //convert to proper index
		n++;
		return null;
	}
	
	
	//removes entry having key k from bucket with hash value h if any
	protected V bucketRemove(int h, K k) {
		int j = findSlot(h, k);
		if(j < 0) //nothing to remove
			return null;
		V answer = table[j].getValue();
		table[j] = DEFUNCT; //make this slot as deactivated
		n--;
		return answer;
	}
	
	//returns an iterable collection of all key-value entries of the map
	public Iterable<Entry<K, V>> entrySet(){
		ArrayList<Entry<K,V>> buffer = new ArrayList<>();
		for(int i = 0; i < capacity; i ++) {
			if(!isAvailable(i))
				buffer.add(table[i]);
		}
		return buffer;
	}

//	This methods piggybacks of the linear search, it just finds an empty position with second
//	hashing, if it encounters two collisions it does nothing and the linear probing will continue
//	as normal, if it finds an available position it will pass it to linear probing as the start
//	position .
//	No se si está bien esto cuando se refiere a lo del second hash, me deje llevar por la presentación
	private int findSecondHash(int h, K k) {
		int newHash = secondHashPrime - (h%secondHashPrime);
		int j = h;
		int avail = -1;
		int count = 0;

		if(isAvailable(h))
			if(table[h] == null)
				return h;
		do {
			if(isAvailable(j)) {
				if(avail == -1)
					avail = j;
				if(table[newHash] == null) {
					break;
				}
				
			}
			else {
				if(table[j].getKey().equals(k))
					return j;				
				count++;
			}
			if(count == 2) {
				System.out.println("Found two collitions");
//				if(k != null)
//				System.out.println("Key for car is --> " + k);
//				System.out.println("Value of j is --> " + table[j].getValue());
//				System.out.println("Key of j is --> " + table[j].getKey());
//				
//					System.out.println(table[ k].getKey());					
//				if(table[h] != null)
//					System.out.println(table[h].getValue());
//				
			
				avail = h;
				return avail;
			}
			
			j = (j + newHash) % capacity;
		}
		while(j != h);
		return avail;
	}
	
	public SortedList<K> getKeys(){
		
		if(keyComparator == null) {
			System.out.println("No key comparator supplied.");
			return null;
		}
		CircularSortedDoublyLinkedList<K> toRet = new CircularSortedDoublyLinkedList<>(keyComparator);
		for(K key: keySet()) {
			toRet.add(key);
		}
		return toRet;
	}
	
	public SortedList<V> getValues(){
		if(valueComparator == null) {
			System.out.println("No value comparator supplied.");
			return null;
		}
		CircularSortedDoublyLinkedList<V> toRet = new CircularSortedDoublyLinkedList<>(valueComparator);
		for(V value: values()) {
			toRet.add(value);
		}
		return toRet;
	}	
}
