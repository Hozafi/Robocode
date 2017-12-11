package model.genetic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

public class Population {

    /*	----- PATHS -----	*/

    /**
     * <p>
     * The directory where the population will be saved.
     * </p>
     */
    private static final String POPULATION_DIRECTORY = "data/population/";

    /**
     * <p>
     * The name of an XML individual file.
     * </p>
     */
    private static final String INDIVIDUAL_FILENAME = "Individual";

    /*	----- ATTRIBUTES -----	*/

    /**
     * <p>
     * The array of individuals contained in the population.
     * </p>
     */
     private ArrayList<Individual> individuals;

    /**
     * <p>
     * The index of the current generation.
     * </p>
     */
    private int generation;

    /**
     * <p>
     * The number of individuals in the population.
     * </p>
     */
    private int size;

    /**
     * <p>
     * The number of individuals that will be kept from one generation to another.
     * </p>
     */
    private int nbSurvivors;



    /*	----- CONSTRUCTOR -----	*/

    /**
     * <p>
     * Creates the population at generation 1, initializes and randomizes every individual.
     * </p>
     */
    public Population(int s, int nbSurv) {

        generation = 1;
        nbSurvivors = nbSurv;
        size = s;

        individuals = new ArrayList<Individual>();

        for (int i = 1; i <= size; i++) {
            individuals.add(new Individual(i));
        }

    }

    /*	----- METHODS -----	*/

    /**
     * <p>
     * Makes every individual fight to determine its fitness.
     * </p>
     */
    public void makeFight() {

        System.out.println("Making generation " + generation + " fight...");

        for (Individual ind : individuals) {
            ind.fight();
        }

        System.out.println("Average fitness of generation " + generation + " : " + averageFitness());

    }

    /**
     * <p>
     * Determines the strongest individuals, kills the weaklings, then creates the next generation with the surviving
     * individuals and their childs.
     * </p>
     */
    public void nextGeneration() {

        System.out.println("Generating generation " + generation + "...");

        Collections.sort(individuals);

        saveBest();

        killWeaklings();

        loadBest();

        generateChilds();

        generation ++;

        System.out.println("Done !");

    }

    /**
     * <p>
     * Saves the best individuals in temporary files.
     * </p>
     */
    private void saveBest() {

        for (int i = 1; i <= nbSurvivors; i++) {
            try {
                copyFile(POPULATION_DIRECTORY + INDIVIDUAL_FILENAME + i + ".xml",
                        POPULATION_DIRECTORY + "Temp_" + INDIVIDUAL_FILENAME + i + ".xml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * <p>
     * Deletes all individual files (except for the temporary ones) and every "weak" Individual in the list.
     * </p>
     */
    private void killWeaklings() {

        File f;

        for (int i = 1; i <= size; i++) {
            f = new File(POPULATION_DIRECTORY + INDIVIDUAL_FILENAME + i + ".xml");
            f.delete();
        }

        for (int i = nbSurvivors + 1; i <= size) {
            individuals.remove(i);
        }

    }

    /**
     * <p>
     * Reloads the best individuals.
     * </p>
     */
    private void loadBest() {

        File f = null;

        for (int i = 1; i <= nbSurvivors; i++) {
            f = new File(POPULATION_DIRECTORY + "Temp_" + INDIVIDUAL_FILENAME + i + ".xml");
            f.renameTo(new File(POPULATION_DIRECTORY + INDIVIDUAL_FILENAME + i + ".xml"));
        }

    }

    /**
     * <p>
     * Fills the new generation with childs of the surviving individuals.
     * </p>
     */
    private void generateChilds() {

        int motherID, fatherID;

        for (int i = nbSurvivors + 1; i <= size) {

            motherID = random(1, nbSurvivors);
            do {
                fatherID = random(1, nbSurvivors);
            } while (motherID == fatherID);

            individuals.add(new Individual(i, individuals.get(motherID).getPerceptron(),
                    individuals.get(fatherID).getPerceptron()));
        }

    }

    /**
     * <p>
     * Return the best individual.
     * </p>
     */
    public Individual bestIndividual() {

        Collections.sort(individuals);

        return individuals.get(0);

    }

    /**
     * <p>
     * Return the average fitness of the generation.
     * </p>
     */
    public double averageFitness() {

        double avg = 0;

        for (Individual ind : individuals) {
            avg += ind.getFitness();
        }

        return (avg / size);

    }



    /*	----- UTIL -----	*/

    /**
     * <p>
     * Copy a file from a path to another path.
     * </p>
     *
     * @param inputFile  the source file
     * @param outputFile the destination file
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
        ;
    }

    /**
     * <p>
     * Return a random number between min and max.
     * </p>
     *
     * @param min the minimum value
     * @param max the maximum value
     * @return the random number
     */
    private int random(int min, int max) {
        return (int) (Math.random() * (max - min)) + min;
    }




}
