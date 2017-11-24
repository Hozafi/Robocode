/*
 * Projet Darwini - Étude Pratique
 *
 * Development of an IA based on genetic algorithms and neural networks.
 *
 * class Score.java
 */

package model.genetic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 *  <p>
 *     Class called by the Genetic algorithm after the ten games of the individuals. It creates an object Score for every individuals (robots)
 *     generate by the genetic algorithm. It allows us to compare the efficiency of those robots.
 *  </p>
 *
 * @see GeneticAlgorithm
 * @see NaturalSelection
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
public class Score implements Comparable<Score> {

    /*	----- ATTRIBUTES -----	*/

		/**
		 *  <p>
		 *     The robot's total score at the exit of robocode
		 *  </p>
		 */
		private int totalScore;

        /**
         *  <p>
         *      total score taking the importance of every value
         *  </p>
         */

		private int weightedScore;

		/**
         *  <p>
		 *     The robot's number of victories and its percentage
         *  </p>
         */
        private int victory;

        /**
         * <p>
		 *     The survival score for the robot in the battle
         * </p>
         */
        private int survival;

        /**
         * <p>
		 *     The last survivor bonus for the robot in the battle
         * </p>
         */
        private int survivalBonus;

        /**
         *  <p>
		 *     The bullet damage score for the robot in the battle
         *  </p>
         */
        private int bulletDamage;


        /**
         * <p>
		 *     The bullet damage bonus for the robot in the battle
         * </p> 
         */
        private int bulletBonus;

        /**
         * <p>
		 *     The ramming damage for the robot in the battle
         * </p>
         */
        private int ramDamage;

        /**
         * <p>
		 *     The ramming damage bonus for the robot in the battle
         * </p>
         */
        private int ramBonus;

        /**
         * <p>
         *     The total of bullets that hit the enemy robot
         * </p>
         */
        private int hits;

        /**
         * <p>
         *     The total of bullets that missed the enemy robot
         * </p>
         */
        private int missed;

	/**
         * <p>
         *     The total of wall that our robot hit
         * </p>
         */
        private int hitsWall;

	/**
         * <p>
         *     The total of bullets that hit our robot
         * </p>
         */
        private int hitByBullet;

	/*	----- CONSTRUCTOR -----	*/

        /**
         * <p>
		 *     The construction of this object is based on the file we get when the games are over.
         *     It represents the different criteria to compare two robots.
         * </p>
		 *
         * @param fileName the filepath of the score file providing by Robocode
		 * @param robotName the name of the robot to compare
         */
        public Score(String fileName, String robotName) {
            try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
                String[] results = stream.filter(line -> line.contains(robotName)).findFirst().get().split("\t");
                Matcher m = Pattern.compile("(\\d+)\\s\\((\\d+)").matcher(results[1]);
                m.find();
                totalScore = Integer.parseInt(m.group(1));
                victory = Integer.parseInt(m.group(2));
                survival = Integer.parseInt(results[2]);
                survivalBonus = Integer.parseInt(results[3]);
                bulletDamage = Integer.parseInt(results[4]);
                bulletBonus = Integer.parseInt(results[5]);
                ramDamage = Integer.parseInt(results[6]);
                ramBonus = Integer.parseInt(results[7]);

                Stream<String> streamAcc = Files.lines(Paths.get("accuracy.txt"));
                String[] acc = streamAcc.filter(line -> line.contains("accuracy")).findFirst().get().split("\t");
                Matcher macc = Pattern.compile("(\\d+)\\s*(\\d+)").matcher(acc[1]);
                macc.find();
                hits = Integer.parseInt(macc.group(1));
                missed = Integer.parseInt(macc.group(2));

                Stream<String> streamDod = Files.lines(Paths.get("dodge.txt"));
                String[] dod = streamDod.filter(line -> line.contains("dodge")).findFirst().get().split("\t");
                Matcher mdod = Pattern.compile("(\\d+)\\s*(\\d+)").matcher(dod[1]);
                mdod.find();
                hitsWall = Integer.parseInt(mdod.group(1));
                hitByBullet = Integer.parseInt(mdod.group(2));

                //System.out.println("Dodge:" +  (hitByBullet + hitsWall));
                if (hits+missed > 0){
                    weightedScore = 6 * bulletDamage + 6 * survival  + ramDamage + 300 * (hits / (hits + missed));
                } else{
                    weightedScore = 6 * bulletDamage + 6 * survival  + ramDamage;
                }
                System.out.println("Fitness : " + weightedScore);
            } catch (IOException e) {
                System.out.println("The results file is not found");
            }
        }


    /*	----- OTHER METHOD -----	*/

        /**
         * <p>
         *     Method used to compare two robots thanks to their score. We choose the percentage of victory as 
		 *	   main selection criterion. Then if two robots have the same percentage, we choose the one which
		 * 	   made the more damages.
         * </p>
		 *
         * @param o the score of the second robot
         */

        @Override       // WEIGHTED TOTAL
        public int compareTo(Score o) {
            if (weightedScore > o.weightedScore)
                return 1;
            else if (weightedScore == o.weightedScore)
                if (victory > o.victory)
                    return 1;
                else if (victory == o.victory)
                    return 0;

            return -1;
        }
    public String toString()
    {
        return "Total robocode: " + this.totalScore+ "  //  weighted total: "+ this.weightedScore;
    }
}
