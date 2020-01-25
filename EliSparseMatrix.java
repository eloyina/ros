import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.imageio.ImageIO;
import javax.swing.text.html.HTMLDocument.Iterator;

@SuppressWarnings("unused")
public class EliSparseMatrix {
	//datos de apoyo
	static final int size_Mat =30;//100;// 1980; //300 //80 //
	static final int size_Mat_Lecturas =1000;//500;// 1980;
	static final int value_gap = 350; // 0.02;

	static final double deltha_prime = Math.toRadians(1);
	static final double deltha_h = Math.toRadians(1);
	static final double value_dis = 0.09; // 0.20 cm se dio el valor de
	static double Hip_rear_komodo = 0.243;// a metros 24.3
	static double Hip_right_komodo = 0.137; // distance between ancho_komodo/2
	static double Hip_left_komodo = 0.137;
	public static double h_anterior_re;
	public static double h_anterior_ri;
	public static double h_anterior_le;
	public static int yy_anterior;
	public static int xx_anterior;
	public static double alpha=0.1;

	public static double Epsilon= 9; 
	//la matriz en si
	public static EliDoubleLinkedList rows;
	public static EliDoubleLinkedList columns;
	public static EliDoubleLinkedListNode baseRows = null;
	public static EliDoubleLinkedListNode baseColumns = null;
	static int maximo_x = 0;
	static int maximo_y = 0;
	static int minimo_x = 0;
	static int minimo_y = 0;
	static int cantidad_x = 0;
	static int cantidad_y = 0;
	
	public static Quaternions quaternion = new Quaternions(cantidad_x, cantidad_x, cantidad_x, cantidad_x);
	
	public EliSparseMatrix() {
		rows = new EliDoubleLinkedList(false);
		columns = new EliDoubleLinkedList(true);
	}
	public static void writeFile(String filename, String textLine)
			throws IOException {
		File fout = new File(filename);
		FileOutputStream fos = new FileOutputStream(fout, true);

		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

		bw.write(textLine);
		bw.newLine();

		bw.close();
	}

	public static Vector<Pair> rear_odometrico(){
		
		String sequence1 = "%s";
		ReaderIMUOdom lecture = new ReaderIMUOdom();
		Vector<Pair> vector = new Vector<Pair>(3);
		int x_rear = 0;
		int y_rear = 0;
		int xx = 0;
		int r;
		int yy = 0;
		double h_rear = 0;
		double h_right=0;
		double h_left=0;
		try {
			if (true) {
				lecture.Load("komodo_1.odom_pub");
				Double x = lecture.GetDouble("pose.pose.position.x");
				Double y = lecture.GetDouble("pose.pose.position.y");
				 x_rear = (int)((x/value_dis)+value_gap);
				 y_rear = (int)((y/value_dis)+value_gap);				
		}

	       Pair respuesta = null;
     		respuesta = new Pair( x_rear,y_rear, respuesta);
     		
			vector.add(respuesta);
		}

		catch (Exception e) {
			e.printStackTrace();
		}

	
		return vector;
	}

	public static float len(Double x,Double y,Double z, Double w) {
		return (float) Math.sqrt(x * x + y * y + z * z + w * w);
	}

