/*
 * Projet Darwini - Étude Pratique
 *
 * Development of an IA based on genetic algorithms and neural networks.
 *
 * class NeuralNetwork.java
 */

package model.perceptron;

import model.genetic.NaturalSelection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Random;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import static model.genetic.Population.output_bias_mean;
import static model.genetic.Population.output_mean;

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
 * @author Beaulieu Simon0
 * @author Goubet Martin
 * @author Estevany Raphael
 * @author Serano Edgar
 * @version 1.1 - 28/03/17
 * @see InputData
 * @see OutputData
 * @see controller.AcquisitionBot
 * @see controller.Darwini
 * @see model.genetic.NaturalSelection
 */
public class NeuralNetwork {

    /*	----- ATTRIBUTES -----	*/

    /**
     * <p>
     * Number of hidden neuron of the neural network
     * </p>
     */
    //private static final int HIDDEN_NEURONS = InputData.INPUT_NEURONS;
    public static final int HIDDEN_NEURONS = 40;

    /**
     * <p>
     * Number of input neurons of the neural network. Here, we redefine it as a local variable to avoid using the InputData prefix every time.
     * </p>
     */

    public static final int INPUT_NEURONS = InputData.INPUT_NEURONS;

    public static final int OUTPUT_NEURONS = OutputData.OUTPUT_NEURONS;

    /*
    Implementation of ELM : the input weight matrix is final
     */
    private static final Matrix INPUT_WEIGHTS_MATRIX = generateInputMatrix();

    /*
    Implementation of ELM : the input bias matrix is final
     */
    private static final Matrix INPUT_BIAS_MATRIX = generateBiasMatrix();

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
    private boolean hasHiddenLayer;  // Enlever ça, ça sert plus à rien (le considérer comme TRUE partout)


    /*	----- CONSTRUCTOR -----	*/

    /*
    Implementation of ELM : neural network always have an hidden layer. The input weights matrix is always the same and can't evolve.
     */
    public NeuralNetwork(){
        inputWeights = INPUT_WEIGHTS_MATRIX;
        inputBias = INPUT_BIAS_MATRIX;
        outputWeights = new Matrix(HIDDEN_NEURONS, OUTPUT_NEURONS);
        outputBias = new Matrix(1, OUTPUT_NEURONS);
        randomizeIOMatrix(outputWeights);
        randomizeBiasMatrix(outputBias);
    }

    /* Implementation of gaussian cross */
    public NeuralNetwork(Matrix mean, Matrix deviation, Matrix bias_mean, Matrix bias_deviation){
        double value;
        Random r = new Random();
        inputWeights = INPUT_WEIGHTS_MATRIX;
        inputBias = INPUT_BIAS_MATRIX;
        outputWeights = new Matrix(HIDDEN_NEURONS, OUTPUT_NEURONS);
        outputBias = new Matrix(OUTPUT_NEURONS, 1);

        for (int i = 0; i<HIDDEN_NEURONS; i++){
            for (int j = 0; j<OUTPUT_NEURONS; j++){
                value = r.nextGaussian() * deviation.get(i, j) + mean.get(i, j);
                outputWeights.set(i, j, value);
            }
        }

        for (int i = 0 ; i<OUTPUT_NEURONS; i++){
            value = r.nextGaussian() * bias_deviation.get(i, 0) + bias_mean.get(i, 0);
            outputBias.set(i, 0, value);
        }
        
    }



    /**
     * <p>
     * The neural network constructor with random weighting coefficient
     * (used in the genetic algorithm process)
     * </p>
     */
    public NeuralNetwork(boolean hiddenLayer) {

        inputWeights = new Matrix(INPUT_NEURONS, hiddenLayer ? HIDDEN_NEURONS : OUTPUT_NEURONS);
        randomizeIOMatrix(inputWeights);
        inputBias = new Matrix(hiddenLayer ? HIDDEN_NEURONS : OUTPUT_NEURONS, 1);
        randomizeBiasMatrix(inputBias);

        if(hiddenLayer){

            outputWeights = new Matrix(HIDDEN_NEURONS, OUTPUT_NEURONS);
            randomizeIOMatrix(outputWeights);
            outputBias = new Matrix(OUTPUT_NEURONS, 1);
            randomizeBiasMatrix(outputBias);

        }

    }



