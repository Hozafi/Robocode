package controller;

import model.perceptron.NeuralNetwork;
import robocode.AdvancedRobot;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileNotFoundException;

import static controller.Darwini.PERCEPTRON_FILE;

public class NTM {
    public static void main(String[] args) {
        File vatefaire = new File("/home/etienne/workspace_Java/Darwini/out/production/Darwini/controller/Darwini.data/"+PERCEPTRON_FILE);
        NeuralNetwork perceptron = new NeuralNetwork(vatefaire); // gets the perceptron given in the population directory (was in "out/...") directory before)
        File f = new File("/home/etienne/workspace_Java/Darwini/test.xml");
        try {
            perceptron.printToXML(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
}
