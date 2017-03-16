/*
 * Projet Darwini - Étude Pratique
 * 
 * Development of an IA based on genetic algorithms and neural networks.
 *
 * class Database.java
 */

package model.acquisition;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import model.perceptron.InputData;
import robocode.RobocodeFileWriter;

/**
 * <p>
 * The Database object is a database of InputData's.
 * This class is only used in the supervised process.
 * AcquisitionBot uses it to stock its examples before saving it in its SSVM file.
 * </p>
 *
 * @see controller.AcquisitionBot
 * @see InputData
 *
 *
 * @version 1.0 - 17/11/15
 * @author BOIZUMAULT Romain
 * @author BUSSENEAU Alexis
 * @author GEFFRAULT Luc
 * @author MATHIEU Vianney
 * @author VAILLAND Guillaume
*/

public class Database {
	
	/*	----- ATTRIBUTE -----	*/
	
		/**
		 * <p>
		 *     The list of InputData
		 * </p>
		 *
		 * @see InputData
		 */
		private ArrayList<InputData> dataList;
	
	
	/*	----- CONSTRUCTOR -----	*/
	
		/**
		 * <p>
		 *     The Database constructor
		 * </p>
		 */
		public Database() {
			dataList = new ArrayList<>();
		}
	
	
	/*	----- ACCESSORS -----	*/
		
		/**
		 * <p>
		 *     Accessor to the last data pushed in the dataList.
		 *     This method is used to set the success value for this method.
		 * </p>
		 *
		 * @return The last InputData pushed
		 *
		 * @see InputData
		 * @see controller.AcquisitionBot
		 */
		public InputData getLastData() {
			return dataList.get(dataList.size() - 1);
		}
	
	
	/*	----- MUTATORS -----	*/
	
		/**
		 * <p>
		 * 		Insert the specified InputData in the database.
		 * </p>
		 *
		 * @param newInputData the data to insert
		 *
		 * @return true if the data has been inserted, false otherwise
		 *
		 * @throws NullPointerException if newInputData is null
		 *
		 * @see InputData
		 * @see controller.AcquisitionBot
		 */
		public boolean insert(InputData newInputData) throws NullPointerException {
			if (newInputData == null)
				throw new NullPointerException("The new InputData specified is empty.");
	
			return dataList.add(newInputData);
		}
	
		/**
		 * <p>
		 *     Print the database in a SSVM file in the SSVM syntax
		 * </p>
		 *
		 * @param f The target file where the database will be printed
		 *
		 * @throws IOException if a problem happens during the print.
		 *
		 * @see controller.AcquisitionBot
		 */
		public void printToSSVM(File f) throws IOException {
			RobocodeFileWriter writer = new RobocodeFileWriter(f.getAbsolutePath(), true);
			StringBuilder stringBuilder = new StringBuilder();
		
			for (InputData data : dataList)
				stringBuilder.append(data.toSSVM()).append("\n");
	
			writer.write(stringBuilder.toString());
			writer.close();
		}
		
}