    /**
     * <p>
     * The neural network constructor, taking two individuals and crossing their neural networks according to CROSS_PROBABILITY.
     * Used in the genetic algorithm process.
     * </p>
     */
    public NeuralNetwork(NeuralNetwork mother, NeuralNetwork father) throws IllegalArgumentException{

        //checks if Individuals have different number of neuron layers, ie if one of them has a hidden layer and the other does not
        if((father.hasHiddenLayer) != (mother.hasHiddenLayer))
        {
            throw new IllegalArgumentException("Error: Individuals do not have the same type of perceptron.");
        }


        this.inputWeights = cross(father.getInputWeights(), mother.getInputWeights());
        this.inputBias = cross(father.getInputBias(), mother.getInputBias());

        if(father.hasHiddenLayer){
            this.outputWeights = cross(father.getOutputWeights(), mother.getOutputWeights());
            this.outputBias = cross(father.getOutputBias(), mother.getOutputBias());
        }

        this.hasHiddenLayer = father.hasHiddenLayer;

    }

    public Matrix cross(Matrix father, Matrix mother) {
        double random;
        Matrix c = new Matrix(father.getRowCount(), father.getColumnCount());

        for (int i = 0; i < father.getRowCount(); i++) {
            for (int j = 0; j < father.getColumnCount(); j++) {
                random = Math.random();
                if (random <= NaturalSelection.CROSS_PROBABILITY) { // If we're applying a cross to this weight
                    c.set(i, j, father.get(i, j));
                } else {
                    c.set(i, j, mother.get(i, j));
                }
            }
        }
        return c;
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
            xmlReader.nextTag();
            xmlReader.nextTag();
            outputWeights = initMatrix(xmlReader);
            xmlReader.nextTag();
            xmlReader.nextTag();
            outputBias = initMatrix(xmlReader);



            // Close the reader
            xmlReader.close();

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
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix.set(i, j, Double.parseDouble(values[index++]));
            }
        }

        return matrix;
    }


    /**
     * <p>
     * Change the value of a matrix in randomize values (multiplicated by 2 and minus one)
     * </p>
     *
     * @param matrix The matrix we want to change
     */
    /* Implementation of gaussian cross */
    private void randomizeIOMatrix(Matrix matrix) {
        double value;
        for (int i = 0; i < matrix.getRowCount(); i++) {
            for (int j = 0; j < matrix.getColumnCount(); j++) {
                value = Math.random() * 2 - 1;
                matrix.set(i, j, value);
                output_mean.set(i, j, (output_mean.get(i, j)+value));
            }
        }
    }

    /**
     * <p>
     * Change the value of a matrix in randomize values
     * </p>
     *
     * @param matrix The matrix we want to change
     */
    private void randomizeBiasMatrix(Matrix matrix) {
        double value;
        for (int i = 0; i < matrix.getRowCount(); i++) {
            for (int j = 0; j < matrix.getColumnCount(); j++) {
                value = Math.random();
                matrix.set(i, j, value);
                output_bias_mean.set(i, j, output_bias_mean.get(i, j) + value);
            }
        }
    }


    /* Implementation of ELM : generates our final input matrix */
    private static Matrix generateInputMatrix(){
        Matrix m = new Matrix(INPUT_NEURONS, HIDDEN_NEURONS);
        for (int i = 0; i<m.getRowCount(); i++){
            for (int j = 0; j<m.getColumnCount();j++){
                m.set(i, j, Math.random()*2 - 1);
            }
        }
        return m;
    }

    /* Implementation of ELM : generates our final bias matrix */
    private static Matrix generateBiasMatrix(){
        Matrix m = new Matrix(1, HIDDEN_NEURONS);
        for (int i = 0; i<m.getRowCount(); i++){
            for (int j = 0; j<m.getColumnCount(); j++){
                m.set(i, j, Math.random());
            }
        }
        return m;
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
        vcouche.plus(inputBias);

        vcouche = vcouche.mult(outputWeights);
        vcouche.plus(outputBias);


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

        xmlWriter.writeEmptyElement("InputWeights");
        xmlWriter.writeAttribute("Rows", Integer.toString(inputWeights.getRowCount()));
        xmlWriter.writeAttribute("Cols", Integer.toString(inputWeights.getColumnCount()));
        StringBuilder sb = new StringBuilder(inputWeights.getRowCount() * inputWeights.getColumnCount());
        for (int i = 0; i < inputWeights.getRowCount(); i++) {
            for (int j = 0; j < inputWeights.getColumnCount(); j++) {
                sb.append(String.format(Locale.US, "%.2f", inputWeights.get(i, j))).append(" ");
            }
        }
        xmlWriter.writeAttribute("Matrix", sb.toString());
        xmlWriter.writeCharacters("\n");
        xmlWriter.writeCharacters("\t\t\t");

        xmlWriter.writeEmptyElement("InputBias");
        xmlWriter.writeAttribute("Rows", Integer.toString(inputBias.getRowCount()));
        xmlWriter.writeAttribute("Cols", Integer.toString(inputBias.getColumnCount()));
        sb = new StringBuilder(inputBias.getRowCount() * inputBias.getColumnCount());;
        for (int i = 0; i < inputBias.getRowCount(); i++) {
            for (int j = 0; j < inputBias.getColumnCount(); j++) {
                sb.append(String.format(Locale.US, "%.2f", inputBias.get(i, j))).append(" ");
            }
        }
        xmlWriter.writeAttribute("Matrix", sb.toString());
        xmlWriter.writeCharacters("\n");
        xmlWriter.writeCharacters("\t\t\t");

        xmlWriter.writeEmptyElement("OutputWeights");
        xmlWriter.writeAttribute("Rows", Integer.toString(outputWeights.getRowCount()));
        xmlWriter.writeAttribute("Cols", Integer.toString(outputWeights.getColumnCount()));
        sb = new StringBuilder(outputWeights.getRowCount() * outputWeights.getColumnCount());
        for (int i = 0; i < outputWeights.getRowCount(); i++) {
            for (int j = 0; j < outputWeights.getColumnCount(); j++) {
                sb.append(String.format(Locale.US, "%.2f", outputWeights.get(i, j))).append(" ");
            }
        }
        xmlWriter.writeAttribute("Matrix", sb.toString());
        xmlWriter.writeCharacters("\n");
        xmlWriter.writeCharacters("\t\t\t");

        xmlWriter.writeEmptyElement("OutputBias");
        xmlWriter.writeAttribute("Rows", Integer.toString(outputBias.getRowCount()));
        xmlWriter.writeAttribute("Cols", Integer.toString(outputBias.getColumnCount()));
        sb = new StringBuilder(outputBias.getRowCount() * outputBias.getColumnCount());;
        for (int i = 0; i < outputBias.getRowCount(); i++) {
            for (int j = 0; j < outputBias.getColumnCount(); j++) {
                sb.append(String.format(Locale.US, "%.2f", outputBias.get(i, j))).append(" ");
            }
        }
        xmlWriter.writeAttribute("Matrix", sb.toString());
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

    public String toDebug(){

        String str = "";

        str += "First matrix: \n" + this.inputWeights.toString();
        str += "\nFirst bias vector: \n" + this.inputBias.toString();

        str += "\nSecond matrix: \n" + this.outputWeights.toString();

        return str;
    }

}