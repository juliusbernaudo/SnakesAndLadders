package snakeladder.game;

import java.util.List;

public class BasicStrategy extends Strategy{
    public BasicStrategy(int diceNumber, List<Connection> connections, GamePane gp){
        super(diceNumber, connections, gp);
    }

    private int findEventCount(int count, int sum){
        if(count == diceNumber){
            if(sum == 0){
                return 1;
            }
            return 0;
        } else if(sum <= 0){
            return 0;
        }

        int eventCount = 0;
        for(int i = 1; i<= diceSize; i++){
            eventCount += findEventCount(count+1, sum-i);
        }

        return eventCount;
    }

    public Boolean getResult(){
        int min = diceNumber;
        int max = diceSize * diceNumber;
        int currentUp=0, currentDown=0;
        double currentProb = 0;
        int alternativeUp = 0, alternativeDown = 0;
        double alternativeProb = 0;
        int ways = 0;
        int cellIndex = 0;

        for (Connection con : connections) {
            for (int i=min; i<max+1; i++){
                cellIndex = i+puppet.getCellIndex();
                if(con.cellStart == cellIndex) {
                    // current possible ways
                    ways = findEventCount(0, i);
                    if(con.cellStart <= con.cellEnd){
                        // Going up
                        currentUp += ways;
                    } else {
                        // Going down
                        currentDown += ways;
                    }
                }
                if(con.cellEnd == cellIndex) {
                    // possible ways when toggle switched
                    ways = findEventCount(0, i);
                    if(con.cellEnd <= con.cellStart){
                        // Going up
                        alternativeUp += ways;
                    } else {
                        // Going down
                        alternativeDown += ways;
                    }
                }
            }
        }
        currentProb = Double.valueOf(currentUp) / (currentUp+currentDown+1);
        alternativeProb = Double.valueOf(alternativeUp) / (alternativeUp+alternativeDown+1);

        System.out.println("cell index: " + puppet.getCellIndex());
        System.out.println("(current events) up: "+currentUp+
                ", down: " + currentDown);
        System.out.println("(alternative events) up: "+alternativeUp+
                ", down: " + alternativeDown);
        if(currentProb > alternativeProb){
            return true;
        }
        return false;
    }
}
