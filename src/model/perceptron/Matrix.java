/*
 * Projet Darwini - Étude Pratique
 * 
 * Development of an IA based on genetic algorithms and neural networks.
 *
 * class Matrix.java
 */

package model.perceptron;

import model.genetic.NaturalSelection;

import java.util.Locale;

/**
 * <p>
 *     Implementation of a matrix system which will be used in the neural network representation and training
 * </p>
 *
 * @version 1.0 - 17/11/15
 * @author BOIZUMAULT Romain
 * @author BUSSENEAU Alexis
 * @author GEFFRAULT Luc
 * @author MATHIEU Vianney
 * @author VAILLAND Guillaume
 *
 * @version 1.1 - 28/03/17
 * @author Beaulieu Simon
 * @author Goubet Martin
 * @author Estevany Raphael
 * @author Serano Edgar
 */

public class Matrix {
	
	/*	----- ATTRIBUTE -----	*/

		/**
		 * The matrix table
		 * <p>
		 * Principle Matrix's features which represent the matrix by a 2D table
		 * </p>
		 *
		 * @see Matrix#Matrix(int, int)
		 *
		 */
		private double[][] matrix;

		private int numRows;
		private int numColumns;

		private int getRows(){return numRows;}
		private int getColumns(){return numColumns;}
		
		
		
		
	/*	----- CONSTRUCTOR -----	*/

		/**
		 * The matrix's constructor
		 *
		 * @param columns Matrix's number of columns
		 * @param rows Matrix's number of rows
		 *
		 * @see Matrix
		 */
		public Matrix(int rows, int columns) {
			matrix = new double[rows][columns];
			this.numRows = rows;
			this.numColumns = columns;
		}
		
	
	/*	----- MUTATORS -----	*/
		
		/**
		 * The procedure to set the value of a matrix's compartment.
		 *
		 * @param numRows the compartement's row number that we want to reach
		 * @param numColumns the compartement's column number that we want to reach
		 * @param value the value to set
		 */
		public void set(int numRows, int numColumns, double value) {
			matrix[numRows][numColumns] = value;
		}
		
		/**
		 * Multiplication of two matrix
		 *
		 * @param m2 the matrix B in the matrix's multiplication A * B
		 *
		 * @return the matrix's multiplication's result
		 */
		public Matrix mult(Matrix m2) {
			Matrix res = new Matrix(matrix.length, m2.matrix[0].length);
			
			if(m2.getRows() != this.numColumns){return null;}
			//checks if multiplication is possible, avoids possible memory issues
			
			double value;
			for (int k = 0; k < m2.matrix.length; k++) 
				for (int i = 0; i < matrix.length; i++) {
					value = matrix[i][k];
					for (int j = 0; j < m2.matrix[0].length; j++)
						  res.matrix[i][j] += value * m2.matrix[k][j];
				}
	                	
			return res;
		}

		public boolean plus(Matrix m) throws IllegalArgumentException {
			if((this.getColumnCount()!=m.getColumnCount()) || (this.getRowCount() != m.getRowCount())){
				throw new IllegalArgumentException("Error: matrices do not have matching dimensions.");
			}

			//sums the contents of the input matrix m into this

			for(int i=0; i<this.getRowCount();i++){
				for(int j=0; j<this.getColumnCount();j++){
					this.set(i,j,this.get(i,j) + m.get(i,j) );
				}
			}
			return true;
		}

		/**
		 *
		 *	Crosses two matrices to create a new one. For each matrix value, it chooses the value of one of the two input matrices according to the given probability;
		 *
		 * @return the new matrix
		 */
		public Matrix cross(Matrix m, double probability) throws IllegalArgumentException {

			//checks if matrices have the same dimensions
			if((m.getColumnCount() != this.getColumnCount())||(m.getRowCount() != this.getRowCount()))
			{
				throw new IllegalArgumentException("Error: Cannot cross matrices with different dimensions.");
			}

			double random;
			Matrix c = new Matrix(this.getRowCount(), this.getColumnCount());

			for (int i = 0; i < this.getRowCount(); i++) {
				for (int j = 0; j < this.getColumnCount(); j++) {
					random = Math.random();
					if (random <= probability) { // If we're applying a cross to this weight
						c.set(i, j, this.get(i, j));
						c.set(i, j, m.get(i, j));
					}
				}
			}
			return c;
		}


	/*	----- ACCESSORS -----	*/
		
		/**
		 * Accessor to a value contains in the matrix thanks to its row's number and column's number
		 *
		 * @param numRows the row's number of the value you want
		 * @param numColumns the column's number of the value you want
		 *
		 * @return the value contains in matrix[numRows][numColumns]
		 */
		public double get(int numRows, int numColumns) {
			return matrix[numRows][numColumns];
		}
		
		/**
		 * Count the number of rows
		 *
		 * @return the rows' number of th matrix
		 */
		public int getRowCount() {
			return matrix.length;
		}
		
		/**
		 * Count the number of columns
		 *
		 * @return the columns' number of the matrix
		 */
		public int getColumnCount() {
			return matrix[0].length;
		}


	/*	----- OTHER METHODS -----	*/

		/**
		 * Print a matrix
		 *
		 * @return the printed matrix
		 */
		public String toString() {
			StringBuilder sb = new StringBuilder(matrix.length * matrix[0].length);
			for (int i = 0; i < matrix.length; i++) {

				for (int j = 0; j < matrix[0].length; j++) {
					sb.append(String.format(Locale.US, "%.2f", matrix[i][j])).append(" ");
				}
			}
			return sb.toString();
		}

	/**
	 * Print a matrix
	 *
	 * @return the printed matrix
	 */
	public String toDebug() {
		StringBuilder sb = new StringBuilder(matrix.length * matrix[0].length);
		for (int i = 0; i < matrix.length; i++) {

			for (int j = 0; j < matrix[0].length; j++) {
				sb.append(String.format(Locale.US, "%.2f", matrix[i][j])).append(" ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

}