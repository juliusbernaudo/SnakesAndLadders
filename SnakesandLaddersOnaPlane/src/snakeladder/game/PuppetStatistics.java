package snakeladder.game;

import java.util.HashMap;
import java.util.Set;

public class PuppetStatistics extends Statistics {

    // Formatting the output into a singular string
    public String formatRollsOutput() {
        String output = new String();
        Set<Integer> keys = rolls.keySet();
        Integer[] rollsKeys = keys.toArray(new Integer[0]);

        for (int i = 0; i < rollsKeys.length; i++) {
            output = output.concat(rollsKeys[i].toString() + "-" + rolls.get(rollsKeys[i]).toString());
            if (i+1 != rollsKeys.length) {
                output = output.concat(", ");
            }
        }
        return output;
    }

    public String formatPathsOutput() {
        String output = new String();
        Set<String> keys = paths.keySet();
        String[] pathsKeys = keys.toArray(new String[0]);

        for (int i = 0; i < pathsKeys.length; i++) {
            output = output.concat(pathsKeys[i] + "-" + paths.get(pathsKeys[i]).toString());
            if (i+1 != pathsKeys.length) {
                output = output.concat(", ");
            }
        }
        return output;
    }
}
