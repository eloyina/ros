/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("unused")
public class EliImageViewer extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JLabel viewer;

	private EliSparseMatrix sparseMatrix;

	public Point currentQuadrant;

	public Point maxQuadrant;

	public EliImageViewer() throws HeadlessException {

		sparseMatrix = new EliSparseMatrix();
		currentQuadrant = new Point(0, 0);
		maxQuadrant = new Point(0, 0);

	}

	public void insertLEFT(double[][] y1) {
		int g = currentQuadrant.x;
		int g1 = currentQuadrant.y;
		int nextX = 0;
		if (currentQuadrant.x < 1)
			nextX = currentQuadrant.x - 1;
		else
			nextX = currentQuadrant.x - 1;
    	System.out.print("f"+currentQuadrant.x+"ee"+currentQuadrant.y+"feniz");

		//.add(y1,g,g1);
		
		currentQuadrant.x = nextX;
		g = nextX;

		if (nextX > maxQuadrant.x)
			maxQuadrant.x = nextX;

		System.out.print(g + " x " + g1 + " y ");
		System.out.print("este es el siguiente de izquierda:" + nextX);

		System.out.print("\n");

	}

	public void insertRIGHT(double[][] m5) {
		int g = currentQuadrant.x;
		int g1 = currentQuadrant.y;
		int nextX = currentQuadrant.x + 1;

		if (currentQuadrant.x < 1)
			nextX = currentQuadrant.x + 1;
		else
			nextX = currentQuadrant.x + 1;

		//sparseMatrix.add(m5,g,g1);
		currentQuadrant.x = nextX;
		g = nextX;

		if (nextX > maxQuadrant.x)
			maxQuadrant.x = nextX;
		System.out.print(g + " x " + g1 + " y ");
		System.out.print("este es el siguiente de derecho:" + nextX);
		System.out.print("\n");

	}

	public void insertUP(double[][] m3) {
		int g = currentQuadrant.x;
		int g1 = currentQuadrant.y;
		int nextY;

		if (currentQuadrant.y < 1)
			nextY = currentQuadrant.y - 1;
		else
			nextY = currentQuadrant.y + 1;
	//.add(m3,g,g1);
		currentQuadrant.y = nextY;
		g1 = nextY;
		if (nextY > maxQuadrant.y)
			maxQuadrant.y = nextY;

		System.out.print(g + " x " + g1 + "y");
		System.out.print("este es el siguiente de arriba:" + nextY);
		System.out.print("\n");
	}

	public void insertDOWN(double[][] y1) {
		int g = currentQuadrant.x;
		int g1 = currentQuadrant.y;
		int nextY;

		if (currentQuadrant.y < 1)
			nextY = currentQuadrant.y - 1;
		else
			nextY = currentQuadrant.y - 1;

//.add(y1,g,g1);
		currentQuadrant.y = nextY;
		g1 = nextY;
		if (nextY > maxQuadrant.y)
			maxQuadrant.y = nextY;
		System.out.print(g + " x " + g1 + " y ");
		System.out.print("este es el siguiente de abajo:" + nextY);
		System.out.print("\n");

	}

}