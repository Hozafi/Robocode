/*
 * Projet Darwini - Étude Pratique
 *
 * Development of an IA based on genetic algorithms and neural networks.
 *
 * class NaturalSelection.java
 */

package model.genetic;

import java.io.File;

/**
 * This class launches the genetic algorithm.
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

//Branche de Edgar
public class NaturalSelection {

    /**
     * The number of generation.
     */
    private static final int NUMBER_GENERATION = 50;

    public static void main(String[] args) {
        GeneticAlgorithm ga = new GeneticAlgorithm();
        ga.generate(NUMBER_GENERATION);
        ga.savePopulation();
        int numBest = ga.whoIsTheBest(true) + 1;
        System.out.println("The best robot is \"Individual" + numBest + ".xml\", launch robocode and try Darwini* !");
        new File("data/population/Individual" + numBest + ".xml" ).renameTo(new File("out/production/Darwini/controller/Darwini.data/Perceptron.xml"));
    }

}
