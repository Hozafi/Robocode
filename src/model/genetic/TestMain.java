package model.genetic;

public class TestMain {

    public static void main (String[] args) {
        Population pop = new Population(20,4);
        for(int i = 0; i < 1; i++) {
            pop.makeFight();
            pop.nextGeneration();
        }

        System.out.println("Best fitness : " + pop.bestIndividual().getFitness());

    }
}
