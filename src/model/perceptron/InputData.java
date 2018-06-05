/*
 * Projet Darwini - Étude Pratique
 *
 * Development of an IA based on genetic algorithms and neural networks.
 *
 * class InputData.java
 */

package model.perceptron;

/**
 * <p>
 * This object is created every turn, filled with the environment data from AcquisitionData and load in the neural network
 * in order to take a decision.
 * The environment data are collected by AcquisitionData when our robot Darwini scanned an other one which will be the "enemy" for this turn.
 * Thanks to those parameters, our robot will take a decision for this turn until the next scanned.
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
 * @see NeuralNetwork
 * @see model.acquisition.AcquisitionData
 * @see controller.Darwini
 * <p>
 * <p>
 * This object is also used in the supervised process to set the perceptron's ponderation coefficient.
 * AcquisitionBot collects the environment data as an example, create a new InputData and stocks it into a SSVM file.
 * Then this file is used in iriselm to create perceptron's weights.
 * </p>
 * @see model.acquisition.AcquisitionData
 * @see controller.AcquisitionBot
 */
public class InputData {


    /*	----- ATTRIBUTES -----	*/

    /**
     * <p>
     * Number of entries
     * </p>
     */
    static final int INPUT_NEURONS = 5;


    /**
     * <p>
     * The success or not of an action (only useful in the supervised process
     * </p>
     */
    private boolean success[];


    /**
     * <p>
     * The direction our robot is going relative to the enemy our robot has scanned
     * </p>
     */
    private double myBearing;

    /**
     * <p>
     * The direction of our gun relative to the enemy our robot has scanned
     * </p>
     */
    private double gunBearing;


    /**
     * <p>
     * The distance to the enemy our robot has scanned
     * </p>
     */
    private double distance;


    /**
     * <p>
     * Our robot's energy
     * </p>
     */
    private double myEnergy;


    /**
     * <p>
     * The enemy's velocity
     * </p>
     */
    private double opponentVelocity;


    /**
     * <p>
     * Our robot velocity
     * </p>
     */
    private double myVelocity;


    /**
     * <p>
     * The direction the enemy is facing
     * </p>
     */
    private double opponentHeading;

    /**
     * <p>
     * The direction our robot is facing
     * </p>
     */
    private double myHeading;

    /**
     * <p>
     * The direction our radar is facing
     * </p>
     */
    private double myRadarHeading;

    /**
     * <p>
     * The direction our gun is facing
     * </p>
     */
    private double myGunHeading;

    /**
     * <p>
     * Our x coordinate
     * </p>
     */
    private double x;

    /**
     * <p>
     * Our y coordinate
     * </p>
     */
    private double y;

    /**
     * <p>
     * The x distance to the enemy
     * </p>
     */
    private double enemyX;

    /**
     * <p>
     * The y distance to our enemy
     * </p>
     */
    private double enemyY;

    private double distWall;


    /*	----- CONSTRUCTOR -----	*/

    /**
     * The InputData constructor
     *
     * @param myBearing        Number of entries
     * @param distance         The success or not of an action (only useful in the supervised process
     * @param myEnergy         The direction our robot is going relative to the enemy our robot has scanned
     * @param opponentVelocity The distance to the enemy our robot has scanned
     * @param myVelocity       Our robot velocity
     * @param opponentHeading  The direction the enemy is facing
     * @param myHeading        The direction our robot is facing
     * @param myGunHeading     The direction our gun is facing
     * @param myRadarHeading   The direction our radar is facing
     * @param x                Our x coordinate
     * @param y                our y coordinate
     * @param enemyX           The x distance to the enemy
     * @param enemyY           The y distance to our enemy
     * @see InputData
     */
    /*public InputData(double myBearing, double distance, double myEnergy, double opponentVelocity,
                     double myVelocity, double opponentHeading, double myHeading,
                     double myRadarHeading, double myGunHeading, double x, double y,
                     double enemyX, double enemyY, double distWall) {
        success = new boolean[OutputData.OUTPUT_NEURONS];
        this.myBearing = myBearing;
        this.distance = distance;
        this.myEnergy = myEnergy;
        this.opponentVelocity = opponentVelocity;
        this.myVelocity = myVelocity;
        this.opponentHeading = opponentHeading;
        this.myHeading = myHeading;
        this.myRadarHeading = myRadarHeading;
        this.myGunHeading = myGunHeading;
        this.x = x;
        this.y = y;
        this.enemyX = enemyX;
        this.enemyY = enemyY;
        this.distWall = distWall;
    }*/

