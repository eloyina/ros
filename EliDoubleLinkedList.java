/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class EliDoubleLinkedList {

	public boolean orientation;

	public EliDoubleLinkedListNode first;
	public EliDoubleLinkedListNode last;

	// true vertical. false horizontal
	public EliDoubleLinkedList() {
		first = null;
		last = null;
		orientation = false;
	}

	public EliDoubleLinkedList(boolean orientation) {
		first = null;
		last = null;
		this.orientation = orientation;
	}

	public EliDoubleLinkedListNode putAtHead(EliSparseMatrixDataNode nodoMatriz) {		
		EliDoubleLinkedListNode node = new EliDoubleLinkedListNode(nodoMatriz,orientation);
		if (first==null){
			first = last = node;
		}
		else{
			first.setPrevious(node);
			node.setNext(first);
			first = node;
		}
		return node;
	}

	public EliDoubleLinkedListNode putAtTail(EliSparseMatrixDataNode nodoMatriz) {
		EliDoubleLinkedListNode node = new EliDoubleLinkedListNode(nodoMatriz,orientation);
		if (last==null){
			first = last = node;
		}
		else{
			last.setNext(node);
			node.setPrevious(last);
			last = node;
		}
		return node;
	}

}