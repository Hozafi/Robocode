/*
 * Projet Darwini - Étude Pratique
 *
 * Development of an IA based on genetic algorithms and neural networks.
 *
 * class Darwini.java
 */

package controller;

import model.acquisition.AcquisitionData;
import model.perceptron.OutputData;
import model.perceptron.NeuralNetwork;

import robocode.*;

import javax.xml.stream.XMLStreamException;
import java.awt.*;
import java.io.*;

import static robocode.util.Utils.normalRelativeAngleDegrees;

/**
 * <p>
 * A robot based on an existing one, however this one will improve itself over time, by building and following a neural network.
 * Must extend an operational robot extending AdvancedRobot
 * </p>
 *
 * @author BOIZUMAULT Romain
 * @author BUSSENEAU Alexis
 * @author GEFFRAULT Luc
 * @author MATHIEU Vianney
 * @author VAILLAND Guillaume
 * @author Beaulieu Simon
 * @author Goubet Martin
 * @author Estevany Raphael
 * @author Serano Edgar
 * @author Rebout Etienne
 * @author Haus Daniel
 * @author Barroux Antoine
 * @author Gilbert Auriane
 * @version 2.0 - 29/11/17
 */

public class Darwini extends AdvancedRobot {

    /*	----- ATTRIBUTES -----	*/
    /**
     * The number of shot
     * <p>
     * This attribute is incremented if the robot shoots.
     * </p>
     */
    public static int nbHits = 0;

    /**
     * The number of missed shots
     * <p>
     * This attribute is incremented if the robot misses.
     * </p>
     */
    public static int nbMissed = 0;
    /**
     * <p>
     * The total of bullets that hit our robot
     * </p>
     */
    public static int nbHitByBullet = 0;

    /**
     * <p>
     * The total of walls that our robot hit
     * </p>
     */
    public static int nbHitWall = 0;

    /**
     * The NeuralNetwork xml file
     * <p>
     * <p>
     * The file which contains the perceptron's weighting coefficients that our Darwini robot will use.
     * This file is charged during the perceptron creation
     * </p>
     *
     * @see NeuralNetwork
     * @see controller.Darwini#run()
     */
    public static final String PERCEPTRON_FILE = "Perceptron.xml";

    /**
     * The object "AcquisitionData"
     * <p>
     * <p>
     * Thanks to this object, we will be able to collect the environment data of the robot (used as entries in
     * the perceptron) every turn.
     * </p>
     * <p>
     * This object is initialized in Darwini's run function and called every time Darwini has to make a decision
     * (Every time it scans an enemy)
     * </p>
     *
     * @see AcquisitionData
     * @see controller.Darwini#run()
     * @see controller.Darwini#onScannedRobot(ScannedRobotEvent)
     */
    private AcquisitionData acquisitionData;

    /**
     * The perceptron "neuralNetworkShoot"
     * <p>
     * <p>
     * This is the perceptron used in the Darwini's decision process.
     * </p>
     *
     * @see NeuralNetwork
     * @see controller.Darwini#run()
     * @see controller.Darwini#onScannedRobot(ScannedRobotEvent)
     */
    private NeuralNetwork perceptron;

    /**
     * The OutputData "decisions"
     * <p>
     * <p>
     * This object collect all the perceptron output data which and allowed us to get them more simply throw
     * gets methods
     * </p>
     *
     * @see OutputData
     * @see controller.Darwini#onScannedRobot(ScannedRobotEvent)
     */
    private OutputData decisions;
    private ScannedRobotEvent ennemy;

    private double absoluteBearing;
    private double bearingFromGun;

    /*	----- OTHER METHODS -----	*/