	public static Vector<Pair> rear_posicion_trasera_x(){
		
		String sequence1 = "%s";
		ReaderIMUOdom lecture = new ReaderIMUOdom();
		ReaderURF reader_rear = new ReaderURF();
		ReaderURF reader_right = new ReaderURF();
		ReaderURF reader_left = new ReaderURF();
		double C3[][] = new double[size_Mat_Lecturas][size_Mat_Lecturas];
		Vector<Pair> vector = new Vector<Pair>(3);
		//File file = new File("/home/Documents/lectura_orientacion.txt");
		int xx = 0;
		int r;
		int yy = 0;
		double h_rear = 0;
		double h_right=0;
		double h_left=0;
		try {

			if (true) {
				lecture.Load("komodo_1.odom_pub");
				reader_rear.Load("/komodo_1/Rangers/Rear_URF");
				reader_right.Load("/komodo_1/Rangers/Right_URF");
				reader_left.Load("/komodo_1/Rangers/Left_URF");

				h_rear = reader_rear.GetDouble("range");
				h_right = reader_right.GetDouble("range");
				h_left=reader_left.GetDouble("range");
				double xxg=0;
				double yyg=0;
				Double sequence = lecture.GetDouble("header.seq");
				Double x1_ =(lecture.GetDouble("pose.pose.orientation.x"));
				Double y1_ =(lecture.GetDouble("pose.pose.orientation.y"));
				Double z1_ =(lecture.GetDouble("pose.pose.orientation.z"));
				Double w1_ =(lecture.GetDouble("pose.pose.orientation.w"));
				
				float x1 = (float) (x1_/len(x1_,y1_,z1_,w1_)) ;
				float y1 = (float)(y1_/len(x1_,y1_,z1_,w1_));
				float z1 = (float) (z1_/len(x1_,y1_,z1_,w1_));
				float w1 =(float)(w1_/len(x1_,y1_,z1_,w1_));
				
				//System.out.print(x1+ " "+ y1+ " "+ z1+ " "+w1);
				quaternion = quaternion.set(x1,y1, z1, w1);
				/*writeFile("out.txt",
						String.format("valores puros"+x1_+" "+y1_+" "+z1_+" "+w1_+" " ));
				writeFile("out.txt",
						String.format("valores"+x1+" "+y1+" "+z1+" "+w1+" " ));
		*/
				Double x = lecture.GetDouble("pose.pose.position.x");
				Double y = lecture.GetDouble("pose.pose.position.y");
				float theta_prime =  quaternion.getAngleRad();
				
				System.out.print("angulo de orientacion   "  +(theta_prime));
				System.out.print("\n");
			    Double x_rear = x
						+ (Math.cos(
								(Math.toRadians(180) + (theta_prime)))
								% Math.toRadians(360)) * (Hip_rear_komodo);
				Double y_rear = y
						+ (Math.sin((Math.toRadians(180) + (theta_prime)))
								% Math.toRadians(360)) * (Hip_rear_komodo);
					
				Double x_left = x
						+ (Math.cos((Math.toRadians(270) + theta_prime)
								% Math.toRadians(360)) * (Hip_left_komodo));
				Double y_left = y
						+ (Math.sin((Math.toRadians(270) + theta_prime)
								% Math.toRadians(360)) * (Hip_left_komodo));
				
				Double x_right = x
						+ (Math.cos((Math.toRadians(90) + theta_prime)
								% Math.toRadians(360)) * (Hip_right_komodo));
				Double y_right = y
						+ (Math.sin((Math.toRadians(90) + theta_prime)
								% Math.toRadians(360)) * (Hip_right_komodo));			
				
				if ( true) {
					//Limpiar C3, es decir llenarlo de ceros para trabajar tranquilos
					for (int i=0;i<size_Mat_Lecturas;i++){
						for (int j=0;j>size_Mat_Lecturas;j++){
							C3[i][j] = 0;
						}
					}
					
					double r1 = 0;
					double h_new;
					for (r1 = theta_prime - Math.toRadians(15.0); r1 < (theta_prime)
							+ Math.toRadians(15.0); r1 = r1 + deltha_prime) {
						
					
						for (h_new = 0; h_new < h_rear; h_new = h_new + deltha_h) {
							 xxg = (int) (((x_rear + (Math
									.cos((Math.toRadians(180) + r1)
											% Math.toRadians(360)) * (h_new))) / value_dis));
							xx = (int) (xxg + value_gap);

							yyg = (int) (((y_rear + (Math
									.sin((Math.toRadians(180) + r1)
											% Math.toRadians(360)) * (h_new))) / value_dis));
							yy = (int) (yyg + value_gap);
							if(xx <size_Mat_Lecturas && yy<size_Mat_Lecturas){
							C3[xx][yy] = -1;
							}
							
						}	for (h_new = 0; h_new < h_right; h_new = h_new + deltha_h) {
							
							xxg = (int) (((x_right + (Math.cos((Math
									.toRadians(90) + r1) % Math.toRadians(360)) *
										(h_new))) / value_dis));

							xx = (int) (xxg + value_gap);

							yyg = (int) (((y_right + (Math.sin((Math
									.toRadians(90) + r1) % Math.toRadians(360)) *
										(h_new))) / value_dis));
							
							yy = (int) (yyg + value_gap);
							if(xx <size_Mat_Lecturas && yy<size_Mat_Lecturas){
							  C3[xx][yy] = -1;
							}
						}
						
						for (h_new = 0; h_new < h_left; h_new = h_new + deltha_h) {
							
							 xxg = (int) (((x_left + (Math
									.cos((Math.toRadians(270) + r1)
											% Math.toRadians(360)) * (h_new))) / value_dis));
							xx = (int) (xxg + value_gap);

							yyg = (int) (((y_left + (Math
									.sin((Math.toRadians(270) + r1)
											% Math.toRadians(360)) * (h_new))) / value_dis));
							yy = (int) (yyg + value_gap);
							if(xx <size_Mat_Lecturas && yy<size_Mat_Lecturas){
								  C3[xx][yy] = -1;
							   }
							}
						
						
						 xxg = (int) (((x_rear + (Math
									.cos((Math.toRadians(180) + r1)
											% Math.toRadians(360)) * (h_rear))) / value_dis));
							xx = (int) (xxg + value_gap);

						yyg = (int) (((y_rear + (Math
									.sin((Math.toRadians(180) + r1)
											% Math.toRadians(360)) * (h_rear))) / value_dis));
						
						yy = (int)(yyg + value_gap);
						if(xx <size_Mat_Lecturas && yy<size_Mat_Lecturas){
							
						   C3[xx][yy] = 1;
						}
						xxg = (int) (((x_right + (Math.cos((Math
								.toRadians(90) + r1) % Math.toRadians(360)) *
									(h_right))) / value_dis));

					xx = (int)  (xxg + value_gap);

					yyg = (int) (((y_right + (Math.sin((Math
								.toRadians(90) + r1) % Math.toRadians(360)) *
									(h_right))) / value_dis));
						
					yy = (int)  (yyg + value_gap);
					if(xx <size_Mat_Lecturas && yy<size_Mat_Lecturas){
						C3[xx][yy] = 1;
					}
					xxg = (int) (((x_left + (Math
								.cos((Math.toRadians(270) + r1)
											% Math.toRadians(360)) * (h_left))) / value_dis));
						xx = (int) (xxg + value_gap);

					yyg = (int) (((y_left + (Math
									.sin((Math.toRadians(270) + r1)
											% Math.toRadians(360)) * (h_left))) / value_dis));
						yy = (int) (yyg + value_gap);							
				
						if(xx <size_Mat_Lecturas && yy<size_Mat_Lecturas){
							C3[xx][yy] = 1;
						}
					
					}

				}
			}
			Pair respuesta = null;
			vector.clear();
			for (int i = 0; i <size_Mat_Lecturas; i++) {
				for (int j = 0; j < size_Mat_Lecturas; j++) {
					if(C3[i][j]==1.00){
					respuesta = new Pair(i+value_gap,j+value_gap, 1.0);
					
					vector.add(respuesta);
						}
					else if(C3[i][j]==-1.00){
						respuesta = new Pair(i+value_gap,j+value_gap, 0.0);
						vector.add(respuesta);
						
					}
					
				}
//				System.out.print( vector_ri);
			}

			
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		Map<String, String> values;
		Set<String> sequences = new HashSet<String>();
		//Prueba respuesta = new Prueba(C3,xx, yy);
		//System.out.print(respuesta.toString());
		return vector;
	}
	
		


	public void insertandoArriba(EliDoubleLinkedListNode nodoActual,int x,int y,EliSparseMatrixDataNode subMatriz){
		EliDoubleLinkedListNode nodoSiguiente = nodoActual.getPrevious();
		int dondeIr;
		if (nodoSiguiente!=null){
			dondeIr = nodoSiguiente.getDataNode().isInRows(x, y); //0=si està aqui;1=està arriba;2=està abajo;3=està izquierda;4=està derecha
			switch (dondeIr){
			case 1: //està arriba
				insertandoArriba(nodoSiguiente,x,y,subMatriz);
				break;
			case 2: //està abajo hay que insertarlo porque no existe
				EliDoubleLinkedListNode nuevo = new EliDoubleLinkedListNode(subMatriz, true);
				nuevo.setPrevious(nodoSiguiente);
				nuevo.setNext(nodoActual);
				nodoSiguiente.setNext(nuevo);
				nodoActual.setPrevious(nuevo);
				cantidad_x++;
				break;
			case 5: //està en esta fila
				nodoSiguiente.insertarEnFila(x,y,subMatriz);
			}
		}
		else{ //insertarlo mas arriba
			EliDoubleLinkedListNode nuevo = new EliDoubleLinkedListNode(subMatriz, true);
			nuevo.setNext(nodoActual);
			nodoActual.setPrevious(nuevo);
			minimo_x = (int)Math.floor((double)x/(double)size_Mat)*size_Mat;
			cantidad_x++;
		}
	}

	public void insertandoAbajo(EliDoubleLinkedListNode nodoActual,int x,int y,EliSparseMatrixDataNode subMatriz){
		EliDoubleLinkedListNode nodoSiguiente = nodoActual.getNext();
		int dondeIr;
		if (nodoSiguiente!=null){
			dondeIr = nodoSiguiente.getDataNode().isInRows(x, y); //0=si està aqui;1=està arriba;2=està abajo;3=està izquierda;4=està derecha
			switch (dondeIr){
			case 1: //està arriba hay que insertarlo porque no existe
				EliDoubleLinkedListNode nuevo = new EliDoubleLinkedListNode(subMatriz, true);
				nuevo.setPrevious(nodoActual);
				nuevo.setNext(nodoSiguiente);
				nodoActual.setNext(nuevo);
				nodoSiguiente.setPrevious(nuevo);
				cantidad_x++;
				break;
			case 2: //està abajo 
				insertandoAbajo(nodoSiguiente,x,y,subMatriz);
				break;
			case 5: //està en esta fila
				nodoSiguiente.insertarEnFila(x,y,subMatriz);
			}
		}
		else{
			EliDoubleLinkedListNode nuevo = new EliDoubleLinkedListNode(subMatriz, true);
			nuevo.setPrevious(nodoActual);
			nodoActual.setNext(nuevo);
			
			maximo_x = ((int)Math.floor((double)x/(double)size_Mat)+1)*size_Mat-1;
			cantidad_x++;
		}
	}

	public void insertandoIzquierda(EliDoubleLinkedListNode nodoActual,int x,int y,EliSparseMatrixDataNode subMatriz){
		EliDoubleLinkedListNode nodoSiguiente = nodoActual.getPrevious();
		int dondeIr;
		if (nodoSiguiente!=null){
			dondeIr = nodoSiguiente.getDataNode().isInColumns(x, y); //0=si està aqui;1=està arriba;2=està abajo;3=està izquierda;4=està derecha
			switch (dondeIr){
			case 3: //està izquierda
				insertandoIzquierda(nodoSiguiente,x,y,subMatriz);
				break;
			case 4: //està derecha hay que insertarlo porque no existe
				EliDoubleLinkedListNode nuevo = new EliDoubleLinkedListNode(subMatriz, true);
				nuevo.setPrevious(nodoSiguiente);
				nuevo.setNext(nodoActual);
				nodoSiguiente.setNext(nuevo);
				nodoActual.setPrevious(nuevo);
				cantidad_y++;
				break;
			case 5: //està en esta columna
				nodoSiguiente.insertarEnColumna(x,y,subMatriz);
			}
		}
		else{
			EliDoubleLinkedListNode nuevo = new EliDoubleLinkedListNode(subMatriz, true);
			nuevo.setNext(nodoActual);
			nodoActual.setPrevious(nuevo);
			
			minimo_y = (int)Math.floor((double)y/(double)size_Mat)*size_Mat;
			cantidad_y++;
		}
	}

	public void insertandoDerecha(EliDoubleLinkedListNode nodoActual,int x,int y,EliSparseMatrixDataNode subMatriz){
		EliDoubleLinkedListNode nodoSiguiente = nodoActual.getNext();
		int dondeIr;
		if (nodoSiguiente!=null){
			dondeIr = nodoSiguiente.getDataNode().isInColumns(x, y); //0=si està aqui;1=està arriba;2=està abajo;3=està izquierda;4=està derecha
			switch (dondeIr){
			case 3: //està izquierda hay que insertarlo porque no existe
				EliDoubleLinkedListNode nuevo = new EliDoubleLinkedListNode(subMatriz, true);
				nuevo.setPrevious(nodoActual);
				nuevo.setNext(nodoSiguiente);
				nodoActual.setNext(nuevo);
				nodoSiguiente.setPrevious(nuevo);
				cantidad_y++;
				break;
			case 4: //està derecha
				insertandoIzquierda(nodoSiguiente,x,y,subMatriz);
				break;
			case 5: //està en esta columna
				nodoSiguiente.insertarEnColumna(x,y,subMatriz);
			}
		}
		else{
			EliDoubleLinkedListNode nuevo = new EliDoubleLinkedListNode(subMatriz, true);
			nuevo.setPrevious(nodoActual);
			nodoActual.setNext(nuevo);
			
			maximo_y = ((int)Math.floor((double)y/(double)size_Mat)+1)*size_Mat-1;
			cantidad_y++;
		}
	}

	
	public EliSparseMatrixDataNode buscandoArriba(EliDoubleLinkedListNode nodoActual,int x,int y){
		EliDoubleLinkedListNode nodoSiguiente = nodoActual.getPrevious();
		int dondeIr;
		if (nodoSiguiente != null){
			dondeIr = nodoSiguiente.getDataNode().isInRows(x, y); //0=si està aqui;1=està arriba;2=està abajo;3=està izquierda;4=està derecha
			switch (dondeIr){
			case 0: //està en ese nodo
				return nodoSiguiente.getDataNode();
			case 1: //està arriba
				return buscandoArriba(nodoSiguiente,x,y);
			case 2: //està abajo hay que insertarlo porque no existe
				EliSparseMatrixDataNode subMatriz = new EliSparseMatrixDataNode(size_Mat,(int)Math.floor((double)x/(double)size_Mat)*size_Mat,(int)Math.floor((double)y/(double)size_Mat)*size_Mat);
				EliDoubleLinkedListNode nuevo = new EliDoubleLinkedListNode(subMatriz, true);
				nuevo.setPrevious(nodoSiguiente);
				nuevo.setNext(nodoActual);
				nodoSiguiente.setNext(nuevo);
				nodoActual.setPrevious(nuevo);
				cantidad_x++;
				//agregar tambien esta subMatriz en Columns
				dondeIr = baseColumns.getDataNode().isInColumns(x, y); //0=si està aqui;1=està arriba;2=està abajo;3=està izquierda;4=està derecha
				switch (dondeIr){
				case 3: //està izquierda
					insertandoIzquierda(baseColumns,x,y,subMatriz);
					break;
				case 4: //està derecha
					insertandoDerecha(baseColumns,x,y,subMatriz);
					break;
				case 5: //està en esta columna
					baseColumns.insertarEnColumna(x,y,subMatriz);
				}
				
				return subMatriz;
			}
		}
		else{ //agregarlo mas arriba
			EliSparseMatrixDataNode subMatriz = new EliSparseMatrixDataNode(size_Mat,(int)Math.floor((double)x/(double)size_Mat)*size_Mat,(int)Math.floor((double)y/(double)size_Mat)*size_Mat);
			EliDoubleLinkedListNode nuevo = new EliDoubleLinkedListNode(subMatriz, true);
			nuevo.setNext(nodoActual);
			nodoActual.setPrevious(nuevo);
			cantidad_x++;
			//agregar tambien esta subMatriz en Columns
			dondeIr = baseColumns.getDataNode().isInColumns(x, y); //0=si està aqui;1=està arriba;2=està abajo;3=està izquierda;4=està derecha
			switch (dondeIr){
			case 3: //està izquierda
				insertandoIzquierda(baseColumns,x,y,subMatriz);
				break;
			case 4: //està derecha
				insertandoDerecha(baseColumns,x,y,subMatriz);
				break;
			case 5: //està en esta columna
				baseColumns.insertarEnColumna(x,y,subMatriz);
			}
			minimo_x = (int)Math.floor((double)x/(double)size_Mat)*size_Mat;
			return subMatriz;
			
		}
		return null;
	}

	public EliSparseMatrixDataNode buscandoAbajo(EliDoubleLinkedListNode nodoActual,int x,int y){
		EliDoubleLinkedListNode nodoSiguiente = nodoActual.getNext();
		int dondeIr;
		if (nodoSiguiente != null){
			dondeIr = nodoSiguiente.getDataNode().isInRows(x, y); //0=si està aqui;1=està arriba;2=està abajo;3=està izquierda;4=està derecha
			switch (dondeIr){
			case 0: //està en ese nodo
				return nodoSiguiente.getDataNode();
			case 1: //està arriba hay que insertarlo porque no existe
				EliSparseMatrixDataNode subMatriz = new EliSparseMatrixDataNode(size_Mat,(int)Math.floor((double)x/(double)size_Mat)*size_Mat,(int)Math.floor((double)y/(double)size_Mat)*size_Mat);
				EliDoubleLinkedListNode nuevo = new EliDoubleLinkedListNode(subMatriz, true);
				nuevo.setPrevious(nodoActual);
				nuevo.setNext(nodoSiguiente);
				nodoSiguiente.setPrevious(nuevo);
				nodoActual.setNext(nuevo);
				cantidad_x++;
				//agregar tambien esta subMatriz en Columns
				dondeIr = baseColumns.getDataNode().isInColumns(x, y); //0=si està aqui;1=està arriba;2=està abajo;3=està izquierda;4=està derecha
				switch (dondeIr){
				case 3: //està izquierda
					insertandoIzquierda(baseColumns,x,y,subMatriz);
					break;
				case 4: //està derecha
					insertandoDerecha(baseColumns,x,y,subMatriz);
					break;
				case 5: //està en esta columna
					baseColumns.insertarEnColumna(x,y,subMatriz);
				}
				return subMatriz;
			case 2: //està abajo
				return buscandoAbajo(nodoSiguiente,x,y);
			}
		}
		else{ //agregarlo mas abajo
			EliSparseMatrixDataNode subMatriz = new EliSparseMatrixDataNode(size_Mat,(int)Math.floor((double)x/(double)size_Mat)*size_Mat,(int)Math.floor((double)y/(double)size_Mat)*size_Mat);
			EliDoubleLinkedListNode nuevo = new EliDoubleLinkedListNode(subMatriz, true);
			nuevo.setPrevious(nodoActual);
			nodoActual.setNext(nuevo);
			cantidad_x++;
			//agregar tambien esta subMatriz en Columns
			dondeIr = baseColumns.getDataNode().isInColumns(x, y); //0=si està aqui;1=està arriba;2=està abajo;3=està izquierda;4=està derecha
			switch (dondeIr){
			case 3: //està izquierda
				insertandoIzquierda(baseColumns,x,y,subMatriz);
				break;
			case 4: //està derecha
				insertandoDerecha(baseColumns,x,y,subMatriz);
				break;
			case 5: //està en esta columna
				baseColumns.insertarEnColumna(x,y,subMatriz);
			}
			maximo_x = ((int)Math.floor((double)x/(double)size_Mat)+1)*size_Mat-1;
			return subMatriz;
			
		}
		return null;
	}

	public EliSparseMatrixDataNode buscandoIzquierda(EliDoubleLinkedListNode nodoActual,int x,int y){
		EliDoubleLinkedListNode nodoSiguiente = nodoActual.getPrevious();
		int dondeIr;
		if (nodoSiguiente != null){
			dondeIr = nodoSiguiente.getDataNode().isInColumns(x, y); //0=si està aqui;1=està arriba;2=està abajo;3=està izquierda;4=està derecha
			switch (dondeIr){
			case 0: //està en ese nodo
				return nodoSiguiente.getDataNode();
			case 3: //està izquierda
				return buscandoIzquierda(nodoSiguiente,x,y);
			case 4: //està derecha hay que insertarlo porque no existe
				EliSparseMatrixDataNode subMatriz = new EliSparseMatrixDataNode(size_Mat,(int)Math.floor((double)x/(double)size_Mat)*size_Mat,(int)Math.floor((double)y/(double)size_Mat)*size_Mat);
				EliDoubleLinkedListNode nuevo = new EliDoubleLinkedListNode(subMatriz, true);
				nuevo.setPrevious(nodoSiguiente);
				nuevo.setNext(nodoActual);
				nodoSiguiente.setNext(nuevo);
				nodoActual.setPrevious(nuevo);
				cantidad_y++;
				//agregar tambien esta subMatriz en Columns
				dondeIr = baseColumns.getDataNode().isInRows(x, y); //0=si està aqui;1=està arriba;2=està abajo;3=està izquierda;4=està derecha
				switch (dondeIr){
				case 1: //està arriba
					insertandoArriba(baseColumns,x,y,subMatriz);
					break;
				case 2: //està abajo
					insertandoAbajo(baseColumns,x,y,subMatriz);
					break;
				case 5: //està en esta fila
					baseRows.insertarEnFila(x,y,subMatriz);
				}
				
				return subMatriz;
			}
		}
		else{ //agregarlo mas a la izquierda
			EliSparseMatrixDataNode subMatriz = new EliSparseMatrixDataNode(size_Mat,(int)Math.floor((double)x/(double)size_Mat)*size_Mat,(int)Math.floor((double)y/(double)size_Mat)*size_Mat);
			EliDoubleLinkedListNode nuevo = new EliDoubleLinkedListNode(subMatriz, true);
			nuevo.setNext(nodoActual);
			nodoActual.setPrevious(nuevo);
			cantidad_y++;
			//agregar tambien esta subMatriz en Columns
			dondeIr = baseColumns.getDataNode().isInRows(x, y); //0=si està aqui;1=està arriba;2=està abajo;3=està izquierda;4=està derecha
			switch (dondeIr){
			case 1: //està arriba
				insertandoArriba(baseColumns,x,y,subMatriz);
				break;
			case 2: //està abajo
				insertandoAbajo(baseColumns,x,y,subMatriz);
				break;
			case 5: //està en esta fila
				baseRows.insertarEnFila(x,y,subMatriz);
			}
			minimo_y = (int)Math.floor((double)y/(double)size_Mat)*size_Mat;
			
			return subMatriz;
			
		}
		return null;
	}
	

	public EliSparseMatrixDataNode buscandoDerecha(EliDoubleLinkedListNode nodoActual,int x,int y){
		EliDoubleLinkedListNode nodoSiguiente = nodoActual.getNext();
		int dondeIr;
		if (nodoSiguiente != null){
			dondeIr = nodoSiguiente.getDataNode().isInColumns(x, y); //0=si està aqui;1=està arriba;2=està abajo;3=està izquierda;4=està derecha
			switch (dondeIr){
			case 0: //està en ese nodo
				return nodoSiguiente.getDataNode();
			case 3: //està izquierda hay que insertarlo porque no existe
				EliSparseMatrixDataNode subMatriz = new EliSparseMatrixDataNode(size_Mat,(int)Math.floor((double)x/(double)size_Mat)*size_Mat,(int)Math.floor((double)y/(double)size_Mat)*size_Mat);
				EliDoubleLinkedListNode nuevo = new EliDoubleLinkedListNode(subMatriz, true);
				nuevo.setPrevious(nodoActual);
				nuevo.setNext(nodoSiguiente);
				nodoSiguiente.setPrevious(nuevo);
				nodoActual.setNext(nuevo);
				cantidad_y++;
				//agregar tambien esta subMatriz en Columns
				dondeIr = baseColumns.getDataNode().isInRows(x, y); //0=si està aqui;1=està arriba;2=està abajo;3=està izquierda;4=està derecha
				switch (dondeIr){
				case 1: //està arriba
					insertandoArriba(baseColumns,x,y,subMatriz);
					break;
				case 2: //està abajo
					insertandoAbajo(baseColumns,x,y,subMatriz);
					break;
				case 5: //està en esta fila
					baseRows.insertarEnFila(x,y,subMatriz);
				}
				return subMatriz;
			case 4: //està derecha
				return buscandoDerecha(nodoSiguiente,x,y);
			}
		}
		else{ //agregarlo mas a la derecha
			EliSparseMatrixDataNode subMatriz = new EliSparseMatrixDataNode(size_Mat,(int)Math.floor((double)x/(double)size_Mat)*size_Mat,(int)Math.floor((double)y/(double)size_Mat)*size_Mat);
			EliDoubleLinkedListNode nuevo = new EliDoubleLinkedListNode(subMatriz, true);
			nuevo.setPrevious(nodoActual);
			nodoActual.setNext(nuevo);
			cantidad_y++;
			//agregar tambien esta subMatriz en Columns
			dondeIr = baseColumns.getDataNode().isInRows(x, y); //0=si està aqui;1=està arriba;2=està abajo;3=està izquierda;4=està derecha
			switch (dondeIr){
			case 1: //està arriba
				insertandoArriba(baseColumns,x,y,subMatriz);
				break;
			case 2: //està abajo
				insertandoAbajo(baseColumns,x,y,subMatriz);
				break;
			case 5: //està en esta fila
				baseRows.insertarEnFila(x,y,subMatriz);
			}
			maximo_y = ((int)Math.floor((double)y/(double)size_Mat)+1)*size_Mat-1;

			return subMatriz;
			
		}
		return null;
	}

	
	public double Set(int x,int y, double value){
		//buscar si existe en mi matriz esparza el x,y
		EliSparseMatrixDataNode subMatriz;
		double actual,resultante=0.0;
		if (baseRows==null){
			subMatriz = new EliSparseMatrixDataNode(size_Mat,0,0);
			baseRows = new EliDoubleLinkedListNode(subMatriz,true);
			baseColumns = new EliDoubleLinkedListNode(subMatriz,false);
			rows.putAtHead(subMatriz);
			columns.putAtHead(subMatriz);
			maximo_x = size_Mat;
			maximo_y = size_Mat;
			minimo_x = 0;
			minimo_y = 0;
			cantidad_x = 1;
			cantidad_y = 1;
		}
		//busquemos donde agregarlo
		if (cantidad_x < cantidad_y) { //busquemos primero en las filas
			int dondeIr = baseRows.getDataNode().isInRows(x, y); //0=si està aqui;1=està arriba;2=està abajo;3=està izquierda;4=està derecha
			switch (dondeIr){
			case 0: //està en ese nodo
				actual = baseRows.getDataNode().get(x, y);
				resultante = actual + alpha*(value-actual);
				baseColumns.getDataNode().set(x, y, resultante);
				break;
			case 1: //està arriba
				subMatriz = buscandoArriba(baseRows,x,y);
				actual = subMatriz.get(x, y);
				resultante = actual + alpha*(value-actual);
				subMatriz.set(x, y, resultante);
				break;
			case 2: //està abajo
				subMatriz = buscandoAbajo(baseRows,x,y);
				actual = subMatriz.get(x, y);
				resultante = actual + alpha*(value-actual);
				subMatriz.set(x, y, resultante);
				break;
			}
		}
		else{//busquemos primero en las columnas
			int dondeIr = baseColumns.getDataNode().isInColumns(x, y); //0=si està aqui;1=està arriba;2=està abajo;3=està izquierda;4=està derecha
			switch (dondeIr){
			case 0: //està en ese nodo
				actual = baseColumns.getDataNode().get(x, y);
				resultante = actual + alpha*(value-actual);
				baseColumns.getDataNode().set(x, y, resultante);
				break;
			case 3: //està izquierda
				subMatriz = buscandoIzquierda(baseColumns,x,y);
				actual = subMatriz.get(x, y);
				resultante = actual + alpha*(value-actual);
				subMatriz.set(x, y, resultante);
				break;
			case 4: //està derecha 
				subMatriz = buscandoDerecha(baseColumns,x,y);
				actual = subMatriz.get(x, y);
				resultante = actual + alpha*(value-actual);
				subMatriz.set(x, y, resultante);
				break;
			}
			
		}
		return resultante;
	}
	
   public static String decimal2Hex(int d) {
        String digits = "0123456789ABCDEF";
        if (d == 0) return "00";
        String hex = "";
        while (d > 0) {
            int digit = d % 16;                // rightmost digit
            hex = digits.charAt(digit) + hex;  // string concatenation
            d = d / 16;
        }
		if (hex.length() == 1){
			hex = "0"+hex;}
		
		else if (hex.length() == 0){
			hex = "00";
			
		}
        
        //System.out.print(hex);
        //System.out.print("\n");
        return hex;
    }

	
	void ActualizarGraficar(Graphics g){

		//Leer sensores, y actualizar la matriz
		Vector<Pair> vector = new Vector<Pair>(3);
		double value = 0.0;
		String colorR,colorG,colorB, color;
		int x,y,x_odometrico,y_odometrico,valorColor;
		vector = rear_posicion_trasera_x();
		for (int i=0;i<vector.size();i++){
			
		//if((double)vector.get(i).third_ >= 0.75){
			x = (int)vector.get(i).first_;
			y = (int)vector.get(i).second_;
			System.out.print(x + " "+y);
			System.out.print("\n");
			value = Set(x,y,(double)vector.get(i).third_);
			valorColor = (int) Math.round(255-255*value);
			colorR=decimal2Hex(valorColor);
			colorG=decimal2Hex(valorColor);
			colorB=decimal2Hex(valorColor);
			color = "0x" + colorR + colorG + colorB;
			g.setColor(Color.decode(color));
			g.drawLine(x,y,x,y);
			System.out.print("\n");
			//}
		}
		System.out.print("Dibuja " + vector.size() + " nuevos puntos" + "\n");
		
		
		
	}

	void ActualizarGraficar_odom(Graphics g){

		Vector<Pair> vector_odomet= new Vector<Pair>(3);
		double value = 0.0;
		int x,y,x_odometrico,y_odometrico,valorColor;
		vector_odomet= rear_odometrico();
		for (int i=0;i<vector_odomet.size();i++){
			x =(int) (vector_odomet.get(i).first_ )+value_gap;
			y =(int) (vector_odomet.get(i).second_)+value_gap;
			g.setColor(Color.red);
			g.drawLine(x,y,x,y);
		}			

		
	}
	

	static void imprimirTamanho(){
		System.out.print("minimo_x =" + minimo_x + " maximo_x =" + maximo_x + " minimo_y =" + minimo_y + " maximo_y =" + maximo_y + "\n");
		
	}
	
	void imprimirTamanhoarchivo() throws IOException{
		writeFile("/home/akson/Descargas/komodo_slam/oute.txt",
				String.format("matriz_tamaño"+" "+cantidad_x+""+ cantidad_y+" "+minimo_x+" "+maximo_x+" "+minimo_y+" "+maximo_y+" " ));
		
	}
	
	public static void main(String[] args) throws IOException {
			imprimirTamanho();
			
		
	      }
		
	}