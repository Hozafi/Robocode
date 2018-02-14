package model.genetic;

import controller.Darwini;
import model.perceptron.Matrix;
import model.perceptron.NeuralNetwork;

import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

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

    public Individual(int id){

        index = id;
        fitness = -9999;
        perceptron = new NeuralNetwork();

        try {
            perceptron.printToXML(new File(Population.POPULATION_DIRECTORY + Population.INDIVIDUAL_FILENAME + index + ".xml"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

    }

    public Individual(int id, NeuralNetwork mother, NeuralNetwork father) {

        index = id;
        fitness = -9999;

        perceptron = new NeuralNetwork(cross(mother.getInputWeights(), father.getInputWeights()),
                cross(mother.getInputBias(), father.getInputBias()));

        try {
            perceptron.printToXML(new File(Population.POPULATION_DIRECTORY + Population.INDIVIDUAL_FILENAME + index + ".xml"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

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

        cross(mother.getInputWeights(), father.getInputWeights());
        cross(mother.getInputBias(), father.getInputBias());

        return children;
    }

    public boolean testmethod(){

        Individual father = new Individual(1);
        Individual mother = new Individual(2);

        System.out.println("Father before cross: \n\n " + father.toString());
        System.out.println("Mother before cross: \n\n " + mother.toString());

        NeuralNetwork[] children = crossover(father.getPerceptron(), mother.getPerceptron());

        System.out.println("Father after cross: \n\n " + father.toString());
        System.out.println("Mother after cross: \n\n " + mother.toString());

        for(int i=0; i<children.length;i++){
            System.out.println("Child # " + i + ":\n" + children[i].toString());
        }
        return true;
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

        try {
            copyFile(RefactoGeneticAlgorithm.POPULATION_DIRECTORY + RefactoGeneticAlgorithm.INDIVIDUAL_FILENAME
                    + index + ".xml", RefactoGeneticAlgorithm.ROBOT_DIRECTORY + Darwini.PERCEPTRON_FILE);

            // Launch the test in Robocode
            Runtime.getRuntime().exec("java -Xmx512M -DNOSECURITY=true -DWORKINGDIRECTORY=data -cp "
                    + RefactoGeneticAlgorithm.ROBOCODE_PATH + " robocode.Robocode -nosound -nodisplay -battle "
                    + RefactoGeneticAlgorithm.BATTLE_PATH + " -results "
                    + RefactoGeneticAlgorithm.RESULTS_PATH).waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        evaluate();

    }

    private void evaluate() throws IllegalStateException{

        // RECOVERING DATA FROM RESULTS.TXT
        try {
            Stream<String> stream = Files.lines(Paths.get(RefactoGeneticAlgorithm.RESULTS_PATH));

            final Optional<String> result = stream.filter(line -> line.contains("Darwini"))
                    .findFirst();
            if (!result.isPresent()){
                throw new IllegalStateException("No Darwini's line found in the result's file !");
            }

            String[] results = result.get().split("\t");
            Matcher m = Pattern.compile("(\\d+)\\s\\((\\d+)").matcher(results[1]);
            if (!m.find()){
                throw new IllegalStateException("Unable to find data matching regex in the result's file");
            }

            int totalScore = Integer.parseInt(m.group(1));
            int victory = Integer.parseInt(m.group(2));
            int survival = Integer.parseInt(results[2]);
            int survivalBonus = Integer.parseInt(results[3]);
            int bulletDamage = Integer.parseInt(results[4]);
            int bulletBonus = Integer.parseInt(results[5]);
            int ramDamage = Integer.parseInt(results[6]);
            int ramBonus = Integer.parseInt(results[7]);


            // RECOVERING DATA FROM ACCURACY.TXT
            try {
                Stream<String> streamAcc = Files.lines(Paths.get("accuracy.txt"));
                Optional<String> accuracy = streamAcc.filter(line -> line.contains("accuracy"))
                        .findFirst();
                if (!accuracy.isPresent()){
                    throw new IllegalStateException("No Accuracy found in the accuracy's file !");
                }
                String[] acc = accuracy.get().split("\t");
                Matcher macc = Pattern.compile("(\\d+)\\s*(\\d+)").matcher(acc[1]);
                if (!macc.find()){
                    throw new IllegalStateException("Unable to find data maching regex in the accuracy's file");
                }

                int hits = Integer.parseInt(macc.group(1));
                int missed = Integer.parseInt(macc.group(2));



                // RECOVERING DATA FROM DODGE.TXT
                try{
                    Stream<String> streamDod = Files.lines(Paths.get("dodge.txt"));
                    Optional<String> dodge = streamDod.filter(line -> line.contains("dodge"))
                            .findFirst();
                    if (!dodge.isPresent()){
                        throw new IllegalStateException("No dodge found in the dodge's file");
                    }
                    String[] dod = dodge.get().split("\t");
                    Matcher mdod = Pattern.compile("(\\d+)\\s*(\\d+)").matcher(dod[1]);
                    if (!mdod.find()){
                        throw new IllegalStateException("Unable to find data matching regex in the dodge's file");
                    }

                    int hitsWall = Integer.parseInt(mdod.group(1));
                    int hitByBullet = Integer.parseInt(mdod.group(2));

                    /*fitness = 20 * bulletDamage
                            + 10 * survival
                            + ramDamage
                            + 5*totalScore
                            + victory
                            - 1000*hitsWall
                            - 5*hitByBullet*/

                    fitness = 100 * hits
                            + 20 * (hits + missed)
                            - 15 * missed;

                    System.out.println("Fitness of robot " + index + " : " + fitness);


                } catch(IOException e){
                    System.err.println("Unable to open the dodge's file...");
                }


            } catch (IOException e){
                System.err.println("Unable to open the accuracy's file...");
            }


        } catch (IOException e){
            System.err.println("Unable to open the result's file...");
        }
        
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

    /*	----- UTIL -----	*/

    @Override
    public int compareTo(Object o) {
        Individual ind = (Individual)o;
        return Double.compare(fitness, ind.fitness);
    }

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
        ;;
    }

    public String toString(){

        String str = "========================================================";

        str += "Individual #" + this.index + "\n\n";
        str += "Fitness: " + this.fitness;
        str += "Perceptron: \n\n" + this.perceptron.toString();

        str += "\n========================================================";

        return str;

    }

}
