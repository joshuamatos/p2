package edu.uprm.cse.datastructures.cardealer.util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class CircularSortedDoublyLinkedList<E> implements SortedList<E> {
	
	private int size;
	private DNode<E> header;
	private Comparator<E> cmp;
	
	public CircularSortedDoublyLinkedList(Comparator<E> comp) {
		size = 0;
		header = createNode(null);
		header.setNext(header);
		header.setPrev(header);
		cmp = comp;
	}
	
//	Returns iterator based on list instance.
	public Iterator<E> iterator() {
		return new LinkedListIterator(this);
	}

	//Add method using comparator for sorting for instances of given elements.
	public boolean add(E e) {
		
		if(isEmpty()) {
			addAfter(header, e);
			size++;
			return true;
		}
		else {
			DNode<E> current = header.getNext();
			while(current != header) {
				if(cmp.compare(e, current.getElement()) <= 0) {
					addBefore(current, e);
					size++;
					return true;
				}
				
				else if(current.getNext() == header) {					
						addAfter(current, e);
						size++;
						return true;											
				}
				
					current = current.getNext();
			}
		}		
		return false;	
	}

//	Size of current list.
	public int size() {
		return size;
	}

//	Removes the object if any in the list.
	public boolean remove(E obj) {
		
		DNode<E> current = header.getNext();
		while(current != header) {
			if(current.getElement().equals(obj)) {
				current.getNext().setPrev(current.getPrev());
				current.getPrev().setNext(current.getNext());
				current.clean();
				size--;
				return true;
			}
			else
				current = current.getNext();
		}
		return false;
	}

//	Removes the object at the given index from the list.
	public boolean remove(int index) {
		
		if(isEmpty() || index >= size || index < 0)
			return false;
		
		if(size == 1) {
			DNode<E> current = header.getNext();
			header.setNext(header);
			header.setPrev(header);
			current.clean();
		}
		else {
			int toRemove = 0;
			DNode<E> ntr = header.getNext();
			while(toRemove++ != index)
				ntr = ntr.getNext();
			DNode<E> before = ntr.getPrev();
			DNode<E> after = ntr.getNext();
			before.setNext(after);
			after.setPrev(before);
			ntr.clean();
		}
		size--;
		return true;
	}

//	Removes all objects that are equal to given object.
	public int removeAll(E obj) {
		int count = 0;
		if(!isEmpty()) {
			
			Iterator<E> iter = iterator();
			while(iter.hasNext()) {
				if(iter.next().equals(obj)) {
					iter.remove();
					count++;
				}
			}
		}
		return count;
	}

//	Returns the first object from the list or null if list is empty.
	public E first() {
		if(isEmpty())
			return null;
		return header.getNext().getElement();
	}

//	Returns the last object from the list or null if list is empty.
	public E last() {
		if(isEmpty())
			return null;
		return header.getPrev().getElement();
	}

//	Returns the object at the given index or null if index is invalid.
	public E get(int index) {		

		if(!isEmpty()) {
			int count = 0;
			for(E item: this) {
				if(index == count)
					return item;
				count++;
			}
		}
		return null;
	}

//	Deletes all objects from the list.
	public void clear() {
		while(!isEmpty())
			remove(0);
	}

//	Return true if the list contains the given object.
	public boolean contains(E e) {
		
		for(E item: this)
			if(item.equals(e))
				return true;		
		return false;
	}

//	True if list is empty.
	public boolean isEmpty() {
		return size == 0;
	}

//	Returns the index of the first object found in the list and returns -1 if list does not contain any. 
	public int firstIndex(E e) {
		if(!isEmpty()) {
			int count = 0;
			for(E item: this) {
				if(item.equals(e))
					return count;
				count++;
			}
		}
		return -1;
	}

//	Returns the index of the last object found in the list and returns -1 if list does not contain any. 
	public int lastIndex(E e) {
		if(!isEmpty()) {
			DNode<E> lastNode = header.getPrev();
			int count = size() - 1;
			while(lastNode != header) {
				if(lastNode.getElement().equals(e))
					return count;
				else {
					lastNode = lastNode.getPrev();
					count--;
				}
			}
		}
		return -1;
	}


//	Private methods for managing the list.
	private void addBefore(DNode<E> targetNode, E e) {
		DNode<E> newNode = createNode(e, targetNode.getPrev(), targetNode);
		targetNode.getPrev().setNext(newNode);
		targetNode.setPrev(newNode);
	}
	
	private void addAfter(DNode<E> targetNode, E e) {
		addBefore(targetNode.getNext(), e);
	}	
	
	
	private DNode<E> createNode(E e, DNode<E> prev, DNode<E> next){
		return new DNode<E>(e, prev, next);
	}
	
	private DNode<E> createNode(E e){
		return new DNode<E>(e);
	}
	
	
///////////////////////////////////////////////////////////////////////
//Private iterator class for use with list
	private class LinkedListIterator implements Iterator<E>{

		private boolean calledNext;
		private int currentIndex;
		private DNode<E> currentNode;
		private CircularSortedDoublyLinkedList<E> currentList; 
		
		public LinkedListIterator(CircularSortedDoublyLinkedList<E> currentL) {
			calledNext = false;
			currentNode = header;
			currentIndex = -1;
			currentList = currentL;
		}
		
//		Returns true if list has more elements.
		public boolean hasNext() {
			return currentIndex < size() - 1;
		}
	
//		Returns the next element of the list or throws if there are none.
		public E next() throws NoSuchElementException{
			if(!hasNext())
				throw new NoSuchElementException("Iterator has no more following elements");
			currentNode = currentNode.getNext();
			currentIndex++;
			calledNext = true;
			return currentNode.getElement();
		}
		
//		Deletes the current element in the list or throws exception is next() was not called.
		public void remove() throws IllegalStateException  {
			if(!calledNext)
				throw new IllegalStateException("Method: 'next()' was not called.");
			currentNode = currentNode.getPrev();
			currentList.remove(currentIndex--);
			calledNext = false;
		}	
	}	
	
////////////////////////////////////////////////////////////////////////////
//Private Double Node Class for List
	private class DNode<T>{
		
		private DNode<T> next;
		private DNode<T> prev;
		private T element;
		
		public DNode(T t, DNode<T> prev, DNode<T> next) {
			this.next = next;
			this.prev = prev;
			this.element = t;
		}
		
		public  DNode(T t) {
			this(t, null, null);
		}
		
		public T getElement() {
			return element;
		}
		
		public DNode<T> getPrev(){
			return prev;
		}
		
		public DNode<T> getNext(){
			return next;
		}		
				
		public void setNext(DNode<T> newNext) {
			next = newNext;
		}
		
		public void setPrev(DNode<T> newPrev) {
			prev = newPrev;
		}
		
		public void clean() {
			element = null;
			next = null;
			prev =null;
		}
		
	}

}
