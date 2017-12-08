package controller;

import java.io.IOException;

public class TestBattle {

    public static void main(String[] args) {
        String ROBOCODE_PATH = "libs/robocode.jar";
        String BATTLE_PATH = "data/test.battle";
        String RESULTS_PATH = "results.txt";
        try {
            // Launch the test in Robocode
            Runtime.getRuntime().exec("java -Xmx512M -DNOSECURITY=true -DWORKINGDIRECTORY=data -cp " + ROBOCODE_PATH + " robocode.Robocode -nosound -battle " + BATTLE_PATH).waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
