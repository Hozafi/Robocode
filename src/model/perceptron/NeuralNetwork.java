/*
 * Projet Darwini - Étude Pratique
 * 
 * Development of an IA based on genetic algorithms and neural networks.
 *
 * class NeuralNetwork.java
 */

package model.perceptron;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/**
 * <p>
 *     This object represent a Neural Network used in the perceptron process.
 *     We load a matrix of environment data (from InputData) and after few calculations the neural network gives us
 *     a matrix of values representing decisions and transformed as an object OutputData.
 *     Those calculations (described further) needs some weighting coefficients which are the parameters of a neural network.
 *     Our job is to define the right weighting coefficient for the neural network to be efficient.
 *     We can set those coefficient thanks to supervised process (AcquitisionBot and iriselm)
 *     or un-supervised process (Genetic algorithm)
 *
 * </p>
 *
 * @see InputData
 * @see OutputData
 *
 * @see controller.AcquisitionBot
 *
 * @see controller.Darwini
 * @see	model.genetic.GeneticAlgorithm
 * @see model.genetic.Score
 * @see model.genetic.NaturalSelection
 *
 * @version 1.0 - 17/11/15
 * @author BOIZUMAULT Romain
 * @author BUSSENEAU Alexis
 * @author GEFFRAULT Luc
 * @author MATHIEU Vianney
 * @author VAILLAND Guillaume
 */
public class NeuralNetwork {
	
	/*	----- ATTRIBUTES -----	*/
	
		/**
		 * <p>
		 *     Number of hidden neuron of the neural network
		 * </p>
		 */
		private static final int HIDDEN_NEURONS = 200;

        /**
		 * <p>
		 *     The first matrix of weighting coefficient
		 *     We'll call it the input weights matrix
		 * </p>
		 */
		private Matrix inputWeights;
		
		/**
		 * <p>
		 *     The second matrix of weighting coefficient
		 *     We'll call it the output weight matrix
		 * </p>
		 */
		private Matrix outputWeights;
	
		/**
		 * <p>
		 *     The bias vector (bias neuron)
		 * </p>
		 */
		private Matrix bias;
	

	/*	----- CONSTRUCTOR -----	*/

		/**
		 * <p>
		 *     The neural network constructor with random weighting coefficient
		 *     (used in the genetic algorithm process)
		 * </p>
		 */
		public NeuralNetwork() {
			inputWeights = new Matrix(InputData.INPUT_NEURONS, HIDDEN_NEURONS);
			randomizeIOMatrix(inputWeights);
			outputWeights = new Matrix(HIDDEN_NEURONS, OutputData.OUTPUT_NEURONS);
			randomizeIOMatrix(outputWeights);
			bias = new Matrix(HIDDEN_NEURONS, 1);
			randomizeBiasMatrix(bias);
		}

		/**
		 * <p>
		 *     The neural network constructor with a file containing the weighting coefficients
		 *     from the supervised process (from iriselm treatment)
		 * </p>
		 * 
		 * @param file The file containing the coefficients
		 */
		public NeuralNetwork(File file) {
			try {
			    // Get an input factory and instantiate a reader
				XMLStreamReader xmlReader = XMLInputFactory.newInstance().createXMLStreamReader( new FileInputStream(file) );

				// Skip the first lines
				for (int i = 0; i < 4; i++)
					xmlReader.nextTag();

				// Get matrix values from the other lines
				inputWeights = initMatrix(xmlReader);
				// Skip the end of the inputWeights
				xmlReader.nextTag();
				xmlReader.nextTag();
				outputWeights = initMatrix(xmlReader);
				// Skip the end of the outputWeights
				xmlReader.nextTag();
				xmlReader.nextTag();
				bias = initMatrix(xmlReader);

				// Close the reader
				xmlReader.close();
			}
			catch (FileNotFoundException e) {
                System.out.println(file.getAbsolutePath() + " is not found.");
                System.out.println("The perceptron is not initialized, please put a perceptron in the correct directory.");
			}
            catch (XMLStreamException e) {
                System.out.println(file.getAbsolutePath() + " is invalid.");
                System.out.println("The perceptron is not initialized, please put a valid perceptron in the directory.");
            }
		}


    /*	----- MUTATORS -----	*/

        /**
         * @return The input weights Matrix
         */
        public Matrix getInputWeights() {
            return inputWeights;
        }

        /**
         * @return The output weights Matrix
         */
        public Matrix getOutputWeights() {
            return outputWeights;
        }

        /**
         * @return The bias vector
         */
        public Matrix getBias() {
            return bias;
        }

		
	/*	----- OTHER METHODS -----	*/
		
		/**
		 * <p>
		 * 		Permute the matrix to the right direction for the xml presentation
		 * </p>
		 *
		 * @param xmlEventReader the XML event to read
		 *
		 * @return the matrix loaded from the XML
		 *
		 * @see  NeuralNetwork#initMatrix(XMLStreamReader)
		 */
		private Matrix initMatrix(XMLStreamReader xmlEventReader) {
			int rows = Integer.parseInt( xmlEventReader.getAttributeValue(0) );
			int cols = Integer.parseInt( xmlEventReader.getAttributeValue(1) );
			String[] values = xmlEventReader.getAttributeValue(2).split(" ");
			
			Matrix matrix = new Matrix(rows, cols);
			
			// Fill the matrix
			int index = 0;
			for (int i = 0; i < rows; i++)
				for (int j = 0; j < cols; j++)
					matrix.set(i, j, Double.parseDouble( values[index++] ));
			
			return matrix;
		}


