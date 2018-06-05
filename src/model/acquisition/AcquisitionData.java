/*
 * Projet Darwini - Étude Pratique
 *
 * Development of an IA based on genetic algorithms and neural networks.
 *
 * class AcquisitionData.java
 */

package model.acquisition;

import model.perceptron.InputData;
import robocode.AdvancedRobot;
import robocode.Rules;
import robocode.ScannedRobotEvent;
import sun.rmi.runtime.Log;

import java.util.logging.Logger;

/**
 * <p>
 * This object is called by a robot in order to create a new InputData with reduced input values.
 * The values are reduced thanks to maximums which are the AcquisitionData's attribute.
 * This reduction is necessary in order to get entries values between -1 and 1 for our perceptron.
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
 * @version 1.1 - 28/03/17
 */
public class AcquisitionData {

    /*	----- ATTRIBUTES -----	*/

    /**
     * <p>
     * The maximal angle for the bearing
     * </p>
     *
     * @see InputData
     */
    private static final double MAX_BEARING = Math.PI;

    /**
     * <p>
     * The maximal value for any other angle than the bearing
     * </p>
     *
     * @see InputData
     */
    private static final double MAX_ANGLE = 2 * Math.PI;

    /**
     * <p>
     * The maximal value of robot's energy
     * </p>
     *
     * @see InputData
     */
    private static final double MAX_ENERGY = 100.0;

    /**
     * <p>
     * The maximal width (corresponding to the battlefield width)
     * </p>
     *
     * @see InputData
     */
    private double maxWidth;

    /**
     * <p>
     * The maximal height (corresponding to the battlefield height)
     * </p>
     *
     * @see InputData
     */
    private double maxHeight;

    /**
     * <p>
     * The maximal Distance between two robots
     * </p>
     *
     * @see InputData
     */
    private double maxDistance;

    /**
     * <p>
     * Our robot. The robot which has called the creation of Acquisition Data.
     * </p>
     *
     * @see controller.Darwini
     * @see controller.AcquisitionBot
     */
    private AdvancedRobot myRobot;

    /**
     * <p>
     * The opponent Robot, The robot that our robot has scanned.
     * </p>
     *
     * @see controller.Darwini
     * @see controller.AcquisitionBot
     */
    private ScannedRobotEvent opponentRobot;


    /*	----- CONSTRUCTOR -----	*/

    /**
     * <p>
     * We just need to set 3 AcquisitionData attributes. The other maximum are known.
     * </p>
     *
     * @param myRobot The robot which has called the creation of Acquisition Data; our robot.
     */
    public AcquisitionData(AdvancedRobot myRobot) {
        this.myRobot = myRobot;
        maxWidth = myRobot.getBattleFieldWidth();
        maxHeight = myRobot.getBattleFieldHeight();
        maxDistance = Math.sqrt(Math.pow(maxWidth, 2) + Math.pow(maxHeight, 2));
    }


    /*	----- OTHER METHODS -----	*/

    /**
     * Launch a InputData creation with all the parameters necessary
     *
     * @param opponentRobot The robot that our robot has scanned.
     * @return an InputData initialized with the reduced environment data
     */
    public InputData acquisition(ScannedRobotEvent opponentRobot) {
        this.opponentRobot = opponentRobot;
        opponentRobot.getEnergy();

        /*return new InputData(
                getMyBearing(),
                getDistance(),
                getMyEnergy(),
                getOpponentVelocity(),
                getMyVelocity(),
                getOpponentHeading(),
                getMyHeading(),
                getMyRadarHeading(),
                getMyGunHeading(),
                getOpponentX(),
                getMyX(),
                getOpponentY(),
                getMyY(),
                getDistWall()
        );*/

        return new InputData(getMyBearing(),getGunBearing(),getDistance(),getMyVelocity(),getDistWall());
    }

    /**
     * Reduced the specified value with its maximum
     *
     * @param value the value to reduce
     * @param max   the maximu of the value
     * @return the value reduced
     */
    private double reduce(double value, double max) {
        return (2.0 * value - max) / max;
    }


