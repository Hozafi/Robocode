/*
 * Projet Darwini - Étude Pratique
 *
 * Development of an IA based on genetic algorithms and neural networks.
 *
 * class NeuralNetwork.java
 */

package model.perceptron;

import model.genetic.Individual;
import model.genetic.NaturalSelection;

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
 * This object represent a Neural Network used in the perceptron process.
 * We load a matrix of environment data (from InputData) and after few calculations the neural network gives us
 * a matrix of values representing decisions and transformed as an object OutputData.
 * Those calculations (described further) needs some weighting coefficients which are the parameters of a neural network.
 * Our job is to define the right weighting coefficient for the neural network to be efficient.
 * We can set those coefficient thanks to supervised process (AcquitisionBot and iriselm)
 * or un-supervised process (Genetic algorithm)
 * </p>
 *
 * @author BOIZUMAULT Romain
 * @author BUSSENEAU Alexis
 * @author GEFFRAULT Luc
 * @author MATHIEU Vianney
 * @author VAILLAND Guillaume
 * @author Beaulieu Simon
 * @author Goubet Martin
 * @author Estevany Raphael
 * @author Serano Edgar
 * @version 1.1 - 28/03/17
 * @see    model.genetic.GeneticAlgorithm
 * @see InputData
 * @see OutputData
 * @see controller.AcquisitionBot
 * @see controller.Darwini
 * @see model.genetic.Score
 * @see model.genetic.NaturalSelection
 */
public class NeuralNetwork {

    /*	----- ATTRIBUTES -----	*/

    /**
     * <p>
     * Number of hidden neuron of the neural network
     * </p>
     */
    private static final int HIDDEN_NEURONS = InputData.INPUT_NEURONS;

    /**
     * <p>
     * Number of input neurons of the neural network. Here, we redefine it as a local variable to avoid using the InputData prefix every time.
     * </p>
     */

    private static final int INPUT_NEURONS = InputData.INPUT_NEURONS;

    private static final int OUTPUT_NEURONS = OutputData.OUTPUT_NEURONS;

    /**
     * <p>
     * The first matrix, linking input neurons and the hidden layer
     * </p>
     */
    private Matrix inputWeights;

    /**
     * <p>
     * The second matrix, linking hidden neurons and the output neurons
     * </p>
     */
    private Matrix outputWeights;

    /**
     * <p>
     * Bias vector for the first matrix
     * </p>
     */
    private Matrix inputBias;

    /**
     * <p>
     * Bias vector for the second matrix
     * </p>
     */
    private Matrix outputBias;

    /**
     * <p>
     * Boolean attribute that indicates if the neural network has a hidden layer or not.
     * </p>
     */
    private boolean hasHiddenLayer;


    /*	----- CONSTRUCTOR -----	*/

    /**
     * <p>
     * The neural network constructor with random weighting coefficient
     * (used in the genetic algorithm process)
     * </p>
     */
    public NeuralNetwork() {
        inputWeights = new Matrix(INPUT_NEURONS, OutputData.OUTPUT_NEURONS);
        randomizeIOMatrix(inputWeights);
        inputBias = new Matrix(OUTPUT_NEURONS, 1);
        randomizeBiasMatrix(inputBias);

        hasHiddenLayer = false;

    }

    /**
     * <p>
     * The neural network constructor with random weighting coefficient
     * (used in the genetic algorithm process)
     * </p>
     */
    public NeuralNetwork(Matrix inputWeights, Matrix inputBias) {
        this.inputWeights = inputWeights;
        this.inputBias = inputBias;

        hasHiddenLayer = false;

    }

    /**
     * <p>
     * The neural network constructor, taking two individuals and crossing their neural networks according to CROSS_PROBABILITY.
     * Used in the genetic algorithm process.
     * </p>
     */
    public NeuralNetwork(NeuralNetwork mother, NeuralNetwork father) throws IllegalArgumentException{

        //checks if Individuals have different number of neuron layers, ie if one of them has a hidden layer and the other does not
        if((father.hasHiddenLayer) && (!mother.hasHiddenLayer)
                || (!father.hasHiddenLayer) && (mother.hasHiddenLayer))
        {
            throw new IllegalArgumentException("Error: Individuals do not have the same type of perceptron.");
        }

        //for readability

        this.inputWeights = father.getInputWeights().cross(mother.getInputWeights(), NaturalSelection.CROSS_PROBABILITY);
        this.inputBias = father.getInputBias().cross(mother.getInputBias(), NaturalSelection.CROSS_PROBABILITY);

        if(father.hasHiddenLayer){
            this.outputWeights = father.getOutputWeights().cross(mother.getOutputWeights(), NaturalSelection.CROSS_PROBABILITY);
            this.outputBias = father.getOutputBias().cross(mother.getOutputBias(), NaturalSelection.CROSS_PROBABILITY);
        }

        this.hasHiddenLayer = father.hasHiddenLayer;

    }



    /**
     * <p>
     * The neural network constructor with a file containing the weighting coefficients
     * from the supervised process (from iriselm treatment)
     * </p>
     *
     * @param file The file containing the coefficients
     */
    public NeuralNetwork(File file) {
        try {
            // Get an input factory and instantiate a reader
            XMLStreamReader xmlReader = XMLInputFactory.newInstance().createXMLStreamReader(new FileInputStream(file));

            // Skip the first lines
            for (int i = 0; i < 4; i++)
                xmlReader.nextTag();

            inputWeights = initMatrix(xmlReader);
            // Skip the end of the inputWeights
            xmlReader.nextTag();
            xmlReader.nextTag();
            inputBias = initMatrix(xmlReader);

            // Close the reader
            xmlReader.close();

            hasHiddenLayer = false;

        } catch (FileNotFoundException e) {
            System.out.println(file.getAbsolutePath() + " is not found.");
            System.out.println("The perceptron is not initialized, please put a perceptron in the correct directory.");
        } catch (XMLStreamException e) {
            System.out.println(file.getAbsolutePath() + " is invalid.");
            System.out.println("The perceptron is not initialized, please put a valid perceptron in the directory.");
        }
    }


