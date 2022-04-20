package snakeladder.game;

import ch.aplu.jgamegrid.Location;

import java.util.List;

public class BasicStrategy extends Strategy{
    public BasicStrategy(int dice_number, List<Connection> connections, GamePane gp){
        super(dice_number, connections, gp);
    }

    private int findEventCount(int count, int sum){
        if(count == dice_number){
            if(sum == 0){
                return 1;
            }
            return 0;
        } else if(sum <= 0){
            return 0;
        }

        int eventCount = 0;
        for(int i=1; i<=DICE_SIZE; i++){
            eventCount += findEventCount(count+1, sum-i);
        }

        return eventCount;
    }

    public Boolean getResult(){
        int min = dice_number;
        int max = DICE_SIZE*dice_number;
        int snakeEvents=0, ladderEvents=0;
        Location locTemp = null;
        int cellIndex = 0;
        int snakeCount = 0, ladderCount = 0;

        for (Connection con : connections) {
            for (int i=min; i<max; i++){
                cellIndex = i+puppet.getCellIndex();
                locTemp = gp.cellToLocation(cellIndex);
                if(con.locStart.equals(locTemp) || con.locEnd.equals(locTemp)){
                    if(con instanceof Snake){
                        snakeCount++;
                        snakeEvents += findEventCount(0, i);
                    } else {
                        ladderCount++;
                        ladderEvents += findEventCount(0, i);
                    }
                }
            }
        }
        System.out.println("cell index: " + puppet.getCellIndex());
        System.out.println("ladder events("+ladderCount+"): " + ladderEvents);
        System.out.println("snake events("+snakeCount+"): " + snakeEvents);
        if(ladderEvents < snakeEvents){
            return false;
        }
        return true;
    }
}
