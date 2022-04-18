package snakeladder.game;

import ch.aplu.jgamegrid.Location;

import java.util.List;

public abstract class Strategy {
    Location locStart;
    int cellStart;

    Strategy(Location location, List<Connection> connections)
    {
        this.cellStart = cellStart;
        locStart = GamePane.cellToLocation(cellStart);
    }
}it