    /**
     * The run methods
     * <p>
     * <p>
     * We initialized the Darwini's perceptron which will take all decisions and the object acquisitionData which will
     * be collecting, every turns, the environment data needed in the decision process.
     * </p>
     *
     * @see controller.Darwini#perceptron
     * @see controller.Darwini#acquisitionData
     * @see controller.Darwini#PERCEPTRON_FILE
     */
    @Override
    public void run() {

        setBodyColor(Color.black);
        setGunColor(Color.gray);
        setRadarColor(Color.red);
        setScanColor(Color.red);

        perceptron = new NeuralNetwork(getDataFile(PERCEPTRON_FILE)); // gets the perceptron given in the population directory (was in "out/...") directory before)
        System.out.println("test");

        acquisitionData = new AcquisitionData(this);
        // MUST be called after because the initial strategy can have an infinite loop.
        // super.run();

        while (true) {
            // If we can't see the enemy robot, turn the gun to the right
            System.out.println("scan");
            turnGunLeft(360);
            //decisions = perceptron.train(acquisitionData.acquisition(ennemy));


            /*if (decisions.getTurnRight() > 0)
                turnRightRadians(2 * Math.PI * sigmoid(decisions.getTurnRight()));

            if (decisions.getTurnLeft() > 0)
                turnLeftRadians(2 * Math.PI * sigmoid(decisions.getTurnLeft()));

            if (decisions.getTurnRadarRight() > 0) {
                turnRadarRightRadians(2 * Math.PI * sigmoid(decisions.getTurnRadarRight()));
                turnGunRightRadians(2 * Math.PI * sigmoid(decisions.getTurnRadarRight()));
            }

            if (decisions.getTurnRadarRight() > 0 && decisions.getTurnGunRight() > 0) {
                double angle = 2 * Math.PI * sigmoid(decisions.getTurnRadarRight());
                turnRadarRightRadians(angle);
                turnGunRightRadians(angle);

            }
            if (decisions.getTurnRadarLeft() > 0 && decisions.getTurnGunLeft() > 0) {
                double angle = 2 * Math.PI * sigmoid(decisions.getTurnRadarLeft());
                turnRadarLeftRadians(angle);
                turnGunLeftRadians(angle);
            }*/

            /*if (decisions.getTurnGunRight() > 0) {
                double angle = 2 * Math.PI * sigmoid(decisions.getTurnGunRight());
                turnRadarRight(angle);
                turnGunRight(angle);
            }

            if (decisions.getTurnGunRight() > 0)
                turnGunRightRadians(2 * Math.PI * sigmoid(decisions.getTurnGunRight()));

            if (decisions.getTurnGunLeft() > 0)
                turnGunLeftRadians(2 * Math.PI * sigmoid(decisions.getTurnGunLeft()));


            if (decisions.getTurnGunLeft() > 0) {
                double angle = 2 * Math.PI * sigmoid(decisions.getTurnRadarRight());
                turnGunLeft(angle);
                turnRadarLeft(angle);
            }
            */

            /*if (decisions.getMoveAhead() > 0)
                ahead(10 * sigmoid(decisions.getMoveAhead()));
            if (sigmoid(decisions.getShoot()) > 0) {
                fire(3);
            }*/
        }

    }

    public void onScannedRobot(ScannedRobotEvent e) {
        ennemy = e;
        System.out.println("onScannedRobot debut");
        // Calculate exact location of the robot
        absoluteBearing = getHeading() + e.getBearing();
        bearingFromGun = normalRelativeAngleDegrees(absoluteBearing - getGunHeading());

        // If it's close enough, fire!
        if (Math.abs(bearingFromGun) <= 3) {
            turnGunRight(bearingFromGun);
            // We check gun heat here, because calling fire()
            // uses a turn, which could cause us to lose track
            // of the other robot.
			/*if (getGunHeat() == 0) {
				fire(Math.min(3 - Math.abs(bearingFromGun), getEnergy() - .1));
			}*/
        } // otherwise just set the gun to turn.
        // Note:  This will have no effect until we call scan()
        else {
            turnGunRight(bearingFromGun);
        }
        // Generates another scan event if we see a robot.
        // We only need to call this if the gun (and therefore radar)
        // are not turning.  Otherwise, scan is called automatically.
        /*if (bearingFromGun == 0) {
            scan();
        }*/

        decisions = perceptron.train(acquisitionData.acquisition(e));

        System.out.println("coucou je suis en train de combattre onScannedRobot fin");

        if (decisions.getMoveAhead() > 0)
            ahead(10 * sigmoid(decisions.getMoveAhead()));
        if (sigmoid(decisions.getShoot()) > 0) {
            fire(3);
        }
        scan();
    }


    @Override
    public void onBulletHit(BulletHitEvent e) {
        super.onBulletHit(e);
        nbHits++;
    }

