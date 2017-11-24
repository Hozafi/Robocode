package model.genetic;

import controller.Darwini;
import model.perceptron.Matrix;
import model.perceptron.NeuralNetwork;
import org.apache.bcel.generic.POP;

import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * This class implements a genetic algorithm to robocode to find the parameters of a perceptron.
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
 * @version 1.2 - 23/11/2017
 * @author REIS HAUS MARTINS Daniel
 * @author BARROUX Antoine
 * @author GILBERT Auriane
 * @author REBOUT Etienne
 */
public class RefactoGeneticAlgorithm {

    // ====================================================================================
    // ====================================== PATHS =======================================
    // ====================================================================================
    /**
     * <p>
     *     The directory where the population will be saved.
     * </p>
     */
    private static final String POPULATION_DIRECTORY = "data/population/";


    /**
     * <p>
     *     The directory where the Darwini's perceptron is loaded.
     *     This directory can change if you use Eclipse or IntelliJ.
     *     Currently, it is set to IntelliJ configuration.
     * </p>
     */
    private static final String ROBOT_DIRECTORY = "out/production/Darwini/controller/Darwini.data/";

    /**
     * <p>
     *     The name of an xml file for a robot. His number will be added at the end of his name to have Individual1, Individual2, ...
     * </p>
     */
    private static final String INDIVIDUAL_FILENAME = "Individual";

    /**
     * <p>
     *     The directory containing the Robocode library.
     * </p>
     */
    private static final String ROBOCODE_PATH = "libs/robocode.jar";

    /**
     * <p>
     *     The battle configuration for test the individuals.
     *     Modify it if you want to change the opponent robot.
     * </p>
     */
    private static final String BATTLE_PATH = "data/test.battle";

    /**
     * <p>
     *     The path where the temporary file created by Robocode will be saved.
     * </p>
     */
    private static final String RESULTS_PATH = "results.txt";





    // ====================================================================================
    // =========================== GENETIC ALGORITHM SETTINGS =============================
    // ====================================================================================
    /**
     * <p>
     *     Take only the third quarter of the available cores of the computer user.
     * </p>
     */
    private static final int NB_THREADS = 3 * Runtime.getRuntime().availableProcessors() / 4;

    /**
     * <p>
     *     Defines the size of a population
     * </p>
     */
    private static final int POPULATION_SIZE = 10;

    /**
     * <p>
     *     Defines the probability of the cross for each weights
     * </p>
     */
    private static final double CROSS_PROBABILITY = 0.1;

    /**
     * <p>
     *     The probability to make a mutation.
     * </p>
     */
    private static final double MUTATION_PROBABILITY = 0.1;

    /**
     * <p>
     *     The number of minimum mutation when it happens.
     * </p>
     */
    private static final int MUTATION_MIN = 5;

    /**
     * <p>
     *     The number of maximum mutation when it happens.
     * </p>
     */
    private static final int MUTATION_MAX = 20;





    // ====================================================================================
    // ================================== ATTRIBUTES ======================================
    // ====================================================================================
    /**
     * <p>
     *     The individuals of the generation.
     * </p>
     */
    private final NeuralNetwork[] population;

    /**
     * <p>
     *     The population's scores.
     * </p>
     */
    private final Score[] scores;

    private NeuralNetwork bestIndividual = null;
    private int indexOfBestIndividual;
    private NeuralNetwork secondBestIndividual = null;
    private int indexOfSecondBestIndividual;




