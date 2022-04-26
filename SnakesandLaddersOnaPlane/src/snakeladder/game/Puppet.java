package snakeladder.game;

import ch.aplu.jgamegrid.*;
import java.awt.Point;
import java.util.*;

public class Puppet extends Actor
{
  private GamePane gamePane;
  private NavigationPane navigationPane;
  private int cellIndex = 0;
  private int nbSteps;
  private Connection currentCon = null;
  private int y;
  private int dy;
  private boolean isAuto;
  private String puppetName;
  private boolean skip = false;
  private boolean movingBack = false;
  private HashMap<Integer, Integer> rolls =  new HashMap<>();
  private HashMap<String, Integer> paths =  new HashMap<>();

  Puppet(GamePane gp, NavigationPane np, String puppetImage)
  {
    super(puppetImage);
    this.gamePane = gp;
    this.navigationPane = np;
  }

  public boolean isAuto() {
    return isAuto;
  }

  public void setAuto(boolean auto) {
    isAuto = auto;
  }

  public String getPuppetName() {
    return puppetName;
  }

  public void setPuppetName(String puppetName) {
    this.puppetName = puppetName;
  }

  void go(int nbSteps)
  {
    if (cellIndex == 100)  // after game over
    {
      cellIndex = 0;
      setLocation(gamePane.startLocation);
    }
    this.nbSteps = nbSteps;
    setActEnabled(true);
  }

  void resetToStartingPoint() {
    cellIndex = 0;
    setLocation(gamePane.startLocation);
    setActEnabled(true);
  }

  int getCellIndex() {
    return cellIndex;
  }

  HashMap<Integer, Integer> getRolls() { return rolls; }

  private void moveToNextCell()
  {
    int tens = cellIndex / 10;
    int ones = cellIndex - tens * 10;
    if (tens % 2 == 0)     // Cells starting left 01, 21, .. 81
    {
      if (ones == 0 && cellIndex > 0)
        setLocation(new Location(getX(), getY() - 1)); // move up one
      else
        setLocation(new Location(getX() + 1, getY())); // move right one
    }
    else     // Cells starting left 20, 40, .. 100
    {
      if (ones == 0)
        setLocation(new Location(getX(), getY() - 1)); // move up one
      else
        setLocation(new Location(getX() - 1, getY())); // move left one
    }
    cellIndex++;
  }

  // Moves puppet back one cell - used for coding change task 3
  public void moveBackACell()
  {
    int tens = cellIndex / 10;
    int ones = cellIndex - tens * 10;
    System.out.println(cellIndex);

    if (tens % 2 == 0)     // Cells starting left 01, 21, .. 81
    {
      if (ones == 1 && cellIndex > 0) // ones == 1 means cellIndex ends with a 1 eg. 21, 41
        setLocation(new Location(getX(), getY() + 1)); // move down one
      else if (ones == 0) // if on 20, 40, ... 80
        setLocation(new Location(getX() + 1, getY())); // move right one
      else
        setLocation(new Location(getX() - 1, getY())); // move left one
    }
    else     // Cells starting left 20, 40, .. 100
    {
      if (ones == 1)
        setLocation(new Location(getX(), getY() + 1)); // move down one
      else if (ones == 0) // on 10, 30, ... 90
        setLocation(new Location(getX() - 1, getY())); // move left one
      else
        setLocation(new Location(getX() + 1, getY())); // move right one
    }
    cellIndex--;

    // after moving back a space check & handle if a player is on a connection
    setActEnabled(true);
    if ((currentCon = gamePane.getConnectionAt(getLocation())) != null)
    {
      connectionMovement();
    }
    else
    {
      setActEnabled(false);
    }
    movingBack = true;
  }
  
  // movement for snake and ladder connections
  private void connectionMovement() {
    gamePane.setSimulationPeriod(50);
    y = gamePane.toPoint(currentCon.locStart).y;

//    if (currentCon.locEnd.y > currentCon.locStart.y)
//      dy = gamePane.animationStep;
//    else
    // Do we need this still? ^^
    dy = -gamePane.animationStep;

    if (currentCon instanceof Snake) {
      navigationPane.showStatus("Digesting...");
      navigationPane.playSound(GGSound.MMM);
      if (Objects.nonNull(paths.get("down"))) {
        paths.put("down", paths.get("down") + 1);
      } else {
        paths.put("down", 1);
      }
    } else {
      navigationPane.showStatus("Climbing...");
      navigationPane.playSound(GGSound.BOING);
      if (Objects.nonNull(paths.get("up"))) {
        paths.put("up", paths.get("up") + 1);
      } else {
        paths.put("up", 1);
      }
    }
  }

  public void act()
  {
    if ((cellIndex / 10) % 2 == 0)
    {
      if (isHorzMirror())
        setHorzMirror(false);
    }
    else
    {
      if (!isHorzMirror())
        setHorzMirror(true);
    }

    // Animation: Move on connection
    if (currentCon != null)
    {

      // skips the animation when true
      if (skip) {
        currentCon = null;
        skip = false;
        return;
      }
      else {
        int x = gamePane.x(y, currentCon);
        setPixelLocation(new Point(x, y));
        y += dy;


        // Check end of connection
        if ((dy > 0 && (y - gamePane.toPoint(currentCon.locEnd).y) > 0)
                || (dy < 0 && (y - gamePane.toPoint(currentCon.locEnd).y) < 0)) {
          gamePane.setSimulationPeriod(100);
          setActEnabled(false);
          setLocation(currentCon.locEnd);
          cellIndex = currentCon.cellEnd;

          setLocationOffset(new Point(0, 0));
          currentCon = null;
          // prepares roll if not relating to moving back one space
          if (!movingBack) {
            navigationPane.prepareRoll(cellIndex);
          }
          movingBack = false;
        }
      }
      return;
    }

    // Normal movement
    if (nbSteps > 0)
    {
      moveToNextCell();

      if (cellIndex == 100)  // Game over
      {
        setActEnabled(false);
        navigationPane.prepareRoll(cellIndex);
        return;
      }

      nbSteps--;
      if (nbSteps == 0)
      {
        // Check if on connection start
        if ((currentCon = gamePane.getConnectionAt(getLocation())) != null)
        {
          // if on a down path and minimum amount rolled then do not follow the path rule
          if (currentCon.cellStart > currentCon.cellEnd
                  && navigationPane.getTotalRoll() == navigationPane.getNumDice()) {
            skip = true;
            setActEnabled(false);
            navigationPane.prepareRoll(cellIndex);
          }
          else {
            connectionMovement();
          }
        }
        else
        {
          setActEnabled(false);
          navigationPane.prepareRoll(cellIndex);
        }
      }
    }
  }

  public String formatRollsOutput() {
    String output = new String();
    for (int i = 0; i < rolls.size(); i++) {
      if (i != 0) {
        output.concat(", ");
      }
      output.concat(i + "-" + rolls.get(i));
    }
    return output;
  }

  public String formatPathsOutput() {
    String output = new String();
    Set<String> keys = paths.keySet();
    List<String> keysList = new ArrayList<>(keys);
    for (int i = 0; i < paths.size(); i++) {
      if (i != 0) {
        output.concat(", ");
      }
      output.concat(keysList.get(i) + "-" + paths.get(keysList.get(i)));
    }
    return output;
  }
}
