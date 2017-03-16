/*
 * Projet Darwini - Étude Pratique
 * 
 * Development of an IA based on genetic algorithms and neural networks.
 *
 * class Matrix.java
 */

package model.perceptron;

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


	/*	----- CONSTRUCTOR -----	*/

		/**
		 * The matrix's constructor
		 *
		 * @param numColumns Matrix's number of columns
		 * @param numRows Matrix's number of rows
		 *
		 * @see Matrix
		 */
		public Matrix(int numRows, int numColumns) {
			matrix = new double[numRows][numColumns];
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
			
			double value;
			for (int k = 0; k < m2.matrix.length; k++) 
				for (int i = 0; i < matrix.length; i++) {
					value = matrix[i][k];
					for (int j = 0; j < m2.matrix[0].length; j++)
						  res.matrix[i][j] += value * m2.matrix[k][j];
				}
	                	
			return res;
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
			for (int i = 0; i < matrix.length; i++)
				for (int j = 0; j < matrix[0].length; j++)
					sb.append(String.format(Locale.US, "%.6f", matrix[i][j])).append(" ");

			return sb.toString();
		}

}