    public InputData(double myBearing, double gunBearing, double distance, double myVelocity, double distWall) {
        this.myBearing = myBearing;
        this.distance = distance;
        this.gunBearing = gunBearing;
        this.myVelocity = myVelocity;
        this.distWall = distWall;
    }


    /*	----- MUTATOR -----	*/

    /**
     * <p>
     * The seter of the Sucess attribute. It's used in the supervised process to set
     * the succes or not of a collected example
     * </p>
     *
     * @param index The index of the success table space that we want to set true
     * @see controller.AcquisitionBot
     * @see model.acquisition.AcquisitionData
     */
    public void setSuccess(int index) throws IllegalArgumentException {
        if (index < 0)
            throw new IllegalArgumentException("The index must be greater than 0");
        if (index >= INPUT_NEURONS)
            throw new IllegalArgumentException("The index must not be greater than or equal to " + INPUT_NEURONS);

        success[index] = true;
    }


    /*	----- OTHER METHODS -----	*/

    /**
     * <p>
     * Procedure to transform an InputData object in a SSVM File usable in iriselm
     * This procedure is useful in the supervised process. Thus, it used the attribute "sucess".
     * </p>
     *
     * @return SSVM File containing the examples collected by AcquisitionBot
     * @see model.acquisition.AcquisitionData
     * @see controller.AcquisitionBot
     */
    public String toSSVM() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < OutputData.OUTPUT_NEURONS; i++)
            if (success[i])
                sb.append("1 ");
            else
                sb.append("-1 ");

        return sb
                .append("1:").append(myBearing)
                .append(" 2:").append(distance)
                .append(" 3:").append(myEnergy)
                .append(" 4:").append(opponentVelocity)
                .append(" 5:").append(myVelocity)
                .append(" 6:").append(opponentHeading)
                .append(" 7:").append(myHeading)
                .append(" 8:").append(myRadarHeading)
                .append(" 9:").append(myGunHeading)
                .append(" 10:").append(x)
                .append(" 11:").append(y)
                .append(" 12:").append(enemyX)
                .append(" 13:").append(enemyY)
                .append(" 14:").append(distWall)
                .toString();
    }

    /**
     * <p>
     * Procedure to transform an InputData object in a Matrix ready to be load in the neural network.
     * This procedure is used by Darwini.
     * </p>
     *
     * @return Matrix containing the actual environments data
     * @see controller.Darwini
     * @see NeuralNetwork
     */
    public Matrix toMatrix() {
        Matrix matrix = new Matrix(1, INPUT_NEURONS);

        /*matrix.set(0, 0, myBearing);
        matrix.set(0, 1, distance);
        matrix.set(0, 2, myEnergy);
        matrix.set(0, 3, opponentVelocity);
        matrix.set(0, 4, myVelocity);
        matrix.set(0, 5, opponentHeading);
        matrix.set(0, 6, myHeading);
        matrix.set(0, 7, myRadarHeading);
        matrix.set(0, 8, myGunHeading);
        matrix.set(0, 9, x);
        matrix.set(0, 10, y);
        matrix.set(0, 11, enemyX);
        matrix.set(0, 12, enemyY);
        matrix.set(0, 13, distWall);*/

        matrix.set(0, 0, myBearing);
        matrix.set(0, 1, distance);
        matrix.set(0, 2, gunBearing);
        matrix.set(0, 3, myVelocity);
        matrix.set(0, 4, distWall);

        return matrix;
    }

}