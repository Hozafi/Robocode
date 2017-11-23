package model.genetic;

import model.perceptron.NeuralNetwork;

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

    // ================================================================
    // ============================ PATHS =============================
    // ================================================================
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





    // ================================================================
    // ================= GENETIC ALGORITHM SETTINGS ===================
    // ================================================================
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
    private static final int POPULATION_SIZE = 50;





    // ================================================================
    // ======================== ATTRIBUTES ============================
    // ================================================================
    /**
     * <p>
     *     The individuals of the generation.
     * </p>
     */
    private NeuralNetwork[] population;

    /**
     * <p>
     *     The population's scores.
     * </p>
     */
    private Score[] scores;



}
