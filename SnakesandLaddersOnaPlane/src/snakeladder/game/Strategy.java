package snakeladder.game;

import ch.aplu.jgamegrid.Location;

import java.util.List;

public abstract class Strategy {
    final int diceSize = 6;
    int diceNumber;
    GamePane gp;
    Puppet puppet;
    List<Connection> connections;

    Strategy(int diceNumber, List<Connection> connections, GamePane gp)
    {
        this.diceNumber = diceNumber;
        this.connections = connections;
        this.gp = gp;
        this.puppet = gp.getPuppet();
    }

    public Boolean getResult(){return false;}
}
