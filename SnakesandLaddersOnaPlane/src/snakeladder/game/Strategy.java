package snakeladder.game;

import ch.aplu.jgamegrid.Location;

import java.util.List;

public abstract class Strategy {
    final int DICE_SIZE = 6;
    int dice_number;
    int cellStart;
    GamePane gp;
    Puppet puppet;
    Location locStart;
    List<Connection> connections;

    Strategy(int dice_number, List<Connection> connections, GamePane gp)
    {
        this.dice_number = dice_number;
        this.connections = connections;
        this.gp = gp;
        this.puppet = gp.getPuppet();
    }

    public Boolean getResult(){return Boolean.TRUE;}
}
