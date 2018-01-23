/*
 * Projet Darwini - Étude Pratique
 * 
 * Development of an IA based on genetic algorithms and neural networks.
 *
 * class OutputData.java
 */

package model.perceptron;

/**
 * <p>
 *	Object which contains the neural network result. Every parameters incarnates a decision's value.
 *	Those values will be tested in the darwini execution. If the the value is superior to a predefined threshold
 *	then Darwini will take this decision.
 * </p>
 *
 * @see NeuralNetwork
 * @see InputData
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

public class OutputData {
	
	/*	----- ATTRIBUTES -----	*/
	
		/**
		 * <p>
		 *  	Number of output Neurons of the neural network
		 * </p>
		 */
		protected static final int OUTPUT_NEURONS = 6;
		
		/**
		 * <p>
		 * 		First output neuron
		 * 		The shooting decision
		 * </p>
		 */
		private double shoot;
		
		/**
		 * <p>
		 * 		Second output neuron
		 * 		The turn right decision
		 * </p>
		 */
		private double turnRight;
		
		/**
		 * <p>
		 * 		Third output neuron
		 * 		The turn left decision
		 * </p>
		 */
		private double turnLeft;
		
		/**
		 * <p>
		 * 		Fourth output neuron
		 * 		The gun turn right decision
		 * </p>
		 */
		private double turnGunRight;
		
		/**
		 * <p>
		 * 		Fifth ouput neuron
		 * 		The gun turn left decision
		 * </p>
		 */
		private double turnGunLeft;
		
		/**
		 * <p>
		 * 		Sixth output neuron
		 * 		The move ahead decision
		 * </p>
		 */
		private double moveAhead;


	/*	----- CONSTRUCTOR -----	*/
		
		/**
		 * The OutputData constructor
		 *
		 * @param results The neural network output matrix
		 */
		public OutputData(Matrix results) {
			shoot = results.get(0, 0);
			turnRight = results.get(0, 1);
			turnLeft = results.get(0, 2);
			turnGunRight = results.get(0, 3);
			turnGunLeft = results.get(0, 4);
			moveAhead = results.get(0, 5);
		}
		
	
	/*	----- ACCESSORS -----	*/
		
		/**
		 * @return The shoot decision
		 */
		public double getShoot() {
			return shoot;
		}
		
		/**
		 * @return The turn right decision
		 */
		public double getTurnRight() {
			return turnRight;
		}
		
		/**
		 * @return The turn left decision
		 */
		public double getTurnLeft() {
			return turnLeft;
		}
		
		/**
		 * @return The gun turn right decision
		 */
		public double getTurnGunRight() {
			return turnGunRight;
		}
		
		/**
		 * @return The gun turn left decision
		 */
		public double getTurnGunLeft() {
			return turnGunLeft;
		}
		
		/**
		 * @return The move ahead decision
		 */
		public double getMoveAhead() {
			return moveAhead;
		}
		
}
