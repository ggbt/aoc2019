package ggbt.aoc2019.hullpainting;

import java.util.HashMap;

import ggbt.aoc2019.common.Point;
import ggbt.aoc2019.intcode.IntCode;

public class HullPaintingRobot {

  private final IntCode brain;
  private final HashMap<Point, Color> colorMap;
  private final HashMap<Point, Boolean> visitationMap;
  
  private Direction direction = Direction.UP;
  private Point position = new Point(0, 0);

  private boolean readingPaintState = true;

  public HullPaintingRobot(IntCode brain, HashMap<Point, Color> colorMap, HashMap<Point, Boolean> visitationMap) {
    this.brain = brain;
    this.colorMap = colorMap;
    this.visitationMap = visitationMap;
    
    brain.onInput(this::handleSesor);
    brain.onOutput(this::handleInstruction);
  }
  
  public void run() throws Throwable {
    brain.run();
  }
  
  private Long handleSesor() {
    Color color = this.colorMap.computeIfAbsent(position, p -> Color.BLACK);
    return color.value;
  }
  
  private void handleInstruction(long instruction) {
    if (readingPaintState) {
      Color from = Color.from(instruction);
      paint(from);
      
      readingPaintState = false;
    } else {
      Turn turn = Turn.from(instruction);
      turn(turn);
      
      readingPaintState = true;
    }
  }
  
  public void turn(Turn turn) {
    switch (direction) {
    
    case UP:
      if (turn == Turn.LEFT) {
        direction = Direction.LEFT;
      } else {
        direction = Direction.RIGHT;
      }
      break;

    case DOWN:
      if (turn == Turn.LEFT) {
        direction = Direction.RIGHT;
      } else {
        direction = Direction.LEFT;
      }
      break;
      
    case LEFT:
      if (turn == Turn.LEFT) {
        direction = Direction.DOWN;
      } else {
        direction = Direction.UP;
      }
      break;
      
    case RIGHT:
      if (turn == Turn.LEFT) {
        direction = Direction.UP;
      } else {
        direction = Direction.DOWN;
      }
      break;
    }
    
    position = new Point(position.x + direction.x, position.y + direction.y);
  }
  
  public void paint(Color color) {
    this.colorMap.put(position, color);
    this.visitationMap.put(position, true);
  }
}
