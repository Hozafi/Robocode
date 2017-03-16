/*
 * Projet Darwini - Étude Pratique
 *
 * Development of an IA based on genetic algorithms and neural networks.
 *
 * class NaturalSelection.java
 */

package model.genetic;

/**
 * This class launches the genetic algorithm.
 *
 * @version 1.0 - 17/11/15
 * @author BOIZUMAULT Romain
 * @author BUSSENEAU Alexis
 * @author GEFFRAULT Luc
 * @author MATHIEU Vianney
 * @author VAILLAND Guillaume
 */
public class NaturalSelection {

    /**
     * The number of generation.
     */
    private static final int NUMBER_GENERATION = 2;

    public static void main(String[] args) {
        GeneticAlgorithm ga = new GeneticAlgorithm();
        ga.generate(NUMBER_GENERATION);
        ga.savePopulation();
        System.out.println("The best robot is \"Individual" + (ga.whoIsTheBest(true) + 1) + ".xml\", try it !");
    }

}