    // ====================================================================================
    // ================================== CONSTRUCTOR =====================================
    // ====================================================================================
    /**
     * <p>
     *     Initialize the genetic algorithm loading all the available individuals (perceptron) or creating it if it doesn't exist.
     *     During the initialization, each perceptron is tested and scored.
     * </p>
     */
    public RefactoGeneticAlgorithm(){
        population = new NeuralNetwork[POPULATION_SIZE];
        scores = new Score[POPULATION_SIZE];
        new File(POPULATION_DIRECTORY).mkdir();
        new File(ROBOT_DIRECTORY).mkdir();

        System.out.println(POPULATION_SIZE + " individuals are being initialized...");
        // Create pools (thread) to execute all the individual loadings.
        ExecutorService executor = Executors.newFixedThreadPool(NB_THREADS);
        for (int i = 0; i < POPULATION_SIZE; i++) {
            int individual = i;
            // Send to the pools a runnable action.
            executor.submit(() -> {
                File file = new File(POPULATION_DIRECTORY + INDIVIDUAL_FILENAME + (individual + 1) + ".xml");

                // If the file exist, loading the perceptron.
                if (file.exists())
                    population[individual] = new NeuralNetwork(file);
                    // Else create a random perceptron.
                else
                    try {
                        population[individual] = new NeuralNetwork();
                        population[individual].printToXML(file);
                    } catch (FileNotFoundException | XMLStreamException e) {
                        e.printStackTrace();
                    }

                scores[individual] = fitness(individual);
                System.out.println("\tIndividual n°" + (individual + 1) + "...LOADED");
            });
        }
        executor.shutdown();
        try {
            executor.awaitTermination(6000, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            System.out.println("Initialization takes too much time, please change your computer");
        }
        System.out.println("DONE");
    }

    // ====================================================================================
    // ========================= EXCHANGE OF GENETIC MATERIAL  ============================
    // ====================================================================================
    /**
     * <p>
     *     Select by tournament an individual.
     * </p>
     * @return the individual selected
     */
    private void selection() {
        int i;
        int indexGagnant;

        // Select the individual which has the best score according to a random selection
        for (i = 0; i < (POPULATION_SIZE-1); i++){
            // Comparing the score of the winning random individual and the two best individual
            indexGagnant = (scores[i].compareTo(scores[i+1]) > 0) ? i : i+1;

            if ( (scores[indexGagnant].compareTo(scores[indexOfBestIndividual]) == 1)
                    || (bestIndividual == null)
                    && (bestIndividual != population[indexGagnant])){
                indexOfBestIndividual = indexGagnant;
                bestIndividual = population[indexOfBestIndividual];
            }
            else if ( (scores[indexGagnant].compareTo(scores[indexOfSecondBestIndividual]) == 1)
                    || (secondBestIndividual == null)
                    && (secondBestIndividual != population[indexGagnant])){
                indexOfSecondBestIndividual = indexGagnant;
                secondBestIndividual = population[indexOfSecondBestIndividual];
            }
        }
    }


    /**
     * <p>
     *     Cross two individuals and return their descendants.
     * </p>
     *
     * @param mother the first individual to cross
     * @param father the second individual to cross
     *
     * @return the children generated
     */
    private NeuralNetwork[] crossover(NeuralNetwork mother, NeuralNetwork father) {
        NeuralNetwork[] children = {mother, father};

        cross(mother.getInputWeights(), father.getInputWeights());
        cross(mother.getOutputWeights(), father.getOutputWeights());
        cross(mother.getBias(), father.getBias());

        return children;
    }



    /**
     * <p>
     *     Cross two matrix with a probability to cross for each weight
     * </p>
     *
     * @param m1 the first matrix to cross
     * @param m2 the second matrix to cross
     */
    private void cross(Matrix m1, Matrix m2) {
        double tmp; // Used to swap the two matrix values
        double random;

        for (int i = 0; i < m1.getRowCount(); i++)
            for (int j = 0; j <  m1.getColumnCount(); j++) {
                random = Math.random();
                if (random <= CROSS_PROBABILITY){ // If we're applying a cross to this weight
                    tmp = m1.get(i, j);
                    m1.set(i, j, m2.get(i, j));
                    m1.set(i, j, tmp);
                }
            }
    }


    /**
     * <p>
     *     Make a mutation on an individual.
     * </p>
     *
     * @param child the individual to mutate
     */
    private void mutation(NeuralNetwork child) {
        if (Math.random() < MUTATION_PROBABILITY)
            mutate(child.getInputWeights());
        if (Math.random() < MUTATION_PROBABILITY)
            mutate(child.getOutputWeights());
        if (Math.random() < MUTATION_PROBABILITY)
            mutate(child.getBias());
    }


    /**
     * <p>
     *     Realize a mutation on a matrix
     * </p>
     *
     * @param m the matrix to mutate
     */
    private void mutate(Matrix m) {
        int rowCount = m.getRowCount();
        int columnCount = m.getColumnCount();

        for (int i = 0; i < random(MUTATION_MIN, MUTATION_MAX); i++)
            m.set(random(0, rowCount), random(0, columnCount) , Math.random() * 2 - 1);
    }


    /**
     * <p>
     *     Evaluate an individual with Robocode.
     *     This method is synchronized because only one individual can be tested at a time.
     *
     * </p>
     *
     * @param individual the index of the individual to test
     *
     * @return the score of the individual
     */
    private synchronized Score fitness(int individual) {
        try {
            copyFile(POPULATION_DIRECTORY + INDIVIDUAL_FILENAME + (individual + 1) + ".xml", ROBOT_DIRECTORY + Darwini.PERCEPTRON_FILE);

            // Launch the test in Robocode
            Runtime.getRuntime().exec("java -Xmx512M -DNOSECURITY=true -DWORKINGDIRECTORY=data -cp " + ROBOCODE_PATH + " robocode.Robocode -nosound -nodisplay -battle " + BATTLE_PATH + " -results " + RESULTS_PATH).waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        // At the end of the battle, the robot saves the number of shot in a txt file.

        return new Score(RESULTS_PATH, "Darwini");
    }




    // ====================================================================================
    // =============================== NEXT GENERATION  ===================================
    // ====================================================================================
    /**
     * <p>
     *     Launch the generations.
     * </p>
     *
     * @param numberGeneration the number of generation to do
     *
     * @exception IllegalArgumentException if the number of generation specified is less than 1
     */
    public void generate(int numberGeneration) throws IllegalArgumentException {
        if (numberGeneration < 1)
            throw new IllegalArgumentException("The number of generation must be greater than 0");

        NeuralNetwork[] newPopulation = new NeuralNetwork[POPULATION_SIZE];
        Score[] newScores = new Score[POPULATION_SIZE];

        for (int i = 0; i < numberGeneration; i++) {
            System.out.println("Generation n°" + (i + 1) + "...");
            // Reset and select the best individuals for this generation
            bestIndividual = null;
            secondBestIndividual = null;
            selection();

            // Keep the best individual
            //int best = keepBest();
            newPopulation[0] = population[indexOfBestIndividual];
            newScores[0] = scores[indexOfBestIndividual];
            System.out.println("\tIndividual n°1 ...RECUPERATED ");
            System.out.println(newScores[0]);
            for (int j = 1; j < POPULATION_SIZE; j = j + 2) {

                NeuralNetwork[] children = crossover(bestIndividual, secondBestIndividual);
                mutation(children[0]);
                newPopulation[j] = children[0];

                newScores[j] = fitness(j);
                System.out.print("\tIndividual n°" + (j + 1) + "...CREATED ");

                System.out.println(newScores[j]);
                if (j != POPULATION_SIZE - 1) {
                    mutation(children[1]);
                    newPopulation[j + 1] = children[1];
                    newScores[j + 1] = fitness(j + 1);
                    System.out.print("\tIndividual n°" + (j + 2) + "...CREATED ");
                    System.out.println(newScores[j+1]);
                }
            }
            copyNewPopulation(newPopulation);
            copyNewScores(newScores);
            //population = newPopulation;
            //scores = newScores;
        }
    }





    // ====================================================================================
    // ===================================== UTIL  ========================================
    // ====================================================================================
    /**
     * <p>
     *     Return a random number between the min and the max.
     * </p>
     *
     * @param min the minimum value
     * @param max the maximum value
     *
     * @return the random number
     */
    private int random(int min, int max) {
        return (int)(Math.random() * (max - min)) + min;
    }


    /**
     * <p>
     *     Copy a file from a path to another path.
     * </p>
     *
     * @param inputFile the source file
     * @param outputFile the destination file
     *
     * @throws IOException if the file paths do not exist
     */
    private void copyFile(String inputFile, String outputFile) throws IOException {
        // Copy the tested perceptron in the specified directory
        FileInputStream is = new FileInputStream(inputFile);
        FileOutputStream os = new FileOutputStream(outputFile);
        byte[] buffer = new byte[1024];
        int length;

        while ((length = is.read(buffer)) > 0)
            os.write(buffer, 0, length);

        is.close();
        os.close();
    }


    /**
     * <p>
     *     Copy all the generation in XML format to allow user to reuse this generation for the genetic algorithm.
     * </p>
     */
    public void savePopulation() {
        System.out.println("The current generation is being saved...");

        ExecutorService executor = Executors.newFixedThreadPool(NB_THREADS);
        for (int i = 0; i < POPULATION_SIZE; i++) {
            final Darwini darwini=new Darwini();
            int individual = i;
            executor.submit(() -> {
                try {
                    population[individual].printToXML( new File(POPULATION_DIRECTORY + INDIVIDUAL_FILENAME + (individual + 1) + ".xml") );
                    //population[individual].printToXML( new File(POPULATION_DIRECTORY + "Perceptron.xml") );
                } catch (FileNotFoundException | XMLStreamException e) {
                    e.printStackTrace();
                }

                darwini.run();

                System.out.println("\tIndividual n°" + (individual + 1) + "...SAVED");
            });
        }
        executor.shutdown();
        try {
            executor.awaitTermination(6000, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.out.println("Saving the population takes too much time, please change your computer");
        }
        System.out.println("DONE");
    }


    /**
     * <p>
     *     Return the best individual of the population.
     * </p>
     *
     * @param copy if true, copy the best individual into the Darwini's perceptron
     *
     * @return the index of the best individual
     */
    public int whoIsTheBest(boolean copy) {
        if (copy)
            try {
                copyFile(POPULATION_DIRECTORY + INDIVIDUAL_FILENAME + (indexOfBestIndividual + 1) + ".xml", ROBOT_DIRECTORY + Darwini.PERCEPTRON_FILE);
            } catch (IOException e) {
                e.printStackTrace();
            }

        return indexOfBestIndividual;
    }

    private void copyNewPopulation(NeuralNetwork[] newPopulation){
        for (int i=0; i<POPULATION_SIZE; i++){
            population[i] = newPopulation[i];
        }
    }

    private void copyNewScores(Score[] newScores){
        for (int i=0; i<POPULATION_SIZE; i++){
            scores[i] = newScores[i];
        }
    }
}
