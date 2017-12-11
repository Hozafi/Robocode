package model.genetic;

import model.perceptron.Matrix;
import model.perceptron.NeuralNetwork;

import java.io.File;

public class Individual implements Comparable {

    /*	----- ATTRIBUTES -----	*/

    /**
     * <p>
     * The index of the individual.
     * </p>
     */
    private int index;

    /**
     * <p>
     * The fitness of the individual.
     * </p>
     */
    private double fitness;

    /**
     * <p>
     * The neural network of the individual.
     * </p>
     */
    private NeuralNetwork perceptron;



    /*	----- CONSTRUCTORS -----	*/

    public Individual(int id) {

        index = id;
        fitness = -9999;
        perceptron = new NeuralNetwork();


    }

    public Individual(int id, NeuralNetwork mother, NeuralNetwork father) {

        index = id;
        fitness = -9999;

        perceptron = new NeuralNetwork(cross(mother.getOutputWeights(), father.getOutputWeights()),
                cross(mother.getBias(), father.getBias()));

    }

    /*	----- METHODS -----	*/

    /**
     * <p>
     * Cross two individuals and return their two descendants.
     * </p>
     *
     * @param mother the first individual to cross
     * @param father the second individual to cross
     * @return the two childrens generated
     */
    private NeuralNetwork[] crossover(NeuralNetwork mother, NeuralNetwork father) {
        NeuralNetwork[] children = {mother, father};

        cross(mother.getOutputWeights(), father.getOutputWeights());
        cross(mother.getBias(), father.getBias());

        return children;
    }


    /**
     * <p>
     * Crosses two matrixes.
     * </p>
     *
     * @param m the mother matrix (first matrix to cross)
     * @param f the father matrix (second matrix to cross)
     */
    private Matrix cross(Matrix m, Matrix f) {

        double random;
        Matrix c = new Matrix(m.getRowCount(), m.getColumnCount());

        for (int i = 0; i < m.getRowCount(); i++) {
            for (int j = 0; j < m.getColumnCount(); j++) {
                random = Math.random();
                if (random <= RefactoGeneticAlgorithm.CROSS_PROBABILITY) { // If we're applying a cross to this weight
                    c.set(i, j, f.get(i, j));
                    c.set(i, j, m.get(i, j));
                }
            }
        }

        return c;
    }

    public void fight() {

        // TODO

    }


    @Override
    public int compareTo(Object o) {
        Individual ind = (Individual)o;
        return Double.compare(fitness, ind.fitness);
    }

    /*	----- GETTERS & SETTERS -----	*/

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public NeuralNetwork getPerceptron() {
        return perceptron;
    }

    public void setPerceptron(NeuralNetwork perceptron) {
        this.perceptron = perceptron;
    }


}