		/**
		 *	<p>
		 *	   Change the value of a matrix in randomize values (multiplicated by 2 and minus one)
		 *	</p>
		 *
		 * @param matrix The matrix we want to change
		 */
		private void randomizeIOMatrix(Matrix matrix) {
			for (int i = 0; i < matrix.getRowCount(); i++)
				for (int j = 0; j < matrix.getColumnCount(); j++)
					matrix.set(i, j, Math.random() * 2 - 1);
		}

		/**
		 * <p>
		 *     Change the value of a matrix in randomize values
		 * </p>
		 *
		 * @param matrix The matrix we want to change
		 */
		private void randomizeBiasMatrix(Matrix matrix) {
			for (int i = 0; i < matrix.getRowCount(); i++)
				for (int j = 0; j < matrix.getColumnCount(); j++)
					matrix.set(i, j, Math.random());
		}
		
		/**
		 * <p>
		 *     The training process (decision process). We load an InputData containing the environment values of a current
		 *     turn in this method. Then, this method calculate (thanks to the weighting coeffcients) the decision values
		 *     (load in a Outputdata)
		 * </p>
		 *
		 * @param entries The InputData containing the environment data of the current turn
		 *
		 * @return An OutputData object created from the perceptron results
		 *
		 * @see InputData
		 * @see OutputData
		 */
		public OutputData train(InputData entries) {
			// First Treatment
			//Multiplication du vecteur d'entrée avec la première matrice de poids. On obtient le vecteur de couche sans le neurone de biais
			Matrix vcouche = entries.toMatrix().mult(inputWeights);
			//Ajout du neurone de biais et application de la fonction sigmoïde
			for (int i = 0; i < inputWeights.getColumnCount(); i++)
				vcouche.set(0, i, 1 / (1 + Math.exp(-vcouche.get(0, i) - bias.get(i, 0))));
			
			// Second Treatment	
			//Multiplication du vecteur de couche avec la seconde matrice de poids pour obtenir le vecteur de sortie
			return new OutputData( vcouche.mult(outputWeights) );
		}
		
		/**
		 * <p>
		 *     This method is used to print a neural network in a XML file.
		 *     A neural network is represented by its weighting coefficients.
		 * </p>
		 *
		 * @param file The file where the neural network is printed
		 *
		 * @throws FileNotFoundException if the file is not found
         * @throws XMLStreamException if a problem happens during the reading
		 */
		public void printToXML(File file) throws FileNotFoundException, XMLStreamException {
            XMLStreamWriter xmlWriter = XMLOutputFactory.newInstance().createXMLStreamWriter( new FileOutputStream(file) );

			xmlWriter.writeStartElement("meta");
			xmlWriter.writeAttribute("NbOutputNeurons", Integer.toString(outputWeights.getColumnCount()));
			xmlWriter.writeAttribute("Learners", "1");
			xmlWriter.writeCharacters("\n");
			xmlWriter.writeCharacters("\t");

			xmlWriter.writeStartElement("learner");
			xmlWriter.writeAttribute("accuracy", "");
			xmlWriter.writeAttribute("nbInputNeurons", Integer.toString(inputWeights.getRowCount()));
			xmlWriter.writeAttribute("features_used", "All");
			xmlWriter.writeCharacters("\n");
			xmlWriter.writeCharacters("\t\t");

			xmlWriter.writeStartElement("perceptron");
			xmlWriter.writeAttribute("InputNeurons", Integer.toString(inputWeights.getRowCount()));
			xmlWriter.writeAttribute("HiddenNeurons", Integer.toString(inputWeights.getColumnCount()));
			xmlWriter.writeAttribute("OutputNeurons", Integer.toString(outputWeights.getColumnCount()));
			xmlWriter.writeAttribute("Kernel", "sigmoid");
			xmlWriter.writeCharacters("\n");
			xmlWriter.writeCharacters("\t\t\t");

			xmlWriter.writeEmptyElement("InputWeights");
			xmlWriter.writeAttribute("Rows", Integer.toString(inputWeights.getRowCount()));
			xmlWriter.writeAttribute("Cols", Integer.toString(inputWeights.getColumnCount()));
			xmlWriter.writeAttribute("Matrix", inputWeights.toString());
			xmlWriter.writeCharacters("\n");
			xmlWriter.writeCharacters("\t\t\t");

			xmlWriter.writeEmptyElement("OutputWeights");
			xmlWriter.writeAttribute("Rows", Integer.toString(outputWeights.getRowCount()));
			xmlWriter.writeAttribute("Cols", Integer.toString(outputWeights.getColumnCount()));
			xmlWriter.writeAttribute("Matrix", outputWeights.toString());
			xmlWriter.writeCharacters("\n");
			xmlWriter.writeCharacters("\t\t\t");

			xmlWriter.writeEmptyElement("Bias");
			xmlWriter.writeAttribute("Rows", Integer.toString(bias.getRowCount()));
			xmlWriter.writeAttribute("Cols", Integer.toString(bias.getColumnCount()));
			xmlWriter.writeAttribute("Matrix", bias.toString());
			xmlWriter.writeCharacters("\n");
			xmlWriter.writeCharacters("\t\t");
			xmlWriter.writeEndElement();
			xmlWriter.writeCharacters("\n");
			xmlWriter.writeCharacters("\t");
			xmlWriter.writeEndElement();
			xmlWriter.writeCharacters("\n");
			xmlWriter.writeEndElement();

		    xmlWriter.close();
		}
		
}