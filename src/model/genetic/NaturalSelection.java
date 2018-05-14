package model.genetic;

public class NaturalSelection {


    static final String POPULATION_DIRECTORY = "data/population/";

    /**
     * <p>
     * The directory where the Darwini's perceptron is loaded.
     * This directory can change if you use Eclipse or IntelliJ.
     * Currently, it is set to IntelliJ configuration.
     * </p>
     */
    static final String ROBOT_DIRECTORY = "out/production/Darwini/controller/Darwini.data/";

    /**
     * <p>
     * The name of an xml file for a robot. His number will be added at the end of his name to have Individual1, Individual2, ...
     * </p>
     */
    static final String INDIVIDUAL_FILENAME = "Individual";

    /**
     * <p>
     * The directory containing the Robocode library.
     * </p>
     */
    static final String ROBOCODE_PATH = "libs/robocode.jar";

    /**
     * <p>
     * The battle configuration for test the individuals.
     * Modify it if you want to change the opponent robot.
     * </p>
     */
    static final String BATTLE_PATH = "data/test.battle";

    /**
     * <p>
     * The path where the temporary file created by Robocode will be saved.
     * </p>
     */
    static final String RESULTS_PATH = "results.txt";

    private static final String FITNESS_EVOLUTION_PATH = "fitness_evo.csv";


    // ====================================================================================
    // =========================== GENETIC ALGORITHM SETTINGS =============================
    // ====================================================================================

    /**
     * <p>
     * Defines the size of a population
     * </p>
     */
    private static final int POPULATION_SIZE = 10;

    /**
     * <p>
     * Defines the probability of the cross for each weights
     * </p>
     */
    public static final double CROSS_PROBABILITY = 0.5;

    /**
     * <p>
     * The probability to make a mutation.
     * </p>
     */
    private static final double MUTATION_PROBABILITY = 0.1;

    /**
     * <p>
     * The maximal absolute proportion of mutation on a single link.
     * </p>
     */
    private static final double MUTATION_PROPORTION = 0.2;

    /**
     * <p>
     * The number of minimum mutation when it happens.
     * </p>
     */
    private static final int MUTATION_MIN = 5;

    /**
     * <p>
     * The number of maximum mutation when it happens.
     * </p>
     */
    private static final int MUTATION_MAX = 20;

    private static final int MAX_GENERATIONS = 10;

    public static void main (String[] args) {
        Population pop = new Population(3,1);
        for(int i = 0; i < MAX_GENERATIONS; i++) {
            pop.makeFight();
            pop.nextGeneration();
        }

        System.out.println("Best fitness : " + pop.bestIndividual().getFitness());

    }
}
