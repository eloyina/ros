import java.io.BufferedWriter;
import java.lang.Object;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/*
 *guardar en archivos que vayan leyendo y en esa posicion guarde la matriz 
 *  
 */

public class EliSparseMatrixDataNode {

	//parte de la estructura en si
	public int sizeMat;
	public double[][] C3;
	public int x = 0; 
	public int y = 0; 
	public EliSparseMatrixDataNode top = null;
	public EliSparseMatrixDataNode right = null;
	public EliSparseMatrixDataNode bottom = null;
	public EliSparseMatrixDataNode left = null;

	/**
	 * this values from the distance between the robot middle point are in cm
	 * converted to meters
	 ***/


	public EliSparseMatrixDataNode(int sM,int x_,int y_) {
		x = x_;
		y = y_;
		sizeMat = sM;
		C3 = new double[sizeMat][sizeMat]; //la sub matriz
		top = null;
		right = null;
		bottom = null;
		left = null;

	}
	
	public int isInRows(int x_, int y_){
		if ((x_>=x && x_<x+sizeMat) && (y_>=y && y_<y+sizeMat)){
			return 0; //si està aqui
		}
		else if (x_<x){
			return 1; //està arriba
		}
		else  if (x_>=x){
			return 2; //està abajo
		}
		else{
			return 5; //esta en esta fila
		}
	}

	public int isInColumns(int x_, int y_){
		if ((x_>=x && x_<x+sizeMat) && (y_>=y && y_<y+sizeMat)){
			return 0; //si està aqui
		}
		else if (y_<y){
			return 3; //està izquierda
		}
		else if (y_>=y){
			return 4; //està derecha
		}
		else{
			return 5; //esta en esta columna
		}
	}

	public boolean set(int x_, int y_, double value) {
		if ((x_>=x && x_<x+sizeMat) && (y_>=y && y_<y+sizeMat)){
			this.C3[x_-x][y_-y] = value;
			return true;
		}
		return false;
	}

	public double get(int x_, int y_) {
		if ((x_>=x && x_<x+sizeMat) && (y_>=y && y_<y+sizeMat)){
			return C3[x_-x][y_-y];
		}
		return -999999;
	}
	
	public EliSparseMatrixDataNode getTop(){
		return top;
	}

	public EliSparseMatrixDataNode getRight(){
		return right;
	}
	
	public EliSparseMatrixDataNode getBottom(){
		return bottom;
	}
	
	public EliSparseMatrixDataNode getLeft(){
		return left;
	}

	public void setTop(EliSparseMatrixDataNode top_){
		top = top_;
		return;
	}

	public void setRight(EliSparseMatrixDataNode right_){
		right = right_;
		return;
	}

	public void setBottom(EliSparseMatrixDataNode bottom_){
		bottom = bottom_;
		return;
	}

	public void setLeft(EliSparseMatrixDataNode left_){
		left = left_;
		return;
	}

	
	@SuppressWarnings("static-access")
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////UNION DE LOS TRES SENSORES EN UNA ////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////////////
	


	// aqui recibimos las variables anteiores para que cambien
	public static void main(String[] args) throws IOException,
			InterruptedException {
	//actualizar();
	//	double[][] CE = new double[ROWS][COLS];
		//	for (int i = 0; i <COLS; i++) {
			//	for (int j = 0; j < ROWS; j++) {
			//		CE[i][j]=3.0;
			//	}}
			
	 

		 //values(rear_posicion_trasera_x());
		//no_values(rear_posicion_trasera_x());
	//	movimientos(values(rear_posicion_trasera_x()));
	}

}