    @Override
    public void onBulletMissed(BulletMissedEvent e) {
        super.onBulletMissed(e);
        nbMissed++;
    }


    @Override
    public void onHitByBullet(HitByBulletEvent e) {
        super.onHitByBullet(e);
        nbHitByBullet++;
    }

    @Override
    public void onHitWall(HitWallEvent e) {
        super.onHitWall(e);
        nbHitWall++;
    }


    @Override
    public void onBattleEnded(BattleEndedEvent event) {
        super.onBattleEnded(event);

        try (FileWriter fw = new FileWriter("accuracy.txt");
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw);) {
            out.println("accuracy" + "\t" + nbHits + " " + nbMissed + "\n");
            if (bw != null)
                bw.close();
            if (fw != null)
                fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileWriter fw = new FileWriter("dodge.txt");
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw);) {
            out.println("dodge" + "\t" + nbHitWall + " " + nbHitByBullet + "\n");
            if (bw != null)
                bw.close();
            if (fw != null)
                fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>
     * Apply the sigmoid on the specified value.
     * </p>
     *
     * @param i the value to apply the sigmoid
     * @return the value after the sigmoid computation
     */
    private double sigmoid(double i) {
        System.out.println("SIGMOID : i = " + i);
        System.out.println("SIGMOID VALEUR = " + 1 / (1 + Math.exp(i)));
        // Code sigmoid
        return 1 / (1 + Math.exp(i));
        // Code RELU
        //return Math.max(0,i);
    }
}


/*

    /**
     * The reaction of Darwini when it has scanned an enemy
     *
     * <p>
     * We load the environment data (thanks to acquisitionData) in the Darwini's perceptron and collect the different
     * perceptron decisions to act
     * </p>
     *
     * @param e the scanned robot
     *
     * @see controller.Darwini#acquisitionData
     * @see controller.Darwini#decisions
     * @see controller.Darwini#perceptron
     */
  /*  @Override
    public void onScannedRobot(ScannedRobotEvent e) {
			/*decisions = perceptron.train( acquisitionData.acquisition(e));

			if (decisions.getShoot() > 0) {
				fire(3);
			}

			if (decisions.getTurnRight() > 0)
				turnRightRadians(2 * Math.PI * sigmoid(decisions.getTurnRight()));

			if (decisions.getTurnLeft() > 0)
				turnLeftRadians(2 * Math.PI * sigmoid(decisions.getTurnLeft()));*/

			/*if (decisions.getTurnRadarRight() > 0) {
				turnRadarRightRadians(2 * Math.PI * sigmoid(decisions.getTurnRadarRight()));
				turnGunRightRadians(2 * Math.PI * sigmoid(decisions.getTurnRadarRight()));=======
			/*if (decisions.getTurnRadarRight() > 0 && decisions.getTurnGunRight() > 0) {
				double angle = 2 * Math.PI * sigmoid(decisions.getTurnRadarRight());
				turnRadarRightRadians(angle);
				turnGunRightRadians(angle);

			}
			if (decisions.getTurnRadarLeft() > 0 && decisions.getTurnGunLeft() > 0) {
				double angle = 2 * Math.PI * sigmoid(decisions.getTurnRadarLeft());
				turnRadarLeftRadians(angle);
				turnGunLeftRadians(angle);
			}*/

			/*if (decisions.getTurnGunRight() > 0) {
				double angle = 2 * Math.PI * sigmoid(decisions.getTurnGunRight());
				turnRadarRight(angle);
				turnGunRight(angle);
			}


			if (decisions.getTurnGunRight() > 0)
				turnGunRightRadians(2 * Math.PI * sigmoid(decisions.getTurnGunRight()));

			if (decisions.getTurnGunLeft() > 0)
				turnGunLeftRadians(2 * Math.PI * sigmoid(decisions.getTurnGunLeft()));

			if (decisions.getTurnGunLeft() > 0) {
				double angle = 2 * Math.PI * sigmoid(decisions.getTurnRadarRight());
				turnGunLeft(angle);
				turnRadarLeft(angle);
			}*/


			/*if (decisions.getMoveAhead() > 0)
				ahead(10 * sigmoid(decisions.getMoveAhead()));*/
//}