    /*	----- GETTERS -----	*/

    /**
     * @return The output weights Matrix
     */
    public Matrix getInputWeights() {
        return inputWeights;
    }

    /**
     * @return The inputBias vector
     */
    public Matrix getInputBias() {
        return inputBias;
    }

    public Matrix getOutputWeights(){
        return outputWeights;
    }

    public Matrix getOutputBias(){
        return outputBias;
    }

    /*	----- OTHER METHODS -----	*/

    /**
     * <p>
     * Permute the matrix to the right direction for the xml presentation
     * </p>
     *
     * @param xmlEventReader the XML event to read
     * @return the matrix loaded from the XML
     * @see NeuralNetwork#initMatrix(XMLStreamReader)
     */
    private Matrix initMatrix(XMLStreamReader xmlEventReader) {
        int rows = Integer.parseInt(xmlEventReader.getAttributeValue(0));
        int cols = Integer.parseInt(xmlEventReader.getAttributeValue(1));
        String[] values = xmlEventReader.getAttributeValue(2).split(" ");

        Matrix matrix = new Matrix(rows, cols);

        // Fill the matrix
        int index = 0;
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                matrix.set(i, j, Double.parseDouble(values[index++]));

        return matrix;
    }


    /**
     * <p>
     * Change the value of a matrix in randomize values (multiplicated by 2 and minus one)
     * </p>
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
     * Change the value of a matrix in randomize values
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
     * The training process (decision process). We load an InputData containing the environment values of a current
     * turn in this method. Then, this method calculate (thanks to the weighting coeffcients) the decision values
     * (load in a Outputdata)
     * </p>
     *
     * @param entries The InputData containing the environment data of the current turn
     * @return An OutputData object created from the perceptron results
     * @see InputData
     * @see OutputData
     */
    public OutputData train(InputData entries) {
        // First Treatment
        //Multiplication du vecteur d'entrée avec la première matrice de poids. On obtient le vecteur de couche sans le neurone de biais
        Matrix vcouche = entries.toMatrix().mult(inputWeights);
        //Ajout du neurone de biais et application de la fonction sigmoïde


        /*for (int i = 0; i < inputWeights.getColumnCount(); i++)
            vcouche.set(0, i, 1 / (1 + Math.exp(-vcouche.get(0, i) - inputBias.get(i, 0)))); */

        // Second Treatment
        //Multiplication du vecteur de couche avec la seconde matrice de poids pour obtenir le vecteur de sortie
        return new OutputData(vcouche);
    }

    /**
     * <p>
     * This method is used to print a neural network in a XML file.
     * A neural network is represented by its weighting coefficients.
     * </p>
     *
     * @param file The file where the neural network is printed
     * @throws FileNotFoundException if the file is not found
     * @throws XMLStreamException    if a problem happens during the reading
     */
    public void printToXML(File file) throws FileNotFoundException, XMLStreamException {
        XMLStreamWriter xmlWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(new FileOutputStream(file));

        xmlWriter.writeStartElement("meta");
        xmlWriter.writeAttribute("NbOutputNeurons", Integer.toString(inputWeights.getColumnCount()));
        xmlWriter.writeAttribute("Learners", "1");
        xmlWriter.writeCharacters("\n");
        xmlWriter.writeCharacters("\t");

        xmlWriter.writeStartElement("learner");
        xmlWriter.writeAttribute("accuracy", "");

        xmlWriter.writeAttribute("features_used", "All");
        xmlWriter.writeCharacters("\n");
        xmlWriter.writeCharacters("\t\t");

        xmlWriter.writeStartElement("perceptron");
        xmlWriter.writeAttribute("OutputNeurons", Integer.toString(inputWeights.getColumnCount()));
        xmlWriter.writeAttribute("Kernel", "sigmoid");
        xmlWriter.writeCharacters("\n");
        xmlWriter.writeCharacters("\t\t\t");

        xmlWriter.writeEmptyElement("OutputWeights");
        xmlWriter.writeAttribute("Rows", Integer.toString(inputWeights.getRowCount()));
        xmlWriter.writeAttribute("Cols", Integer.toString(inputWeights.getColumnCount()));
        xmlWriter.writeAttribute("Matrix", inputWeights.toString());
        xmlWriter.writeCharacters("\n");
        xmlWriter.writeCharacters("\t\t\t");

        xmlWriter.writeEmptyElement("Bias");
        xmlWriter.writeAttribute("Rows", Integer.toString(inputBias.getRowCount()));
        xmlWriter.writeAttribute("Cols", Integer.toString(inputBias.getColumnCount()));
        xmlWriter.writeAttribute("Matrix", inputBias.toString());
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

    public String toString(){

        String str = "";

        str += "First matrix: \n" + this.inputWeights.toString();
        str += "\nFirst bias vector: \n" + this.inputWeights.toString();

        if(hasHiddenLayer){
            str += "\nSecond matrix: \n" + this.inputWeights.toString();
            str += "\nSecond bias vector: \n" + this.inputWeights.toString();
        }

        return str;
    }

}