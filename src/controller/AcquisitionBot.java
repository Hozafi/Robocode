/*
 * Projet Darwini - Étude Pratique
 * 
 * Development of an IA based on genetic algorithms and neural networks.
 *
 * class AcquisitionBot.java
 */

package controller;

import java.io.*;

import robocode.*;

import model.acquisition.Database;
import model.acquisition.AcquisitionData;

/**
 * <p>
 * A robot based on an existing one, however this one is used to collect examples (which are represented by the
 * environment data when the robot does an action and the success of this action as a boolean).
 * Those examples are used in the supervised process only.
 * For now, we only collect examples when the robot is shooting.
 * You can easily add some collected data according to output neurons.
 * Careful to make AcquisitionBot fight against one robot at a time.
 * </p>
 *
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
 * 
 * @version 2.0 - 29/11/17
 * @author Rebout Etienne
 * @author Haus Daniel
 * @author Barroux Etienne
 * @author Gilbert Auriane
 */
public class AcquisitionBot extends InitialRobot {

	/*	----- ATTRIBUTES -----	*/

		/**
		 * <p>
		 * The name of the SSVM file where the examples are saved.
		 * Because of the Robocode security, this file will be save in the project compiler output directory in the
		 * subdirectory "Darwini/controller/AcquisitionBot.data".
		 * </p>
		 */
		private static final String SSVM_FILE = "data.ssvm";


		/**
		 * <p>
		 * 		The list of InputData (examples) of the current round
		 * </p>
		 *
		 * @see Database
		 * @see model.perceptron.InputData
		 */
		private Database knowledges;

		/**
		 * <p>
		 *     The AcquisitionData of the AcquisitionBot
		 * </p>
		 *
		 * @see AcquisitionData
		 */
		private AcquisitionData acquisitionData;
		
	/*	----- CONSTRUCTOR -----	*/
	
		/**
		 * <p>
		 * 		When the robot is first running, it create a new database for the current round and a new AcquisitionData
		 * </p>
		 *
		 * @see Database
		 * @see InitialRobot
		 * @see AcquisitionData
		 */

        @Override
        public void run() {
			knowledges = new Database();
			acquisitionData = new AcquisitionData(this);

			// MUST be call after the initialization (the initial robot can have an infinite loop).
            super.run();
		}
		
		
	/*	----- OTHER METHODS -----	*/

		/**
		 * <p>
		 *     Procedure called when AcquisitionBot has scanned an enemy.
		 *     As we saw in InitialRobot, when this method is called,
		 *     the robot creates a new InputData throw the calling of the acquisition method of AcquisitionData
		 *     and then inserts this InputData in his database knowledges
		 * </p>
		 *
		 * @param e The enemy robot our AcquisitionBot has scanned
		 *
		 * @see model.perceptron.InputData
		 * @see InitialRobot
		 * @see AcquisitionData
		 */
		@Override
		public void onScannedRobot(ScannedRobotEvent e) {
			super.onScannedRobot(e);

			// Get all data required for construct an InputData and save it in a database.
			knowledges.insert( acquisitionData.acquisition(e) );
			System.out.println(knowledges.getLastData().toSSVM());
		}

		/**
		 * <p>
		 *     Procedure called when AcquisitionBot's bullet hits an enemy.
		 *     Because we are just collecting examples for the action "shoot",
		 *     we set the example as a success if the bullet does not miss the enemy robot
		 * </p>
		 *
		 * @param e The enemy robot our AcquisitionBot has scanned
		 *
		 * @see model.perceptron.InputData
		 */
		@Override
		public void onBulletHit(BulletHitEvent e) {
			super.onBulletHit(e);
			// Set a success for the first output neuron in OutputData when the robot hits another robot.
			knowledges.getLastData().setSuccess(0);
		}

		@Override
		public void onBulletMissed(BulletMissedEvent e) {
			super.onBulletMissed(e);
			// Set a success for the first output neuron in OutputData when the robot hits another robot.
			knowledges.getLastData().setSuccess(0);
		}

		/**
		 * <p>
		 *     Procedure called when the round is ended
		 *     We save the database in the SSVM_File
		 * </p>
		 *
		 * @param event the event at the end of the game
		 *
		 * @see model.perceptron.InputData
		 */
		@Override
		public void onRoundEnded(RoundEndedEvent event) {
			super.onRoundEnded(event);

			// At the end of the game, the robot saves all the data collected in a SSVM format file.
			try {
				knowledges.printToSSVM( getDataFile(SSVM_FILE) );
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
}