    /*	----- ACQUISITION METHODS -----	*/

    /**
     * @return The reduced bearing
     * @see InputData
     */
    private double getMyBearing() {
        return opponentRobot.getBearingRadians() / MAX_BEARING;
    }

    /**
     * @return The reduced heading of our gun
     * @see InputData
     */
    private double getGunBearing() {
        return reduce((myRobot.getHeadingRadians()-opponentRobot.getBearingRadians()-myRobot.getGunHeadingRadians()), MAX_ANGLE);
    }

    /**
     * @return The reduced distance
     * @see InputData
     */
    private double getDistance() {
        return reduce(opponentRobot.getDistance(), maxDistance);
    }

    /**
     * @return The reduced energy of our robot
     * @see InputData
     */
    private double getMyEnergy() {
        return reduce(myRobot.getEnergy(), MAX_ENERGY);
    }

    /**
     * @return The reduced opponent's velocity
     * @see InputData
     */
    private double getOpponentVelocity() {
        return reduce(opponentRobot.getVelocity(), Rules.MAX_VELOCITY);
    }

    /**
     * @return The reduced velocity of our robot
     * @see InputData
     */
    private double getMyVelocity() {
        return reduce(myRobot.getVelocity(), Rules.MAX_VELOCITY);
    }

    /**
     * @return The reduced opponent's heading
     * @see InputData
     */
    private double getOpponentHeading() {
        return reduce(opponentRobot.getHeadingRadians(), MAX_ANGLE);
    }

    /**
     * @return The reduced heading of our robot
     * @see InputData
     */
    private double getMyHeading() {
        return reduce(myRobot.getHeadingRadians(), MAX_ANGLE);
    }

    /**
     * @return The reduced heading of our robot's radar
     * @see InputData
     */
    private double getMyRadarHeading() {
        return reduce(myRobot.getRadarHeadingRadians(), MAX_ANGLE);
    }

    /**
     * @return The reduced heading of our robot's gun
     * @see InputData
     */
    private double getMyGunHeading() {
        return reduce(myRobot.getGunHeadingRadians(), MAX_ANGLE);
    }

    /**
     * @return The opponent's x position
     * @see InputData
     */
    private double getOpponentX() {
        return reduce(myRobot.getX() + opponentRobot.getDistance() * Math.sin(myRobot.getHeadingRadians() + opponentRobot.getBearingRadians()), maxWidth);
    }

    /**
     * @return The x position of our robot's
     * @see InputData
     */
    private double getMyX() {
        return reduce(myRobot.getX(), maxWidth);
    }

    /**
     * @return The opponent's y position
     * @see InputData
     */
    private double getOpponentY() {
        return reduce(myRobot.getY() + opponentRobot.getDistance() * Math.cos(myRobot.getHeadingRadians() + opponentRobot.getBearingRadians()), maxHeight);
    }

    /**
     * @return The y position of our robot's
     * @see InputData
     */
    private double getMyY() {
        return reduce(myRobot.getY(), maxHeight);
    }

    private double getDistWall() {
        double angle = getMyHeading();
        double northWestAngle = Math.atan(myRobot.getX() / (maxHeight - myRobot.getY()));
        double northEastAngle = Math.atan(maxWidth - myRobot.getX() / (maxHeight - myRobot.getY()));
        double southEastAngle = Math.atan(myRobot.getY() / (maxWidth - myRobot.getX()));
        double southWestAngle = Math.atan(myRobot.getY() / (myRobot.getX()));

        if (angle >= 2 * Math.PI - northWestAngle && angle < 2 * Math.PI || angle >= 0 && angle <= northEastAngle)
            return reduce(maxHeight - myRobot.getY(), maxHeight);

        if (angle > northWestAngle && angle <= southEastAngle + Math.PI / 2)
            return reduce(maxWidth - myRobot.getX(), maxHeight);

        if (angle > southEastAngle + Math.PI / 2 && angle <= southWestAngle + Math.PI)
            return reduce(myRobot.getY(), maxHeight);
        else
            return reduce(myRobot.getX(), maxWidth);
    }
}