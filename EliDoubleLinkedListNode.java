/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
public class EliDoubleLinkedListNode {
    
	private  boolean orientation;
    
    private EliDoubleLinkedListNode previous = null;
    private EliDoubleLinkedListNode next = null;
    
    private EliSparseMatrixDataNode nodoInicial = null;
    
    // true vertical. false horizontal
    public EliDoubleLinkedListNode(EliSparseMatrixDataNode nodoMatriz, boolean orientation) {
        this.orientation = orientation;
        this.nodoInicial = nodoMatriz;
    }
    
    public void insertandoEnColumna(EliSparseMatrixDataNode  actual, int x,int y,EliSparseMatrixDataNode subMatriz){
    	EliSparseMatrixDataNode nodoSiguiente = actual.getBottom();
    	int dondeIr = nodoSiguiente.isInColumns(x, y);
		switch (dondeIr){
		case 1: //està arriba hay que insertarlo porque no esta referenciado
			subMatriz.setTop(actual);
			subMatriz.setBottom(nodoSiguiente);
			actual.setBottom(subMatriz);
			nodoSiguiente.setTop(subMatriz);
			break;
		case 2: //està abajo 
			insertandoEnColumna(nodoSiguiente,x,y,subMatriz);
		}
    }

    public void insertandoEnFila(EliSparseMatrixDataNode  actual, int x,int y,EliSparseMatrixDataNode subMatriz){
    	EliSparseMatrixDataNode nodoSiguiente = actual.getRight();
    	int dondeIr = nodoSiguiente.isInRows(x, y);
		switch (dondeIr){
		case 3: //està izquierda hay que insertarlo porque no esta referenciado
			subMatriz.setLeft(actual);
			subMatriz.setRight(nodoSiguiente);
			actual.setRight(subMatriz);
			nodoSiguiente.setLeft(subMatriz);
			break;
		case 4: //està derecha
			insertandoEnFila(nodoSiguiente,x,y,subMatriz);
		}
    }

	public void insertarEnColumna(int x,int y,EliSparseMatrixDataNode subMatriz){
    	int dondeIr = nodoInicial.isInColumns(x, y);
		switch (dondeIr){
		case 1: //està arriba hay que insertarlo porque no esta referenciado
			subMatriz.setBottom(nodoInicial);
			nodoInicial.setTop(subMatriz);
			nodoInicial = subMatriz;
			break;
		case 2: //està abajo 
			insertandoEnColumna(nodoInicial,x,y,subMatriz);

		}
	}
    	
	public void insertarEnFila(int x,int y,EliSparseMatrixDataNode subMatriz){
    	int dondeIr = nodoInicial.isInRows(x, y);
		switch (dondeIr){
		case 3: //està izquierda hay que insertarlo porque no esta referenciado
			subMatriz.setRight(nodoInicial);
			nodoInicial.setLeft(subMatriz);
			nodoInicial = subMatriz;
			break;
		case 4: //està derecha 
			insertandoEnFila(nodoInicial,x,y,subMatriz);

		}
	}
    	
	    	
    public void setNext(EliDoubleLinkedListNode nodo){
    	next = nodo;
    }
    
    public void setPrevious(EliDoubleLinkedListNode nodo){
    	previous = nodo;
    }
    
    public EliSparseMatrixDataNode getDataNode(){
            return nodoInicial;
    }

    public EliDoubleLinkedListNode getNext(){
    	return next;
    }
    
    public EliDoubleLinkedListNode getPrevious(){
    	return previous;
    }
    
    
    
   public boolean isHead(EliSparseMatrixDataNode node){
        return orientation ? (node.top == null) : (node.left == null);
    }
    
    public boolean isTail(EliSparseMatrixDataNode node){
        return orientation ? (node.bottom == null) : node.right == null;
    }
    
}